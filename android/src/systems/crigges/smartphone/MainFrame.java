package systems.crigges.smartphone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		SmartFontGenerator fontGen = new SmartFontGenerator();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
		BitmapFont fontMedium = fontGen.createFont(exoFile, "exo-medium", 40);
		Label.LabelStyle mediumStyle = new Label.LabelStyle();
		mediumStyle.font = fontMedium;
		BitmapFont fontSmall = fontGen.createFont(exoFile, "exo-small", 24);
		Label.LabelStyle smallStyle = new Label.LabelStyle();
		smallStyle.font = fontSmall;
		skin.get("default", TextButtonStyle.class).font = fontSmall;
		skin.get("default", CheckBoxStyle.class).font = fontSmall;
		skin.get("default", LabelStyle.class).font = fontSmall;
		skin.get("default", ListStyle.class).font = fontSmall;
		
		tableMain = new Table();
		subtable = new Table();
		deviceList = new List<String>(skin);
		searchDevices = new TextButton("Search Devices", skin);
		deviceList.setItems("one", "two", "three", "four");
		connectionMethodWlan = new CheckBox("Use Wirless LAN", skin);
		connectionMethodBluetooth = new CheckBox("Use Bluetooth", skin);
		log = new Label("Log initilised", skin);
		ScrollPane pane = new ScrollPane(log);
		
		stage.addActor(tableMain);
		stage.addActor(subtable);
		
		tableMain.setFillParent(true);
		Label title = new Label("MicroPHONE", mediumStyle);
		tableMain.add(title).expandX().fillX().center().spaceBottom(Value.percentHeight(2f));
		tableMain.row();
		tableMain.add(connectionMethodBluetooth).left();
		tableMain.add(connectionMethodWlan).right();
		tableMain.row();
		tableMain.add(searchDevices).expandX().space(Value.percentHeight(2.5f));
		tableMain.row();
		tableMain.add(pane).bottom().left().expand().fill();
		tableMain.add(subtable).bottom().right().expand().fill();
		
		subtable.add(new Label("Devices", smallStyle));
		subtable.row();
		subtable.add(deviceList).fill().expand();
		
		
		//new WlanClient(app);
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
