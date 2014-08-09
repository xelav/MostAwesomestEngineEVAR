package com.me.maee;

public class Vec {
	public float x,y;
	
	public Vec(){
		x = 0;
		y = 0;
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
		x += v.x;
		y += v.y;
		return this;
	}
	public Vec add (float x, float y){
		x += x;
		y += y;
		return this;
	}
	
	public Vec scl (float a){
		return new Vec(x*a,y*a);
	}
	
	public Vec div (float a){
		return new Vec(x/a,y/a);
	}
	
	public static Vec getUnitVector(float angle){
		return new Vec((float) Math.cos(angle),(float) Math.sin(angle));
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
	
}
