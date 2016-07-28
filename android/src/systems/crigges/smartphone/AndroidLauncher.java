package systems.crigges.smartphone;

import android.os.Bundle;
import systems.crigges.smartphone.cgui.AppWindow;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//new ConnectTest(this);
		//newMainFrame(this)
		initialize(new AppWindow(this), config);
		
	}
}
