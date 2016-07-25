package systems.crigges.smartphone;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WlanClient {
	private static final int timeout = 1000;

	private ExecutorService subnetScanPool;

	public WlanClient() {
		String clientIP;
		try {
			clientIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		subnetScanPool = Executors.newCachedThreadPool();
		String subnet = clientIP.substring(0, clientIP.lastIndexOf('.'));
		System.out.println(subnet);
		for (int i = 1; i < 255; i++) {
			String host = subnet + "." + i;
			subnetScanPool.submit(new CheckHost(host));
		}
	}

	

	class CheckHost implements Runnable {
		private String host;

		public CheckHost(String host) {
			this.host = host;
		}

		@Override
		public void run() {
			try {
				
				if (InetAddress.getByName(host).isReachable(timeout)) {
					System.out.println(host + " is reachable");
				} else {
					System.out.println(host + " nope");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args){
		new WlanClient();
	}

}
