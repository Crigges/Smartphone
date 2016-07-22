package systems.crigges.smartphone;

import java.util.ArrayList;
import java.util.Set;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.sun.crypto.provider.ARCFOURCipher;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothClient implements Client {
	private static final int REQUEST_ENABLE_BT = 1;
	
	private AndroidApplication app;
	private BluetoothAdapter btAdapter;
	private ClientUI ui;
	private ArrayList<BluetoothDevice> availableDevices;
	private ArrayList<String> deviceAddresses;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectToClient(String address) {
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
			//TODO
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

}
