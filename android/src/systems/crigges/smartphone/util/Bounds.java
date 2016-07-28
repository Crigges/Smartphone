package systems.crigges.smartphone.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bounds {
	public int x, y, width, height;
	
	public Bounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Bounds add(Bounds b){
		x += b.x;
		y += b.y;
		return this;
	}
	
	public void applyToActor(Actor a){
		a.setBounds(x, y, width, height);
	}
	
	public static void setBoundsForActor(Actor a, Bounds b){
		a.setBounds(b.x, b.y, b.width, b.height);
	}
}
