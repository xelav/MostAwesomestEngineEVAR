package body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.maee.Stats;
import com.me.maee.Vec;

public class Point extends Body {
	
	private void Constructor (float x ,float y, Vec vel, float mass, Vec F){
		this.pos = new Vec(x,y);
		renderer = new ShapeRenderer();
		this.vel = vel;
		this.mass = mass;
	}

	Point(float x, float y) {
		Constructor(x,y,new Vec(), 1, new Vec() );
	}
	Point (float x, float y, Vec vel){
		Constructor(x,y,vel,1,new Vec());
	}

	@Override
	public void draw() {
		renderer.begin(ShapeType.Point);
		renderer.setColor(1, 0, 0, 1);
		Stats.render();
		//Stats.write(new float[]{MAE.GLOBAL_HEIGHT,pos.y}, 9); //почему-то без этого не работает, так что Ќ≈ “–ќ√ј“№ - “”“ ћј√»я
		renderer.point(pos.x,pos.y, 0);
		renderer.end();
	}
	
	private void updatePosition(float dT){
		this.pos = pos.add(vel.scl(dT));
	}
	
	private void updateSpeed(float dT){
//		Vec imp = F.div(mass);
//		this.vel = vel.add(imp);
	}
	private void updateRotation(float dT){
		
	}
	public void update(float dT){
		updatePosition(dT);
		updateSpeed(dT);
		updateRotation(dT);
//		F = new Vec(0,0);
	}

	@Override
	public float defineSquare() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void rotate(float angle) {

	}

	@Override
	protected AABB buildAABB() {
		return null;
	}

}
