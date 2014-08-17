package body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.maee.Vec;

public class Line {
	/*
	 *  Used only for drawing line
	 */
	
	ShapeRenderer renderer;
	public Vec A;
	public Vec B;
	
	public Line(Vec a2, Vec pointer){
		this.A = a2;
		this.B = pointer;
		renderer = new ShapeRenderer();
		//System.out.println("Line is created!");
	}
	
	public Line (){
		this.A = new Vec(0,0);
		this.B = new Vec(0,0);
		renderer = new ShapeRenderer();
	}

	public void draw() {
		renderer.begin(ShapeType.Line);
		renderer.line(A.x, A.y, B.x, B.y);
		renderer.end();
	}
	
	public void finalize(){
		//System.out.println("Line is destroyed");
	}
	
	public float getdX(){
		return (B.x-A.x);
	}
	public float getdY(){
		return (B.y-A.y);
	}
	
}