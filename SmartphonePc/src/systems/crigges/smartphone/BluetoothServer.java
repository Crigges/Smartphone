package systems.crigges.smartphone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.sound.sampled.LineUnavailableException;

public class BluetoothServer implements Server {
	private ServerUI ui;
	private Thread serverThread;
	private boolean running = false;

	public BluetoothServer(ServerUI ui) {
		this.ui = ui;
		ui.log("Searching for local Bluetooth device...");
		LocalDevice localDevice = null;
		try {
			localDevice = LocalDevice.getLocalDevice();
			if(localDevice.getFriendlyName() == null){
				ui.log("Device named with address: "
						+ localDevice.getBluetoothAddress() + " was found. But it is not useable!");
				ui.setStatus(Status.Pending);
				ui.setDeviceStatus(false, null, null);
				return;
			}
			ui.log("Device named " + localDevice.getFriendlyName() + " with address: "
					+ localDevice.getBluetoothAddress() + " was found.");
			ui.setDeviceStatus(true, localDevice.getFriendlyName(), localDevice.getBluetoothAddress());
			ui.setStatus(Status.Ready);
		} catch (BluetoothStateException e) {
			ui.log("No compatible device was found, please check if the adapter is enabeld and proper drivers are installed.");
			ui.setDeviceStatus(false, null, null);
			ui.setStatus(Status.Pending);
		}
	}

	public void startServer() {
		running = true;
		serverThread = Thread.currentThread();
		ui.setStatus(Status.Running);
		ui.log("Starting Server");
		// Create a UUID for SPP
		UUID uuid = new UUID("446118f08b1e11e29e960800200c9a66", false);
		// Create the servicve url
		String connectionString = "btspp://localhost:" + uuid + ";name=Sample SPP Server";

		// open server url
		StreamConnectionNotifier streamConnNotifier;
		StreamConnection connection;
		try {
			streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);
			ui.log("Opened connection successful.");
			ui.log("Server is now running. Waiting for clients to connect...");
			ui.setStatus(Status.Awaiting);
			connection = streamConnNotifier.acceptAndOpen();
		} catch (IOException e) {
			ui.log("Could not open connection:\n" + e.getMessage() + "\nCheck github for Help");
			ui.log("Server was stopped");
			ui.setStatus(Status.Ready);
			return;
		}

		RemoteDevice dev = null;
		try {
			dev = RemoteDevice.getRemoteDevice(connection);
			ui.log("Found a device named " + dev.getFriendlyName(true) + " and address: " + dev.getBluetoothAddress());
		} catch (IOException e) {
			ui.log("Found device, but couldn't connect to it:\n" + e.getMessage());
			ui.log("Server was stopped");
			ui.setStatus(Status.Ready);
			return;
		}

		ui.log("Opening streams...");
		InputStream in = null;
		ObjectInputStream objIn = null;
		OutputStream out = null;
		try {
			in = connection.openInputStream();
			objIn = new ObjectInputStream(in);
			out = connection.openOutputStream();
		} catch (IOException e1) {
			ui.log("Couldn't open Stream:\n" + e1.getMessage());
			ui.log("Server was stopped.");
			ui.setStatus(Status.Ready);
		}
		try {
			streamConnNotifier.close();
			ui.log("Search for new Clients has stoped.");
			ui.log("Audio data is beeing streamed.");
			ui.setStatus(Status.Running);
		} catch (IOException e) {
			ui.log("Couldn't stop connection notifier:\n" + e.getMessage());
			ui.log("Server was stopped.");
			ui.setStatus(Status.Ready);
		}
		try {
			PCMPlayback playback = new PCMPlayback();
			while (true) {
				short[] pcmData = (short[]) objIn.readObject();
				
			}
		} catch (ClassNotFoundException | IOException | LineUnavailableException e) {
			ui.log("Could not playback sound:\n" + e.getMessage());
			ui.log("Server was stopped.");
			ui.setStatus(Status.Ready);
		}
	}

	@Override
	public void stopServer() {
		if(!running){
			ui.setStatus(Status.Pending);
			return;
		}
		running = false;
		ui.log("Server was shutdown.");
		ui.setDeviceStatus(false, null, null);
		ui.setStatus(Status.Pending);
		serverThread.interrupt();
	}
}