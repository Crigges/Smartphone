package systems.crigges.smartphone;

import com.badlogic.gdx.scenes.scene2d.ui.List;

public interface ClientUI {
	
	public void setStatus();
	
	public void showAvailableServers(List<String> names, List<String> address);
	
}
