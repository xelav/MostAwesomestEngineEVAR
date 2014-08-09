package com.me.maee;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Stats {
	
	static BitmapFont font = new BitmapFont();
	static SpriteBatch batch = new SpriteBatch();
	 
	private Stats() {} // lol
	
	public static void render (boolean b) {
		batch.begin();
		if (b) {
			font.draw(batch, "yep",0, MAE.GLOBAL_HEIGHT-20);
		} else { font.draw(batch, "nope",0, MAE.GLOBAL_HEIGHT-20); }
		batch.end();
	}
	
	public static void render (){
		batch.begin();
		font.draw(batch, "ok", 0, MAE.GLOBAL_HEIGHT-15);
		batch.end();
	}
	
	public static void write(boolean a, boolean b){
		batch.begin();
		if (a && !b) {font.draw(batch, "a !b", 0, MAE.GLOBAL_HEIGHT-30);}
		if (!a && !b) {font.draw(batch, "!a !b", 0, MAE.GLOBAL_HEIGHT-30);}
		if (a && b) {font.draw(batch, "a b", 0, MAE.GLOBAL_HEIGHT-30);}
		if (!a && !b) {font.draw(batch, "!a !b", 0, MAE.GLOBAL_HEIGHT-30);}
		batch.end();
	}
	
	public static void write(float[] n, int str ){
		batch.begin();
			font.draw(batch,Arrays.toString(n),0,MAE.GLOBAL_HEIGHT-(str*15));
		batch.end();
	}
	
}
