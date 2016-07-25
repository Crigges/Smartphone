package systems.crigges.smartphone;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.backends.android.AndroidApplication;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WlanClient implements Client {
	private static final int timeout = 1000;

	private AndroidApplication app;
	private ExecutorService subnetScanPool;

	public WlanClient(AndroidApplication app) {
		this.app = app;
		String clientIP = getWifiIpAddress();
		subnetScanPool = Executors.newCachedThreadPool();
		String subnet = clientIP.substring(0, clientIP.lastIndexOf('.'));
		for (int i = 1; i < 255; i++) {
			String host = subnet + "." + i;
			subnetScanPool.submit(new CheckHost(host));
		}
	}

	private String getWifiIpAddress() {
		WifiManager wifiManager = (WifiManager) app.getContext().getSystemService(Context.WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			ipAddress = Integer.reverseBytes(ipAddress);
		}

		byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

		String ipAddressString;
		try {
			ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
		} catch (UnknownHostException ex) {
			// ui.log("Unable to get host address.");
			ipAddressString = null;
		}

		return ipAddressString;
	}

	class CheckHost implements Runnable {
		private String host;

		public CheckHost(String host) {
			this.host = host;
		}

		@Override
		public void run() {
			try {
				Socket soc = new Socket();
				soc.connect(new InetSocketAddress(host, 80), 1000);
				System.out.println("yep: " + host);
			} catch (IOException ex) {
				System.out.println("nope: " + host);
//				ex.printStackTrace();
			}
		}

	}

	@Override
	public void startClient() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopClient() {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectToServer(String address) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

}
