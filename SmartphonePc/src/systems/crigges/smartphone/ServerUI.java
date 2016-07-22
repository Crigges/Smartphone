package systems.crigges.smartphone;

public interface ServerUI {
	
	public void log(String msg);
	
	public void setDeviceStatus(boolean avail, String name, String address);
	
	public void setStatus(Status e);

}
