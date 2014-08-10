package body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.maee.Utils;
import com.me.maee.Vec;

public class Circle extends Body {

	//public float R;
	//Конструктор
	//////////
	private void Constructor (float x ,float y, Vec vel, Vec F, float R){
		setPosition(new Vec(x,y));
		//setRenderer();
		setVelocity(vel);
		this.R = R;
		mass = defineMass();
		aabb = buildAABB();
		//posRenderer= new ShapeRenderer();
		//posRenderer.setColor(0,1,0,1);
		type = BodyType.CIRCLE;
	}
	public Circle (){
		Constructor (0,0,new Vec(0,0) , new Vec(0,0), 1);
	}
	
	public Circle (float x,float y, float R){
		Constructor (x,y, new Vec(0,0), new Vec(0,0), R);
	}
	
	public Circle (float x,float y, float R, Vec vel){
		Constructor (x,y, vel, new Vec(0,0), R);
	}
	public Circle (Vec pos, float R){
		Constructor(pos.x,pos.y,new Vec(0,0),new Vec(0,0),R);
	}
	///////////
	
	public void draw() {
		//aabb.draw();
		setColor();
		renderer.begin(ShapeType.Circle);
		renderer.circle(getPosition().x, getPosition().y, R);
		renderer.end();
		//posRenderer.begin(ShapeType.Point);
		//posRenderer.point(pos.x, pos.y, 0);
		//posRenderer.end();

	}
	
	private void updatePosition(float dT){
		setPosition(pos.add(vel.scl(dT)));
	}
	
	public void update(float dT) {
		updatePosition(dT);
		aabb = buildAABB();
		//updateRotation(dT);
		setColor();
	}

	public void test(){
		
	}
	
	public float defineSquare() {
		return (float) (Math.PI*Math.PI*R);
	}
	@Override
	public void rotate(float angle) {
		Utils.rotatePoint(angle, this.angle);
	}
	@Override
	public AABB buildAABB () {
		return new AABB(new Vec (pos.x-R,pos.y-R), new Vec (pos.x+R,pos.y+R));
	}
}
