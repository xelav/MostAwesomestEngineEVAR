package body;

import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.maee.MAE;
import com.me.maee.Utils;
import com.me.maee.Vec;

public class Circle extends Body {
	
	Vec AnglePoint = new Vec();
	
	//Конструктор
	//////////
	private void Constructor (float x ,float y, Vec vel, Vec F, float R){
		density = new Random().nextFloat();
		setPosition(new Vec(x,y));
		setVelocity(vel);
		this.R = R;
		mass = defineSquare()*(MAE.MASS_COEFFICIENT*density);
		System.out.println(mass);
		aabb = buildAABB();
		angle = (float) (new Random().nextFloat()*(Math.PI+Math.PI));
		AnglePoint = new Vec ( Math.cos(angle)*R, Math.sin(angle)*R );
		rotationSpeed = new Random().nextFloat()*MAE.MAX_ROTATION_SPEED;
		type = BodyType.CIRCLE;
		
		renderer.setColor(1,1,1,density);
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
		angleRenderer.begin(ShapeType.Line);
		//System.out.println((float) Math.cos(angle)+" "+pos.x);
		angleRenderer.line(pos.x, pos.y,(float) Math.cos(angle)*R+pos.x,(float)  Math.sin(angle)*R+pos.y); // !!! Слишком много рассчетов
		angleRenderer.end();
		posRenderer.begin(ShapeType.Point);
		posRenderer.point(pos.x, pos.y, 0);
		posRenderer.end();

	}
	
	private void updatePosition(float dT){
		setPosition(pos.add(vel.scl(dT)));
	}
	private void updateRotation(float dT){
		//rotate (dT*rotationSpeed);
		angle += dT*rotationSpeed;
	}
	
	public void update(float dT) {
		//gravity();
		updatePosition(dT);
		aabb = buildAABB();
		updateRotation(dT);
		setColor();
	}

	public void test(){
		
	}
	
	public float defineSquare() {
		return (float) (Math.PI*Math.PI*R);
	}
	@Override
	public void rotate(float angle) {
		this.angle += angle;
		if (this.angle >= Math.PI*2) this.angle -= Math.PI*2;
	}
	@Override
	public AABB buildAABB () {
		return new AABB(new Vec (pos.x-R,pos.y-R), new Vec (pos.x+R,pos.y+R));
	}
}
