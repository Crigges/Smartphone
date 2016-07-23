package systems.crigges.smartphone;

public interface Client {
	
	public void startClient();
	
	public void stopClient();
	
	public void connectToServer(String address);
	
	public void disconnect();

}
