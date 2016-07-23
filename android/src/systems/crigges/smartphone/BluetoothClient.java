package systems.crigges.smartphone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothClient implements Client {
	private static final int REQUEST_ENABLE_BT = 1;
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private AndroidApplication app;
	private BluetoothAdapter btAdapter;
	private ClientUI ui;
	private ArrayList<BluetoothDevice> availableDevices;
	private ArrayList<String> deviceAddresses;

	private BluetoothSocket btSocket;
	private OutputStream out;
	private InputStream in;
	
	public BluetoothClient(AndroidApplication app, ClientUI ui){
		this.app = app;
		this.ui = ui;
		ui.log("Initalizing Bluetooth adapter...");
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			ui.log("Failed to initalize, is your device Bluetooth capable?");
			ui.setStatus(Status.Ready);
			return;
		} else {
			if (btAdapter.isEnabled()) {
				ui.log("Initialisation successful.");
			} else {
				ui.log("Adapter is diabeld, requesting to enable it");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				app.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				if (btAdapter.isEnabled()) {
					ui.log("Initialisation successful.");
				}else{
					ui.log("Failed to initalize, couldn't enable adapter. Try it manualy.");
					ui.setStatus(Status.Ready);
				}
			}
		}
	}

	@Override
	public void startClient() {
		ui.log("Starting Client...");
		availableDevices = new ArrayList<BluetoothDevice>();
		deviceAddresses = new ArrayList<String>();
		for(BluetoothDevice d : btAdapter.getBondedDevices()){
			availableDevices.add(d);
			deviceAddresses.add(d.getAddress());
		}
		ui.log("Found " + availableDevices.size() + " bound devices. Searching for unbound ones...");
		sendAvailableDevicesToUI();
		ui.log("");
		ui.setStatus(Status.Searching);
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		app.registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getType() == BluetoothDevice.ACTION_FOUND){
					BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if(!deviceAddresses.contains(d.getAddress())){
						deviceAddresses.add(d.getAddress());
						availableDevices.add(d);
						ui.log("Found device named " + d.getName() + " with address: " + d.getAddress());
						sendAvailableDevicesToUI();
					}
				}else{
					ui.log("Scan finished");
					ui.setStatus(Status.Ready);
				}
			}
		}, filter);
		btAdapter.startDiscovery();
	}
	
	private void sendAvailableDevicesToUI(){
		ArrayList<String> tempAddresses = new ArrayList<String>();
		ArrayList<String> tempNames = new ArrayList<String>();
		for(BluetoothDevice d : availableDevices){
			tempAddresses.add(d.getAddress());
			tempNames.add(d.getName());
		}
		ui.showAvailableServers(tempNames, tempAddresses);
	}

	@Override
	public void stopClient() {
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		try {
			if(btSocket != null){
				out.close();
				in.close();
				btSocket.close();
			}
			ui.log("Stopped Client");
		} catch (IOException e) {
			ui.log("Failed to stop Client:\n" + e.getMessage());
			ui.setStatus(Status.Ready);
		}
	}

	@Override
	public void connectToServer(String address) {
		Gdx.app.postRunnable(new ConnectionTask(address));
	}
	
	public class ConnectionTask implements Runnable{
		private String address;
		
		public ConnectionTask(String address) {
			this.address = address;
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			ui.setStatus(Status.Connecting);
			if(btAdapter.isDiscovering()){
				btAdapter.cancelDiscovery();
			}
			BluetoothDevice dev = btAdapter.getRemoteDevice(address);
			ui.log("Connecting to" + dev.getName());
			if(dev.getBondState() != BluetoothDevice.BOND_BONDED){
				ui.log(dev.getName() + " is not bound yet, bounding...");
				dev.createBond();
				ui.log("Try again after device is bounded.");
				return;
			}else{
				try {
					btSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
					ui.log("Opened socket socket successfuly");
				} catch (IOException e) {
					ui.log("Opening socket failed:\n" + e.getMessage());
					ui.setStatus(Status.Ready);
					return;
				}
				try {
					btSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
					ui.log("Opened socket socket successfuly");
				} catch (IOException e) {
					ui.log("Opening socket failed:\n" + e.getMessage());
					ui.setStatus(Status.Ready);
					return;
				}
				try {
					ui.log("Establish socket connection...");
					btSocket.connect();
				} catch (IOException e) {
					ui.log("Socket connenction failed:\n" + e.getMessage());
					ui.setStatus(Status.Ready);
					return;
				}
				ui.log("Opening streams...");
				try {
					out = btSocket.getOutputStream();
					in = btSocket.getInputStream();
					ui.log("Connected successfuly. Audio data is beeing streamed now");
					ui.setStatus(Status.Running);
				} catch (IOException e) {
					ui.log("Couldn't open streams:\n" + e.getMessage());
					ui.setStatus(Status.Ready);
				}
			}
		}
		
	}
	

	@Override
	public void disconnect() {
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		try {
			out.close();
			in.close();
			btSocket.close();
			ui.log("Disconnected");
		} catch (IOException e) {
			ui.log("Failed to close connection:\n" + e.getMessage());
			ui.setStatus(Status.Ready);
		}
	}

}
