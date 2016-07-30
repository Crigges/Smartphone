package systems.crigges.smartphone.cgui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import systems.crigges.smartphone.AndroidLauncher;
import systems.crigges.smartphone.Client;
import systems.crigges.smartphone.ClientUI;
import systems.crigges.smartphone.Status;
import systems.crigges.smartphone.util.AssetFactory;
import systems.crigges.smartphone.util.Bounds;


public class AppWindow extends ApplicationAdapter implements ClientUI {
	private static final int viewportWidth = 1080;
	private static final int viewportHeight = 1776;

	private static final Bounds titleBounds = new Bounds(300, 1680, 400, 90);
	private static final Bounds bluetoothBoxBounds = new Bounds(80, 1540, 400, 100);
	private static final Bounds wlanBoxBounds = new Bounds(610, 1540, 400, 100);
	private static final Bounds serverListLabelBounds = new Bounds(55, 1420, 200, 100);
	private static final Bounds serverListBounds = new Bounds(40, 920, 1000, 500);
	private static final Bounds connectButtonBounds = new Bounds(545, 780, 500, 125);
	private static final Bounds disconnectButtonBounds = new Bounds(35, 780, 500, 125);
	private static final Bounds volumeSliderBounds = new Bounds(50, 500, 600, 125);
	private static final Bounds muteBoxBounds = new Bounds(600, 500, 500, 125);
	private static final Bounds logBounds = new Bounds(40, 30, 1000, 350);
	private static final Bounds logLabelBounds = new Bounds(55, 380, 200, 400);



	private AndroidApplication app;
	private Client client;
	private Stage stage;
	private OrthographicCamera camera;
	private StretchViewport viewport;
	private TextArea log;
	private TextButton startButton;
	private TextButton stopButton;
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private List<String> serverList;
	private ArrayList<String> availableServers;
	private CheckBox bluetoothBox;
	private CheckBox wlanBox;
	private CheckBox muteBox;
	private Slider volumeSlider;

	public AppWindow(AndroidLauncher androidLauncher) {
		this.app = androidLauncher;
	}

	@Override
	public void create() {
		AssetFactory.setViewportBounds(viewportWidth, viewportHeight);
		AssetFactory.loadAllRessources();
		
		camera = new OrthographicCamera();
		camera.zoom = 1f;
		camera.position.set(viewportWidth / 2, viewportHeight / 2, camera.position.z);
		viewport = new StretchViewport(viewportWidth, viewportHeight, camera);
		stage = new Stage(viewport);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		System.out.println("Height: " + Gdx.graphics.getHeight() + "    ||    Width: " + Gdx.graphics.getWidth());
		initTitleLabel();
		initVolumeSlider();
		initMuteBox();
		initCheckBoxes();
		initButtons();
		initLog();
		initServerList();
		log("Log initalized.");
		log("This are sample log messages");
		log("This are sample log messages");
		log("This are sample log messages");
		log("This are sample log messages");
		Gdx.input.setInputProcessor(stage);
	}

	private void initVolumeSlider() {
		volumeSlider = new Slider(1f, 100f, 0.01f, false, AssetFactory.getDefaultSliderStyle());
		volumeSliderBounds.applyToActor(volumeSlider);
		volumeSlider.setValue(1f);
		stage.addActor(volumeSlider);
	}

	private void initMuteBox() {
		muteBox = new CheckBox("", AssetFactory.getMicMuteBoxStyle());
		muteBoxBounds.applyToActor(muteBox);
		muteBox.getCells().get(0).size(175, 175);
		stage.addActor(muteBox);
	}

	private void initCheckBoxes() {
		bluetoothBox = new CheckBox("Use Bluetooth", AssetFactory.getDefaultCheckBoxStyle());
		bluetoothBoxBounds.applyToActor(bluetoothBox);
		bluetoothBox.getCells().get(0).size(100, 100);
		stage.addActor(bluetoothBox);
		wlanBox = new CheckBox("Use Wlan", AssetFactory.getDefaultCheckBoxStyle());
		wlanBoxBounds.applyToActor(wlanBox);
		wlanBox.getCells().get(0).size(100, 100);
		stage.addActor(wlanBox);
	}

	private void initTitleLabel() {
		Label smart = new Label("Smart", new LabelStyle(AssetFactory.getFont("normal", 90), Color.WHITE));
		titleBounds.applyToActor(smart);
		stage.addActor(smart);
		Label phone = new Label("PHONE", new LabelStyle(AssetFactory.getFont("normal", 90), Color.CYAN));
		titleBounds.applyToActor(phone);
		phone.setX(phone.getX() + smart.getPrefWidth());
		stage.addActor(phone);
	}

	private void initServerList() {
		Label listLabel = new Label("Available Servers:", new LabelStyle(AssetFactory.getFont("normal", 55), Color.WHITE));
		serverListLabelBounds.applyToActor(listLabel);
		listLabel.setAlignment(Align.bottomLeft);
		stage.addActor(listLabel);
		serverList = new List<String>(AssetFactory.getDefaultListStyle());
		serverListBounds.applyToActor(serverList);
		serverList.setItems("test item1", "test item 3541234");
		stage.addActor(serverList);
	}

	private void initLog() {
		log = new TextArea("", AssetFactory.getDefaultTextFieldStyle());
		logBounds.applyToActor(log);
		log.setTouchable(Touchable.disabled);
		stage.addActor(log);
		Label logLabel = new Label("Log:", new LabelStyle(AssetFactory.getFont("normal", 55), Color.WHITE));
		logLabel.setAlignment(Align.bottomLeft);
		logLabelBounds.applyToActor(logLabel);
		stage.addActor(logLabel);
	}

	private void initButtons() {
		startButton = new TextButton("Connect", AssetFactory.getDefaultButtonStyle());
		//startButton.getLabel().setFontScale(AssetFactory.getFontScaleX(), AssetFactory.getFontScaleY());
		connectButtonBounds.applyToActor(startButton);
		stage.addActor(startButton);
		stopButton = new TextButton("Disconnect", AssetFactory.getDefaultButtonStyle());
		//stopButton.getLabel().setFontScale(AssetFactory.getFontScaleX(), AssetFactory.getFontScaleY());
		disconnectButtonBounds.applyToActor(stopButton);
		stage.addActor(stopButton);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		super.resize(width, height);
	}

	@Override
	public void log(String s) {
		log.setText(log.getText() + "[" + sdf.format(cal.getTime()) + "] " + s + "\n");
	}

	@Override
	public void setAdapterAvailable(boolean available) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showAvailableServers(java.util.List<String> names, java.util.List<String> address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(Status s) {
		// TODO Auto-generated method stub
		
	}
}
