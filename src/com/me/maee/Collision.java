package com.me.maee;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;

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
	static HashSet<Contact> contacts;
	
	//создаем список контактов
	public static void create (){
		contacts = new HashSet<Contact>();
	}
	
	
	// ћетод, взаимодействующий с другими классами
	// ѕросто вызываем проверочный detectColl и смотрим список контактов
	/*
	public static void check(){
		contacts = new HashSet<Contact>();
		
		//состовл€ем список контактов
		//ѕо идее, нужно провер€ть только еще нестолкнувшиес€ тела
		//System.out.println("------ ");
		for (int i = 0; i < MAE.Bodies.size()-1; i++)
			for (int j = i+1; j < MAE.Bodies.size(); j++){
				if (detectColl(i,j)){
					
				}
			}
		//ќбновл€ем статусы объектов
		for (Body b : MAE.Bodies) b.collide = false;
		for (Contact c : contacts){
			
			
			
			//System.out.println(""+c.ind1()+" "+c.ind2()+" , "+c.depth);
			MAE.Bodies.get(c.ind1()).collide = true;
			MAE.Bodies.get(c.ind2()).collide = true;
			
			//–ешаем контакты
			c.resolve();
		}
	}
	*/
	/*
	 * ¬торой алгоритм проверки коллизий, по идее, должен быть быстрее
	 * ќснован на квадратичном дереве
	 */
	public static void check(){
		//новый список контактов
		contacts = new <Contact>HashSet();
		//окрашиваем все тела в белый цвет
		for (Body b : MAE.Bodies) b.collide = false;
		
		MAE.quad.check(new ArrayList<Body>());
		
		for (Contact c :contacts) c.resolve();
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
		//System.out.println(b1.getPosition().x+" "+b2.getPosition().x);
		if (b1.type == BodyType.CIRCLE) {
			if ( b2.type == BodyType.CIRCLE)  coll = circlesIntersects((Circle) b1,(Circle) b2);
			else coll = circleVsShape((Circle)b1, (Shape)b2);
		} else { 
			if ( b2.type == BodyType.CIRCLE) coll = circleVsShape((Circle)b2,(Shape) b1);
			else coll = shapesIntersects((Shape)b2,(Shape) b1);
		}
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

		for (int i = 0; i <= s1.points.size()-1; i++){
			for (int j = 0; i <= s2.points.size()-1; j++){
				if (linesIntersects(s1.points.get(i),s2.points.get(j),s1.points.get(i+1),s2.points.get(j+1))) return true;
			}
		}
		return false;
	}
////////////
//Circle-Shape
	protected static boolean circleVsShape(Circle c, Shape s){
		//! Ќе работает в случае когда круг внутри
		
		for (int i = 0; i<s.points.size()-1; i++){
			if (circleVsLine(c,s.points.get(i),s.points.get(i+1))){
				Vec normal = new Vec (c.getPosition(),s.getPosition());
				contacts.add(new Contact (normal,normal.getLength(), c, s, Contact.CIRCLE_VS_SHAPE));
				return true;
			}
		}
		if (circleVsLine(c,s.points.get(0),s.points.get(s.points.size()-1))){
			Vec normal = new Vec (c.getPosition(),s.getPosition());
			contacts.add(new Contact (normal,normal.getLength(), c, s, Contact.CIRCLE_VS_SHAPE));
			return true;
		}
		return false;
	}
////////////
	public static Vec getNormal(Vec a, Vec b){
		//получение нормали дл€ случа€ столкновени€ окружностей
		return new Vec(a.x-b.x,a.y-b.y);
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
		/*//слишком сложно
		 * //находим точки пересечени€ и определ€ем принадлежность их к отрезку
		float L = Utils.getLength(A,B);
		float Xv = (B.x-A.x)/L;
		float Yv = (B.y-A.y)/L;
		float Xd = A.x-x;
		float Yd = A.y-y;
		float a = (Xd*Xd+Yd*Yd);
		float b = (2*Xd*Yd*A.y)-(2*A.x*Yd*Yd);
		float c = (2*Xd*Yd*A.y*A.x)+(2*A.y*A.y*Xd*Xd)-(Xd*Xd*R*R);
		float D = b*b - (4*a*c);
		if (D > 0) {
			return true;
		} else{
			return false;
		}
		*/
	}

	////////////
	protected static boolean AABBvsAABB(Vec oneA, Vec oneB, Vec twoA, Vec twoB ){
		if ((oneA.x < twoB.x) && (oneB.y>oneA.y)) {return true;}
		return false;
	}
	///////////
	//Circle-Circle
	public static boolean circlesIntersects(Circle c1, Circle c2){
		float dX = c1.getPosition().x - c2.getPosition().x;
		float dY = c1.getPosition().y - c2.getPosition().y;
		float dis = c1.R + c2.R;
		//System.out.println(c2);
		//c2.getPosition().write();
		//System.out.println(""+dX+" "+dY);
		//System.out.println(" "+(dis*dis)+" , "+(dX*dX+dY*dY));
		if (dX*dX+dY*dY>dis*dis) {
			return false;
		} else {
			Vec normal = getNormal(c1.getPosition(),c2.getPosition());
			//normal.write();
			contacts.add(new Contact(Utils.getUnitVector(normal), normal.getLength(), c1 , c2, Contact.CIRCLE_VS_CIRCLE));
			return true;
		}
	}
//////////
	/*public static boolean linesIntersects(Vec a1, Vec a2, Vec b1, Vec b2){
		// проверка скрещивани€ линий AB1 и AB2
		// Ќаходим общую точку, смотрим принадлежит ли она данным отрезкам. ќднако проблематично вычисл€ть общую точку
		
		float dY1 = b1.y - a1.y;
		float dX1 = b1.x - a1.x;
		float k1 = dY1/dX1;
		float m1 = a1.y - a1.x*k1;
		
		float dY2 = b2.y - a2.y;
		float dX2 = b2.x - a2.x;
		float k2 = dY2/dX2;
		float m2 = a2.y - a2.x*k2;
		
		if (k1 != k2 && m1 != m2){
			
			float x = (m1-m2)/(k2-k1);
			float y = (k2*m1 - k1*m2)/(k2-k1);
			Vec A = new Vec (x,y);
			boolean ab1 = Utils.pointBelongsToSection(a1, b1, A);
			boolean ab2 = Utils.pointBelongsToSection(a2, b2, A);
			Stats.write(ab1 , ab2 );
			return(ab1 && ab2);
			
		} else {
			System.out.println("--------");
			System.out.println(""+k1+" "+k2);
			return false;
		}
		
	}*/
	public static boolean linesIntersects(Vec a1, Vec a2, Vec b1, Vec b2){
		//–аботает намного лучше
		//!  ’от€ случай когда отрезки на одной пр€мой всегда будет выдавать false
		float disA = Utils.getDistance(a2, b2, a1);
		float disB = Utils.getDistance(a2, b2, b1);
		boolean a = (disA/Math.abs(disA) != disB/Math.abs(disB));
		
		disA = Utils.getDistance(a1, b1, a2);
		disB = Utils.getDistance(a1, b1, b2);
		boolean b = (disA/Math.abs(disA) != disB/Math.abs(disB));
		
		return (a && b);
	}
	public static Vec getIntersectionPoint (Vec a1 , Vec a2 , Vec b1 , Vec b2){
			//Ѕлизко, но неправильно. ѕолучаетс€ точка пересечени€ с линией с отклоением.
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
}
