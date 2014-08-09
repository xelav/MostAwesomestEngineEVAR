package body;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.maee.Stats;
import com.me.maee.Utils;
import com.me.maee.Vec;

public class Shape extends Body {

	public ArrayList<Vec> points = new ArrayList<Vec>();
	
	public Shape(ArrayList<Vec> points, Vec pos) {
		
		this.points = points;
		this.pos = new Vec(pos.x,pos.y);
		setRenderer(); 
		setVelocity(new Vec());;
		defineCircle();
		aabb = buildAABB();
		type = BodyType.SHAPE;
		
	}

	private void defineCircle() {
		//определение описывающего круга
	}

	int n;
	
	public boolean isOriented(ArrayList<Vec> p){
		return false;
	}

	@Override
	public void draw() {
		//System.out.println("=====");
		aabb.draw();
		renderer.begin(ShapeType.Line);
		Vec p1;
		Vec p2;
		for (int i = 0 ; i < points.size()-1 ; i++ ){
			p1 = points.get(i);
			p2 = points.get(i+1);
			//System.out.println("P1: "+p1.x+" "+p1.y+" ; P2:"+p2.x+" "+p2.y );
			renderer.line((float)p1.x, (float)p1.y, (float)p2.x, (float)p2.y);
		}
		p1 = points.get(points.size()-1);
		p2 = points.get(0);
		renderer.line((float)p1.x,(float) p1.y,(float)  p2.x,(float)  p2.y);
		renderer.end();
	}
	
	@Override
	public void update(float deltaTime) {
		//pos.write();
		
		updateRotation(deltaTime);
		updatePosition(deltaTime);
		setColor();
		aabb = buildAABB();
	}
	
	private void updateRotation(float dTime){
		
	}
	
	private void updatePosition(float dTime){
		pos.add(vel);
		for (Vec i : points){
			i.add(vel);
		}
	}

	@Override
	public float defineSquare() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	public void rotate(float angle){
		
		for (int i = 0 ; i < points.size() ; i++ ){
			Vec newDot = Utils.rotatePoint(angle, points.get(i) ,pos);
			points.get(i).x = newDot.x;
			points.get(i).y = newDot.y;
		}
		
		/*for (Vec i : points){
			System.out.println("=======");
			System.out.println(""+ i.x+ " "+ i.y);
			i = Utils.rotatePoint(angle, i ,pos);
			System.out.println(""+ i.x+ " "+ i.y);
		}*/
	}

	@Override
	public AABB buildAABB() {
		//! учитываю ориентированость формы, должен быть алгоритм быстрее, хотя сложность та же
		Vec a = points.get(0);
		float left = a.x;
		float right = a.x;
		float up = a.y;
		float down = a.y;
		for (Vec i: points){
			if (left > i.x) left = i.x;
			if (right < i.x) right = i.x;
			if (up < i.y) up = i.y;
			if (down > i.y) down = i.y;
		}
		return new AABB(new Vec (left,down), new Vec (right,up));
	}
	
}
