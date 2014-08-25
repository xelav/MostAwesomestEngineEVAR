package com.me.maee;

import body.Circle;


public class Utils {
	//////////
	//зачем я написал этот метод?
	//public static Vec rotatePoint(float a, Vec position){
		//return rotatePoint(a,position, new Vec(0,0));
	//}

	public static Vec rotatePoint(float a,Vec p, Vec b){
		
		Vec rotatedPoint = rotatePoint(a,new Vec( p.x-b.x , p.y-b.y));
		return new Vec(rotatedPoint.x+b.x,rotatedPoint.y+b.y);
	}

	public static Vec rotatePoint(float a,Vec position){
		//против часовой стрелки!
		//position -  точка вокруг которой осуществляется вращение
		//a - угол в радианах
		float x2,y2;
		x2 = (float) (position.x * Math.cos(a) - position.y * Math.sin(a));
		y2 = (float) (position.x * Math.sin(a) + position.y * Math.cos(a));
		return new Vec(x2,y2);
	}
	//////////////
	//еще не тестил их, могут быть ошибки
	public static Vec getUnitVector(float angle){
		return new Vec((float)Math.cos(angle),(float)Math.sin(angle));
	}
	
	public static Vec getUnitVector(Vec V){
		float l = V.getLength(); 
		V = new Vec(V.x/l,V.y/l);
		return V;
		
	}
	
	public static float getAngleOfVector (Vec v){
		return getAngleOfVector (v.x, v.y, v.getLength());
	}
	
	private static float getAngleOfVector (float x, float y, float len){
		//+
		x = x / len;
		y = y / len;
		float a = (float) Math.acos(x);
		if (y<0) {
		a = (float) (Math.PI*2-a);
	}
	
	return a;
	}
	public static Vec getRadiusVector(float angle){
		return new Vec((float) Math.cos(angle),(float) Math.sin(angle));
	}
	////////////
	public static float getLength(Vec A, Vec B){
		float x = Math.abs(A.x-B.x);
		float y = Math.abs(A.y-B.y);
		return (float) Math.sqrt((x*x)+(y*y));
	}
	/////////////
	public static float getDistance(Vec A, Vec B){
		float dx = Math.abs(A.x-B.x);
		float dy = Math.abs(A.y-B.y);
		return (float) Math.sqrt(dx*dx+dy*dy);
	}
	/////////////
	public static float getDistance(Vec a,Vec b,Vec c)
	{	//расстояние от AB до C
		//возвращает также знак полуплоскости
		//+
		double dx = a.x-b.x;
		double dy = a.y-b.y;
		double D = dx * (c.y - a.y) - dy * (c.x-a.x); //Почему? необходимо прояснить этот момент, вывод формулы, например
		return (float) (D / getDistance(a,b));
	}
	////////////
	public static boolean inRange(Vec C,Vec A,float r){
		//+
		double dx = Math.abs(C.x-A.x);
		double dy = Math.abs(C.y-A.y);
		if (Math.sqrt(dy*dy+dx*dx) < r){
			return true;
		} else {
			return false;
		}
	}
	///////////
	public static float getPolygonArea(){
		float a = 0;
	return a;
	}
	//////////
	public static float getAngle(Vec v, Vec A){
		return Math.abs(getAngleOfVector(v)-getAngleOfVector(A));
	}
	/////////
	public static Vec getProjection(Vec v, Vec A){
		//return length of projection v on A
		if (v.getLength() == 0 || A.getLength() == 0) return new Vec();
		//if (A.x < 0) A.x = -A.x;
		float angle = getAngle(v, A);
		float l = (float) Math.cos(angle)*v.getLength();
		//return  new Vec(A).sub((float) (v.getLength()*Math.cos((double) angle)));
		//Vec.polarToCartesian(A.getAngle(), l).write();
		return Vec.polarToCartesian(A.getAngle(), l);
		
	}
	public static Vec getNormal(Vec vector, Vec point) {
		//Перпендикуляр
		//System.out.println("---");
		//if (vector.getLength() == 0) return new Vec();
		//float angle = getAngle(vector, point);
		//float l = (float) Math.sin(angle)*vector.getLength();
		//return Vec.polarToCartesian((float)Math.PI/2 - point.getAngle(), l );
		Vec proj = getProjection (vector,point);
		//return new Vec (line.A.add(linearImpulse), pointer);
		return proj;
		
	}
	//////////
	
	public static boolean pointBelongsToSection (Vec p1, Vec p2, Vec A){
		return (between(p1.x,A.x,p2.x)) && (between(p1.y,A.y,p2.y));
	}
	public static boolean between (float x1, float a, float x2){
		return ((x1<=a)&&(a<=x2)) | ((x2<=a)&&(a<=x1));
	}
	public static boolean linesIntersects(Vec a1, Vec a2, Vec b1, Vec b2){
		//Работает намного лучше
		//!  Хотя случай когда отрезки на одной прямой всегда будет выдавать false
		float disA = Utils.getDistance(a2, b2, a1);
		float disB = Utils.getDistance(a2, b2, b1);
		boolean a = (disA/Math.abs(disA) != disB/Math.abs(disB));
		
		disA = Utils.getDistance(a1, b1, a2);
		disB = Utils.getDistance(a1, b1, b2);
		boolean b = (disA/Math.abs(disA) != disB/Math.abs(disB));
		
		return (a && b);
	}
	public static Vec getIntersectionPoint (Vec a1 , Vec a2 , Vec b1 , Vec b2){
			float dY1 = b1.y - a1.y;
			float dX1 = b1.x - a1.x;
			float k1 = dY1/dX1;
			float m1 = a1.y - a1.x*k1;
			
			float dY2 = b2.y - a2.y;
			float dX2 = b2.x - a2.x;
			float k2 = dY2/dX2;
			float m2 = a2.y - a2.x*k2;
			
			float x = (m1-m2)/(k2-k1);
			float y = (k2*m1 - k1*m2)/(k2-k1);
			
			return new Vec (x,y);
	}
	public static float getPolarAngle (float x, float y){
		//return angle from -Pi/2
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
}
