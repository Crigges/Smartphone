package systems.crigges.smartphone;

import java.util.List;

public interface ClientUI {
	
	public void log(String s);
	
	public void setStatus(Status s);
	
	public void setAdapterAvailable(boolean available);
	
	public void showAvailableServers(List<String> names, List<String> address);
	
}
