package systems.crigges.smartphone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Align;

import android.media.AudioRecord;

public class MainFrame extends ApplicationAdapter implements ClientUI {
	private AndroidApplication app;
	Skin skin;
	Table tableMain;
	Table subtable;
	Stage stage;
	Label log;
	List<String> deviceList;
	TextButton searchDevices;
	CheckBox connectionMethodWlan;
	CheckBox connectionMethodBluetooth;

	public MainFrame(AndroidLauncher androidLauncher) {
		this.app = androidLauncher;
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		tableMain = new Table();
		subtable = new Table();
		deviceList = new List<String>(skin);
		searchDevices = new TextButton("Search Devices", inittextButtonStyle());
		deviceList.setItems("one", "two", "three", "four");
		stage = new Stage();
		connectionMethodWlan = new CheckBox("Use Wireless Lan", skin);
		connectionMethodBluetooth = new CheckBox("Use Bluetooth", skin);
		log = new Label("Log initilised", skin);
		
		stage.addActor(tableMain);
		stage.addActor(subtable);
		
		tableMain.setFillParent(true);
		Label title = new Label("MicroPHONE", skin);
		tableMain.add(title).expand().fill().align(Align.top);
		title.setAlignment(Align.center);
		tableMain.row();
		tableMain.add(subtable);
		tableMain.row();
		tableMain.add(log).fill().expand().align(Align.bottomRight);
		tableMain.add(deviceList).fill().expand().align(Align.bottomLeft);
		
		subtable.add(searchDevices);
		subtable.row();
		subtable.add(connectionMethodBluetooth);
		subtable.add(connectionMethodWlan);
		
		new WlanClient(app);
	}

	private TextButtonStyle inittextButtonStyle(){
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round");
        textButtonStyle.checked = skin.getDrawable("default-round");
        skin.addRegions(buttonAtlas);
        return textButtonStyle;
	}
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void log(String s) {
		log.setText(s);
	}

	@Override
	public void setStatus(Status s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAdapterAvailable(boolean available) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showAvailableServers(java.util.List<String> names, java.util.List<String> address) {
		// TODO Auto-generated method stub
		
	}
}
