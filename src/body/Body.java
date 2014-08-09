package body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.me.maee.MAE;
import com.me.maee.Utils;
import com.me.maee.Vec;

import java.lang.Math;;

public abstract class Body {
	
	protected AABB aabb; 
	protected Vec pos;
	protected Vec vel;
	//private float angle;
	//protected boolean isStatic;
	public static ShapeRenderer  renderer; // should be protected!
	public float mass;
	//protected ShapeRenderer posRenderer;
	
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
	protected void setRenderer(){
		renderer = new ShapeRenderer();
	}
	public Vec getPosition (){
		return pos;
	}
	public Vec getVelocity(){
		return vel;
	}
	///////////
	
	public void applyImpulse(float x, float y, Vec normal, float impulse){
		// applyImpulse - улучшенная версия applyForce; учитывает также точку приложения и массу
		// normal - единичный вектор, отвечающий ТОЛЬКО за направление
		Utils.getUnitVector(normal);
		Vec Vel = getVelocity();
		Vel.x += impulse*normal.x/mass;
		Vel.y += impulse*normal.y/mass;
		setVelocity(Vel);
	}
	public void setCollision(boolean collide){
		this.collide = collide;
		//if (collide == false) System.out.println(""+this.collide);
	}
	
	public void setColor(){
		//System.out.println(""+collide);
		if (collide) renderer.setColor(0.9f, 0.2f, 0.2f, 1);
		else renderer.setColor(1, 1, 1, 1);
	}
	public void setColorRed(){
		System.out.println("ok");
		renderer.setColor(0.3f, 0.3f, 0.3f, 1);
	}
	
	protected float defineMass(){
		float mass=defineSquare();
		return mass;
	}
	public AABB getRect(){
		return aabb;
		//return buildAABB();
	}
	
}
