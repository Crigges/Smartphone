package systems.crigges.smartphone;

public interface Client {
	
	public void startClient();
	
	public void stopClient();
	
	public void connectToClient(String address);
	
	public void disconnect();

}
