package com.me.maee;

import com.badlogic.gdx.Gdx;

import body.Body;
import body.BodyType;
import body.Circle;
import body.Shape;

public class Contact {
	
	public final int type;
	public final static int CIRCLE_VS_CIRCLE = 1;
	public final static int CIRCLE_VS_SHAPE = 2;
	public final static int SHAPE_VS_SHAPE = 3;
	
	public final static float Resilience = 0.6f;
	
	//private int i1;
	//private int i2;
	public Body b1;
	public Body b2;
	
	public Vec normal;
	public float depth;
	
	Contact (Vec normal, float depth, int index1,int index2, int type){
		this.normal = normal;
		this.depth = depth;
		b1 = MAE.Bodies.get(index1);
		b2 = MAE.Bodies.get(index1);
		this.type= type;
	}
	Contact (Vec normal, float depth, Body b1, Body b2, int type){
		this.normal = normal;
		this.depth = depth;
		this.b1 = b1;
		this.b2 = b2;
		this.type = type;
	} 
	
	public void resolve (){
		int type1 = b1.type;
		int type2 = b2.type;
		if (type1 == BodyType.CIRCLE) {
			if (type2 == BodyType.CIRCLE){
				circlesIntersects();
			} else circleVsShape ((Circle)b1,(Shape)b2);
		} else  if (type2 == BodyType.CIRCLE){
			circleVsShape ((Circle)b2,(Shape)b1);
		} else shapesIntersects();
		
	}
	
	private void circlesIntersects(){
		Circle c1 = (Circle)b1;
		Circle c2 = (Circle)b2;
		
		float impulse = getImpulse(normal, depth, c1.getVelocity(), c2.getVelocity(), c1.mass, c2.mass);
		c1.applyImpulse(c1.getPosition().x, c1.getPosition().y, normal, -impulse);
		c2.applyImpulse(c2.getPosition().x, c2.getPosition().y, normal, impulse);
	}
	private void shapesIntersects(){
		
	}
	private void circleVsShape(Circle c, Shape s){
		
		float impulse = getImpulse(normal, depth, c.getVelocity(), s.getVelocity(), c.mass, s.mass);
		//c.applyImpulse(c.getPosition().x, c.getPosition().y, normal, -impulse);
		//s.applyImpulse(s.getPosition().x, s.getPosition().y, normal, impulse);
	}
	
	private static float getImpulse(Vec normal, float depth,Vec v1,Vec v2, float m1, float m2){
		float v1Pr = Utils.getProjection(v1,normal);
		float v2Pr = Utils.getProjection(v2,normal);
		float Vab = v2Pr - v1Pr;
		//Vec imp1 = v1.scl(m1).add(v2.scl(m2));
		
		//TODO: ”пругий удар
		if (Vab < 0 ) return 0;
		if (Vab == 0) return - m1*m2/(m1+m2) *depth;
		float p = m1*m2/(m1+m2) * (1+Resilience) * Vab;
		p = p + m1*m2/(m1+m2) * depth * 0.6f ;
		Vec imp2 = v1.scl(m1).add(v2.scl(m2));
		//imp1.write();
		//imp2.write();
		
		return - p;
	}
	
}
