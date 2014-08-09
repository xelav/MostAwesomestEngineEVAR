package body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.maee.Vec;

public class AABB{
	
	public Vec one, two;
	public static ShapeRenderer  sr;
	public float width;
	public float height;
	
	
	private void constructor (Vec one, Vec two){
		this.one = one;
		this.two = two;
		
		sr.setColor(0.3f, 0.3f, 0.3f, 1);
		width = two.x-one.x;
		height = two.y-one.y;
	}
	
	AABB(Vec one, Vec two){
		constructor (one,two);
	}
	
	public AABB(float x, float y, float width, float height){
		constructor (new Vec (x,y),new Vec (x+width,y+height));
	}
	
	public AABB(Circle circle){
		constructor (new Vec (circle.getPosition().x-circle.R,circle.getPosition().y-circle.R), new Vec (circle.getPosition().x+circle.R,circle.getPosition().y+circle.R));
	}
	
	public AABB(Shape shape){
		
	}
	
	public void draw() {
		sr.begin(ShapeType.Rectangle);
		sr.rect(one.x, one.y, width, height);
		sr.end();
	}

	public void write() {
		System.out.println("AABB" );
		one.write();
		two.write();
		
	}
	public void setColorRed(){
		sr.setColor(0.7f, 0.7f, 0.7f, 1);
	}
	
}
