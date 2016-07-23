package systems.crigges.smartphone;

import java.net.SocketImpl;

public interface ServerUI {
	
	public void log(String msg);
	
	public void setDeviceStatus(boolean avail, String name, String address);
	
	public void setStatus(Status e);
	
	public void displayIP(String ip);

	public int getPort();

}
