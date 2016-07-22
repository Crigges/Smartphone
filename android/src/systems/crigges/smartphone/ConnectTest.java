package systems.crigges.smartphone;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ConnectTest{
	
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private AndroidApplication app;

	// Well known SPP UUID
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// Insert your server's MAC address
	private static String address = "E4:B3:18:44:8D:AF";

	public ConnectTest(AndroidApplication app) {
		this.app = app;
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		checkBTState();
		onResume();
	}

	public void onResume() {
		
		// Set up a pointer to the remote node using it's address.
		for(BluetoothDevice d : btAdapter.getBondedDevices()){
			System.out.println(d.getName());
		}
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		// Two things are needed to make a connection:
		// A MAC address, which we got above.
		// A Service ID or UUID. In this case we are using the
		// UUID for SPP.
		try {
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
			alertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
		}

		// Discovery is resource intensive. Make sure it isn't going on
		// when you attempt to connect and pass your message.
		btAdapter.cancelDiscovery();

		// Establish the connection. This will block until it connects.
		try {
			btSocket.connect();
		} catch (IOException e) {
			try {
				btSocket.close();
			} catch (IOException e2) {
				alertBox("Fatal Error",
						"In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
			}
		}

		// Create a data stream so we can talk to server.

		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			alertBox("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
		}

		String message = "Hello from Android.\n";
		byte[] msgBuffer = message.getBytes();
		try {
			outStream.write(msgBuffer);
		} catch (IOException e) {
			String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
			if (address.equals("00:00:00:00:00:00"))
				msg = msg
						+ ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
			msg = msg + ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

			alertBox("Fatal Error", msg);
		}
	}

	private void checkBTState() {
		// Check for Bluetooth support and then check to make sure it is turned
		// on

		// Emulator doesn't support Bluetooth and will return null
		if (btAdapter == null) {
			alertBox("Fatal Error", "Bluetooth Not supported. Aborting.");
		} else {
			if (btAdapter.isEnabled()) {
 
			} else {
				// Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
				app.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	public void alertBox(String title, String message) {
		new AlertDialog.Builder(app).setTitle(title).setMessage(message + " Press OK to exit.")
				.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						app.finish();
					}
				}).show();
	}
}