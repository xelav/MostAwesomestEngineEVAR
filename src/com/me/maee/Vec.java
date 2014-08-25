package com.me.maee;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Vec {
	public float x,y;
	private static ShapeRenderer renderer;
	
	public Vec(){
		x = 0;
		y = 0;
	}
	public Vec (double x, double y){
		this.x = (float) x;
		this.y = (float) y;
	}
	
	public Vec (Vec v1, Vec v2){
		x = v1.x-v2.x;
		y = v1.y-v2.y;
	}
	
	public Vec(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vec(Vec v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vec add (Vec v){
		return new Vec(this.x + v.x, this.y + v.y);
	}
	public Vec add (float x, float y){
		return new Vec(this.x + x, this.y + y);
	}
	
	public Vec scl (float a){
		if ( a > 0)
		 return new Vec(x*a,y*a);
		else return new Vec (-x*a,y*a);
	}
	
	public Vec div (float a){
		if ( a > 0)
			return new Vec(x/a,y/a);
		else return new Vec (-x/a,y/a);
	}
	
	public static float getLength(Vec v){
		float x = Math.abs(v.x);
		float y = Math.abs(v.y);
		return (float) Math.sqrt((x*x)+(y*y));
	}
	public float getLength (){
		float x = Math.abs(this.x);
		float y = Math.abs(this.y);
		return (float) Math.sqrt((x*x)+(y*y));
	}
	public void write(){
		String s = new String("X: "+x+" ; Y: "+y);
		System.out.println(s);
	}
	public void rotate(float angle){
		//Вращает радиус-вектор на угол angle в радианах
		Utils.rotatePoint(angle, new Vec(0,0));
	}
	public Vec reverse(){
		return new Vec (-x,-y);
	}
	////////
	public static void draw (Vec A, Vec B){
		renderer = new ShapeRenderer();
		renderer.setColor(1,1,1,1f);
		renderer.begin(ShapeType.Line);
		renderer.line(A.x, A.y, B.x, B.y);
		renderer.end();
	}
	public void draw (Vec a){
		renderer = new ShapeRenderer();
		renderer.setColor(1,1,1,1f);
		renderer.begin(ShapeType.Line);
		renderer.line(a.x, a.y, x+a.x, y+a.y);
		renderer.end();
	}
	//////
	public Vec add (float l){
		//increasing length of this vector
		l += getLength();
		float a = getAngle();
		if (a>=Math.PI/2) a -= Math.PI/2; 
		float x = (float)Math.cos(a)*l;
		float y = (float)Math.sin(a)*l;
		
		if (this.x > 0) this.x = x;
		else this.x = -x;
		if (this.y > 0)this.y = y;
		else this.y = -y;
		
		return this;
	}
	public Vec sub (float l){
		//System.out.println("---");
		//decreasing length of this vector
		//!!! L < currentLength
		l -= getLength();
		float a = getAngle();
		//if (a>=Math.PI/2) a -= Math.PI/2; 
		float x = (float)Math.cos(a)*l;
		float y = (float)Math.sin(a)*l;
		if (this.x > 0) this.x = x;
		else this.x = -x;
		if (this.y > 0)this.y = y;
		else this.y = -y;
		
		return this;
	}
	////////
	public float getAngle() {
		//return trigonometric angle from 0 to 2PI
		Vec a = Utils.getUnitVector(this);
		if (y < 0) 
			return  (float) (Math.acos(a.x) + Math.PI);
		else
			return  (float) Math.acos(a.x);
	}
	public static Vec polarToCartesian(float a, float r){
		return new Vec (r*Math.cos(a),r*Math.sin(a));
	}
	public float getPolarAngle (){
		//!!! return angle from -Pi/2
		//Need only for shape generator method
		float PI = (float) Math.PI;
		if (x == 0){
			if (y == 0) return 0;// but this is an error;
			else if (y > 0) return (PI);
			else return 0;
		}
		float arc = (float)Math.atan(y/x);
		if (x<0) return (arc+PI*3/2);
		else if (y < 0) return (arc+PI/2);
		else return (arc+PI/2);
	}
	////////
}
