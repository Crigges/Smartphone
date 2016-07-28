package systems.crigges.smartphone.cgui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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

	private static final Bounds logBounds = new Bounds(50, 210, 980, 400);
	private static final Bounds logLabelBounds = new Bounds(55, 620, 200, 400);
	private static final Bounds startButtonBounds = new Bounds(560, 40, 480, 150);
	private static final Bounds stopButtonBounds = new Bounds(40, 40, 480, 150);
	

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
		initButtons();
		initLog();
		log("Log initalized.");
		log("This are sample log messages");
		log("This are sample log messages");
		log("This are sample log messages");
		log("This are sample log messages");
		Gdx.input.setInputProcessor(stage);
	}
	
	private void initLog() {
		log = new TextArea("", AssetFactory.getDefaultTextFieldStyle());
		logBounds.applyToActor(log);
		log.setTouchable(Touchable.disabled);
		stage.addActor(log);
		Label logLabel = new Label("Log:", new LabelStyle(AssetFactory.getFont("normal", 45), Color.WHITE));
		logLabel.setAlignment(Align.bottomLeft);
		logLabelBounds.applyToActor(logLabel);
		stage.addActor(logLabel);
	}

	private void initButtons() {
		startButton = new TextButton("Start", AssetFactory.getDefaultButtonStyle());
		//startButton.getLabel().setFontScale(AssetFactory.getFontScaleX(), AssetFactory.getFontScaleY());
		startButtonBounds.applyToActor(startButton);
		stage.addActor(startButton);
		stopButton = new TextButton("Stop", AssetFactory.getDefaultButtonStyle());
		//stopButton.getLabel().setFontScale(AssetFactory.getFontScaleX(), AssetFactory.getFontScaleY());
		stopButtonBounds.applyToActor(stopButton);
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
