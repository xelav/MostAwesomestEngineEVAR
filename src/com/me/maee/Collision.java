package com.me.maee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import body.AABB;
import body.Body;
import body.BodyType;
import body.Circle;
import body.Shape;

/*
 *   ласс провер€ющий наличие столкновений, состовл€ет списки контактов, окрашивает столкнувшиес€ тела и так далее.
 *  –азрешение контактов идет строго в класс Contact
 *  
 */


public class Collision {	
	
	// оличество проверок
	static int count;
	static int cor;
	//”пругость всех тел
	static final float Resilience = 0f;
	//Ћист контактов
	static ArrayList<Contact> contacts;
	
	//создаем список контактов
	public static void create (){
		contacts = new ArrayList<Contact>();
	}
	/*
	 * ¬торой алгоритм проверки коллизий, по идее, должен быть быстрее
	 * ќснован на квадратичном дереве
	 */
	public static void check(){
		//новый список контактов
		contacts = new <Contact>ArrayList();
		//окрашиваем все тела в белый цвет
		for (Body b : MAE.Bodies) b.collide = false;
		
		MAE.quad.check(new ArrayList<Body>());
		
		if (contacts.size() == 0) return;
		//System.out.println("---Contact---  :" + contacts.size());
		
		for (Contact c :contacts) {
			//System.out.println(c);
			//System.out.println("   "+c.b1+" , "+c.b2);
			c.resolve();
		}
	}
	
	public static boolean preDetect (Body b1, Body b2){
		if (checkRects (b1.getRect(),b2.getRect()))
			if (b1.R != 0 && b2.R != 0)
				return circlesIntersects (b1,b2);
			else {
				if (b1.type == BodyType.CIRCLE) {
					if ( b2.type == BodyType.CIRCLE)  return circlesIntersects((Circle) b1,(Circle) b2);
					else return circleVsShape((Circle)b1, (Shape)b2);
				} else { 
					if ( b2.type == BodyType.CIRCLE) return circleVsShape((Circle)b2,(Shape) b1);
					else return shapesIntersects((Shape)b2,(Shape) b1);
				}
			}
		else return false;
	}
	
	////////////
	public static boolean detectColl(Body b1, Body b2){
		count++;
		boolean coll;
		//System.out.println(b2+" "+b1);
		if (b1 == b2) {
			cor++;
			return false;
		}
		
		coll = preDetect(b1,b2);
		
		//System.out.println(b1.getPosition().x+" "+b2.getPosition().x);
		/*
		if (b1.type == BodyType.CIRCLE) {
			if ( b2.type == BodyType.CIRCLE)  coll = circlesIntersects((Circle) b1,(Circle) b2);
			else coll = circleVsShape((Circle)b1, (Shape)b2);
		} else { 
			if ( b2.type == BodyType.CIRCLE) coll = circleVsShape((Circle)b2,(Shape) b1);
			else coll = shapesIntersects((Shape)b2,(Shape) b1);
		}
		*/
		//System.out.println(b1+" "+b2);
		if (coll)
			setColor(b1,b2,true);
		return coll;
	}
	
	public static void setColor (Body b1, Body b2, boolean coll){
		if (coll){
			b1.collide = true;
			b2.collide = true;
		} else {
			b1.collide = false;
			b2.collide = false;
		}
	}

	////////////
	//Shape-Shape
	protected static boolean shapesIntersects(Shape s2, Shape s1){
		//System.out.println("--- Shapes intersection: "+s1.points.size()+" "+s2.points.size()+"---");
		for (int i = 0; i < s1.points.size()-1; i++){
			for (int j = 0; j < s2.points.size()-1; j++){
				//System.out.println(i+" "+j);
				if (Utils.linesIntersects(s1.points.get(i),s2.points.get(j),s1.points.get(i+1),s2.points.get(j+1))) return true;
			}
		}
		return false;
	}
////////////
//Circle-Shape
	protected static boolean circleVsShape(Circle c, Shape s){
		//! Ќе работает в случае когда круг внутри
		//if (!circlesIntersects(c,new Circle(s.getPosition(),s.R))) return false;
		
		for (int i = 0; i<s.points.size()-1; i++){
			if (circleVsLine(c,s.points.get(i),s.points.get(i+1))){
				Vec normal = new Vec (c.getPosition(),s.getPosition());
				//contacts.add(new Contact (normal,normal.getLength(), c, s, Contact.CIRCLE_VS_SHAPE));
				return true;
			}
		}
		if (circleVsLine(c,s.points.get(0),s.points.get(s.points.size()-1))){
			Vec normal = new Vec (c.getPosition(),s.getPosition());
			//contacts.add(new Contact (normal,normal.getLength(), c, s, Contact.CIRCLE_VS_SHAPE));
			return true;
		}
		return false;
	}
////////////
	//Circle - Line
	public static boolean circleVsLine(Circle c, Vec A, Vec B) {
		return circleVsLine(c.getPosition().x,c.getPosition().y,c.R,A,B);
	}	
	
	public static boolean circleVsLine(float x,float y, float R, Vec A, Vec B ){
		//¬се работает
		
		//проверка пересечени€ с пр€мой
		float dis = Utils.getDistance (A,B,new Vec (x,y));
		dis = Math.abs(dis);
		if (dis > R){
			return false;
		} 
		/* —мотрим, принадлежат ли концы отрезка к окружности.
		 * ≈сли только одна точка - true ; сразу две точки - true (хот€ технически false) ;
		 * Ќи одной точки - дополнительно провер€ем на принадлежность точек к одинаковым четверт€м относительно центра окружности
		 */
		boolean a = Utils.inRange(new Vec(x,y),A,R);
		boolean b = Utils.inRange(new Vec(x,y),B,R);
		if ( a || b ) return true;
		if  (Utils.between(A.x, x, B.x)) return true;
		if  (Utils.between(A.y, y, B.y)) return true;
		return false;
	}

	////////////
	private static boolean checkRects(AABB rect1, AABB rect2){
		//! This method will be called very often
		if (rect1.x + rect1.width > rect2.x)
			if (rect1.y + rect1.height > rect2.y || rect2.y + rect2.height > rect1.y) return true;
		if (rect2.x + rect1.width > rect1.x)
			if (rect2.y + rect2.height > rect1.y || rect1.y + rect1.height > rect2.y) return true;
		return false;
	}
	///////////
	//Circle-Circle
	public static boolean circlesIntersects(Body b1, Body b2){
		
		
		float dX = b1.getPosition().x - b2.getPosition().x;
		float dY = b1.getPosition().y - b2.getPosition().y;
		float dis = b1.R + b2.R;
		if (dX*dX+dY*dY>dis*dis) {
			return false;
		} else {
			if (b1.type == BodyType.CIRCLE && b2.type == BodyType.CIRCLE){
				Vec normal = new Vec(b1.getPosition(),b2.getPosition());
				//normal.write();
				//b1.applyAngularImpulse( Utils.getNormal(new Vec(normal),new Vec(b1.getVelocity()).add(b2.getVelocity())));
				//b2.applyAngularImpulse( Utils.getNormal(new Vec(normal),new Vec(b1.getVelocity()).add(b2.getVelocity())));
				
				Contact C =new Contact (Contact.CIRCLE_VS_CIRCLE, b1, b2);
				contacts.add(C);
				//contacts.add(new Contact(Utils.getUnitVector(normal), normal.getLength(), b1 , b2, Contact.CIRCLE_VS_CIRCLE));
				return true;
			}
			if (b1.type == BodyType.CIRCLE && b2.type == BodyType.SHAPE)
				return circleVsShape((Circle)b1,(Shape)b2);
			if (b2.type == BodyType.CIRCLE && b1.type == BodyType.SHAPE)
				return circleVsShape((Circle)b2,(Shape)b1);
			if (b1.type == BodyType.SHAPE && b2.type == BodyType.SHAPE)
				return shapesIntersects((Shape)b1,(Shape)b2);
			else return false;
			
		}
	}
	///////////
	public static void boundCollision (Body b){
		AABB rect = b.getRect();
		if  (rect.x < 0 || rect.x+rect.width > MAE.GLOBAL_WIDTH) {
			b.revertVelX();
		}
		if ( rect.y < 0 || rect.y + rect.height > MAE.GLOBAL_HEIGHT){
			b.revertVelY();
		}
	}
	
	
}
