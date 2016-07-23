package systems.crigges.smartphone;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.sound.sampled.LineUnavailableException;

public class TCPServer implements Server {
	private ServerUI ui;
	private Thread serverThread;
	private boolean running = false;
	private InetAddress hostAddress;
	private ServerSocket socket;
	private Socket client;
	private OutputStream out;
	private InputStream in;

	public TCPServer(ServerUI ui) {
		this.ui = ui;
		ui.log("Retriving host address...");
		try {
			hostAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			ui.log("Couldn't get host:\n" + e1.getMessage());
			ui.setStatus(Status.Pending);
			return;
		}
		ui.log("Found host with IP: " + hostAddress.getHostAddress() + " Creating Socket...");
		try {
			socket = new ServerSocket(ui.getPort(), 3, hostAddress);
		} catch (IOException e) {
			ui.log("Couldn't create sorcket:\n" + e.getMessage());
			ui.setStatus(Status.Pending);
			return;
		}
		ui.displayIP(hostAddress.getHostAddress());
		ui.log("Socket created successfuly");
	}

	public void startServer() {
		running = true;
		serverThread = Thread.currentThread();
		ui.setStatus(Status.Connecting);
		ui.log("Starting Server");
		serverThread = new Thread(new AcceptTask());
		serverThread.start();
	}

	class AcceptTask implements Runnable {

		@Override
		public void run() {
			try {
				client = socket.accept();
			} catch (IOException e) {
				ui.log("Connection failed:\n" + e.getMessage());
				ui.setStatus(Status.Ready);
				return;
			}
			ui.log("Client with IP: " + client.getInetAddress().getHostAddress() + " connected to Server");
			ui.log("Opening streams...");
			try {
				out = client.getOutputStream();
				in = client.getInputStream();
			} catch (IOException e) {
				ui.log("Coudln't open streams:\n" + e.getMessage());
				ui.setStatus(Status.Ready);
				return;
			}
			ui.log("Audio data is beeing streamed now!");
			ui.setStatus(Status.Running);
			byte[] pcmData = new byte[2048];
			PCMPlayback player;
			try {
				 player = new PCMPlayback();
			} catch (LineUnavailableException e1) {
				ui.log("No suitable audio device was found:\n" + e1.getMessage());
				ui.setStatus(Status.Ready);
				return;
			}
			try {
				while (true) {
					in.read(pcmData);
					player.play(pcmData);
				}
			} catch (IOException e) {
				ui.log("Stream was interrupted:\n" + e.getMessage());
				ui.setStatus(Status.Ready);
				return;
			}
		}

	}

	@Override
	public void stopServer() {
		if (!running) {
			try {
				socket.close();
			} catch (IOException e) {
				//just try
			}
			ui.setStatus(Status.Pending);
			return;
		}
		running = false;
		serverThread.interrupt();
		if(client != null){
			try {
				client.close();
				out.close();
				in.close();
			} catch (IOException e) {
				//doesnt matter
			}
		}
		ui.log("Server was shutdown.");
		ui.setStatus(Status.Pending);
		serverThread.interrupt();
	}
}
