package com.me.maee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import body.Body;
import body.BodyType;
import body.Circle;
import body.Shape;

public class Contact {
	
	public static ShapeRenderer contactRenderer = new ShapeRenderer();
	
	public final int type;
	public final static int CIRCLE_VS_CIRCLE = 1;
	public final static int CIRCLE_VS_SHAPE = 2;
	public final static int SHAPE_VS_SHAPE = 3;
	
	//private int i1;
	//private int i2;
	public Body b1;
	public Body b2;
	
	//required data
	
	//auxiliary data
	private Vec P1 , P2; // точка контакта
	private Vec normal; //also depth of contact
	private float depth;
	private Vec Vab; //relative speed
	float v1, v2;
	
	
	
	Contact (int type, Body b1, Body b2){
		this.b1 = b1;
		this.b2 = b2;
		this.type = type;
	}
	public void resolve (){
		
		getPoints();
		
		normal = new Vec (P1,P2);
		depth = normal.getLength();
		Vab = new Vec (b1.getVelocity().add(b2.getVelocity()));
		v1 = Utils.getProjection(b1.getVelocity(), normal).getLength();
		v2 = Utils.getProjection(b2.getVelocity(), normal).getLength();
		
		draw();
		
		int type1 = b1.type;
		int type2 = b2.type;
		if (type1 == BodyType.CIRCLE) {
			if (type2 == BodyType.CIRCLE){
				circlesIntersects();
			} else circleVsShape ((Circle)b1,(Shape)b2);
		} else  if (type2 == BodyType.CIRCLE){
			circleVsShape ((Circle)b2,(Shape)b1);
		} else shapeVsShape();
		
	}
	
	private void getPoints(){
		float dx = Math.abs(b1.getPosition().x - b2.getPosition().x);
		float dy = Math.abs(b1.getPosition().y - b2.getPosition().y);
		float L = (float) Math.sqrt(dx*dx+dy*dy);
		//float l = b1.R + b2.R - L;
		float sin = dy / L;
		float cos = dx / L;
		float minX = Math.min(b1.getPosition().x, b2.getPosition().x);
		float minY = Math.min(b1.getPosition().y, b2.getPosition().y);
		
		if (b1.getPosition().x < b2.getPosition().x){
			this.P2 = new Vec ( minX + (cos * (L - b2.R)) , minY + (sin * (L - b2.R)) );
			this.P1 = new Vec ( minX + (cos * b1.R) , minY + (sin * b1.R));
		} else {
			this.P1 = new Vec ( minX + (cos * (L - b1.R)) , minY + (sin * (L - b1.R)) );
			this.P2 = new Vec ( minX + (cos * b2.R) , minY + (sin * b2.R));
		}
		
	}
	
	private void draw(){
		//normal.write();
		contactRenderer.setColor(Color.GREEN);
		contactRenderer.begin(ShapeType.Circle);
		contactRenderer.circle(normal.x, normal.y, 5);
		contactRenderer.end();
	}
	
	private void circlesIntersects(){
		Circle c1 = (Circle)b1;
		Circle c2 = (Circle)b2;
		
		//System.out.println("---impulse---");
		
		float V = Utils.getProjection(Vab, normal).getLength();
		//System.out.println("v1: "+v1+" v2: "+v2+"  DV: "+V);
		//System.out.println("m1: "+b1.getMass()+"  m2: "+b2.getMass());
		
		//c1.applyAngularImpulse(depth);
		//c2.applyAngularImpulse(depth);
		
		//positionCorrection(Utils.getProjection(c1.getVelocity(),normal),Utils.getProjection(c2.getVelocity(),normal),normal);
		
		float p1 = b1.getMass()*v1+b2.getMass()*v2;
		//System.out.println(("p1:   "+ b1.getMass()*v1)+" + "+(b2.getMass()*v2)+" = "+p1);
		float impulse = getLinearImpulse(V, c1.getMass(), c2.getMass());
		float E1 = b1.getMass()*b1.getVelocity().x*b1.getVelocity().x + b2.getMass()*b2.getVelocity().x*b2.getVelocity().x;
		
		//c1.applySpeed(getSpeed(Utils.getProjection(Vab,normal),c1.getMass(),c2.getMass()), normal);
		
		//c1.applyImpulse(A, impulse);
		//c2.applyImpulse(A, impulse);
		
		v1 = Utils.getProjection(b1.getVelocity(), normal).getLength();
		v2 = Utils.getProjection(b2.getVelocity(), normal).getLength();
		//System.out.println("v1: "+v1+" v2: "+v2+"  DV: "+V);
		float p2 = b1.getMass()*v1+b2.getMass()*v2;
		//System.out.println(("p2:   "+ b1.getMass()*v1)+" + "+(b2.getMass()*v2)+" = "+p2);
		float E2 = b1.getMass()*b1.getVelocity().x*b1.getVelocity().x + b2.getMass()*b2.getVelocity().x*b2.getVelocity().x;
		
		//if (E1 != E2) {
			//System.out.println(E2 - E1);
		//}
		
		//System.out.println(p1+" V "+ p2);
	}
	private void circleVsShape(Circle c, Shape s){
		
		//float impulse = getLinearImpulse(normal, depth, c.getVelocity(), s.getVelocity(), c.getMass(), s.getMass());
		//c.applyImpulse(c.getPosition().x, c.getPosition().y, normal, -impulse);
		//s.applyImpulse(s.getPosition().x, s.getPosition().y, normal, impulse);
	}
	private void shapeVsShape(){
		Shape s1 = (Shape)b1;
		Shape s2 = (Shape)b2;
		
		//float impulse = getLinearImpulse(normal, depth, s1.getVelocity(), s1.getVelocity(), s1.getMass(), s2.getMass());
		//s1.applyImpulse(s1.getPosition().x, s1.getPosition().y, normal, -impulse);
		//s2.applyImpulse(s2.getPosition().x, s2.getPosition().y, normal, impulse);
	}
	
	private float getLinearImpulse(float V, float m1, float m2){
		//System.out.println("getImpulse");
		//System.out.println(V);
		
		if (V < 0 ) return 0;
		if (V == 0) return m1*m2/(m1+m2);
		float p = m1*m2/(m1+m2) * V  * 2f;
		//p = p + m1*m2/(m1+m2) * depth * 0.6f ;
		//float p = (m1*m2)/(m1+m2)*V;

		//System.out.println(((m1*m2)/(m1+m2))+" * "+V);
		//System.out.println("imp:"+ p);
		
		return p;
	}
	private float getSpeed (float V, float m1, float m2) {
		float u1 =- V*(m1-m2)/(m1+m2);
		return u1;
	}
	
	private void positionCorrection(float v1,float v2, Vec depth){
		//System.out.println("cor");
		//System.out.println(v1+" "+v2);
		if (v1 == -v2) return;
		//depth.write();
		b1.positionCorrection(depth.reverse(),v1/(v1+v2));
		b2.positionCorrection(depth,v2/(v1+v2));
	}
	
}
