package body;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.me.maee.MAE;
import com.me.maee.Stats;
import com.me.maee.Utils;
import com.me.maee.Vec;

import java.lang.Math;;

public abstract class Body {
	
	protected AABB aabb; 
	protected Vec pos;
	protected Vec vel;
	public float R;
	protected float angle;
	protected float rotationSpeed;
	public boolean isStatic;
	protected static ShapeRenderer  renderer;
	protected static ShapeRenderer angleRenderer;
	protected static ShapeRenderer posRenderer;
	protected float mass;
	protected float density;
	
	public boolean collide;
	public byte type;
	
	public abstract void draw();
	public abstract void update(float deltaTime);
	protected abstract float defineSquare();
	protected abstract void rotate(float angle);
	protected abstract AABB buildAABB();
	///////////
	protected void setVelocity(Vec NewVel){
		vel = NewVel;
	}
	protected void setPosition (Vec NewPos){
		//if (vel != null){
			//if (NewPos.y == 180){
				//System.out.println(vel.y);
			//}
		//}
		//System.out.println(NewPos.y);
		this.pos = NewPos;
	}
	public static void setRenderers(){
		renderer = new ShapeRenderer();
		posRenderer = new ShapeRenderer();
		posRenderer.setColor(Color.GREEN);
		angleRenderer = new ShapeRenderer();
		angleRenderer.setColor(0.7f,0.7f,0.7f,1);
	}
	public Vec getPosition (){
		return pos;
	}
	public Vec getVelocity(){
		return vel;
	}
	///////////
	public void applyImpulse(float x, float y, Vec normal, float impulse){
		// normal - единичный вектор, отвечающий ТОЛЬКО за направление
		Utils.getUnitVector(normal);
		Vec Vel = getVelocity();
		Vel.x += impulse*normal.x/mass;
		Vel.y += impulse*normal.y/mass;
		setVelocity(Vel);
		
		//if (type == BodyType.SHAPE) {
			//System.out.println(x+" "+" "+y+" "+impulse);
			//Vel.write();
			//normal.write();
		//}
	}
	public void applyAngularImpulse (float dV){
		
		rotationSpeed += mass*dV/R/1000;
		
	}
	
	public void setCollision(boolean collide){
		this.collide = collide;
		//if (collide == false) System.out.println(""+this.collide);
	}
	
	public void setColor(){
		//System.out.println(""+collide);
		if (collide) renderer.setColor(0.9f, 0.2f, 0.2f, density);
		else renderer.setColor(1, 1, 1, density);
	}
	public AABB getRect(){
		return aabb;
		//return buildAABB();
	}
	public float getMass(){
		return mass;
	}
	protected void gravity(){
		vel.add(new Vec(0, -5));
	}
	
}
