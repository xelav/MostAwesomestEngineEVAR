package com.me.quadtree;

import java.util.ArrayList;
import java.util.List;

import body.AABB;
import body.Body;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.me.maee.Collision;
import com.me.maee.MAE;
import com.me.maee.Vec;

public class QuadTree {
	
	public static int i;
	//Совершенно ненужные константы
	//private static final int MAX_OBJECTS = 3;
	//private static final int MAX_LEVELS =4;
	
	 private int level;
	 public List<Body> objects;
	 private AABB bounds;
	 private QuadTree[] nodes;
	 public boolean clicked; //test only
	 //private ShapeRenderer renderer;
	 
	 public QuadTree(int level, AABB bounds) {
		 //System.out.println("---New Quad---");
		 //System.out.println("Level: "+level);
		 //bounds.write();
		 this.level = level;
		 this.bounds = bounds;
		 nodes = new QuadTree[4];
		 objects = new ArrayList();
		 //this.bounds.write();
		 
		 //renderer = new ShapeRenderer();
		 //System.out.println("---- Spliting: ----");
		 //if (level <= MAX_LEVELS-1){
			// split();
		 //}
	 }
	 
	 public void clear (){
		 objects.clear();
		 
		 for (int i = 0; i < nodes.length; i++) {
		     if (nodes[i] != null) {
		       nodes[i].clear();
		       nodes[i] = null;
		     }
		 }
	 }
	 
	 private void split (){
		 //System.out.println("Split: "+ level);
		 //bounds.write();
		 float width = bounds.width / 2;
		 float height = bounds.height / 2;
		 float x = bounds.one.x;
		 float y = bounds.one.y;
		 //System.out.println("X:"+x+"  Y:"+y);
		 //System.out.println("Width:"+width+"  Height:"+height);
		 
		 nodes[0] = new QuadTree(level+1, new AABB(x + width, y+height, width, height)); //В случае листьев - единственный испульзуемый
		 nodes[1] = new QuadTree(level+1, new AABB(x, y + height, width, height));
		 nodes[2] = new QuadTree(level+1, new AABB(x, y, width, height));
		 nodes[3] = new QuadTree(level+1, new AABB(x + width, y, width, height));
	 }
	 
	 public int getIndex (AABB body){
		 /*
		  * Выводит -1 в случае, если объект лежит на грани
		  * ! Тело обязательно должно лежать в квадрате
		  */
		 int index = -1;
		 float midX = bounds.one.x + (bounds.width / 2);
		 float midY = bounds.one.y + (bounds.height / 2);
		  
		 boolean TopQuad = (body.one.y > midY) && (body.two.y > midY);
		 boolean BotQuad = (body.one.y < midY) && (body.two.y < midY);
		 if (TopQuad || BotQuad){
			boolean LeftQuad = (body.one.x < midX) && (body.two.x < midX);
			boolean RightQuad = (body.one.x > midX) && (body.two.x > midX);
			if (LeftQuad || RightQuad){
				if (TopQuad)
					if (RightQuad) return 0;
					else return 1;
				else if (RightQuad) return 3;
					else return 2;
			}
		 } 
		 return index;
	 }
	 public int getIndex (Vec point){
		 float midX = bounds.one.x + (bounds.width / 2);
		 float midY = bounds.one.y + (bounds.height / 2);
		 boolean TopQuad = (point.y > midY);
		 boolean RightQuad = (point.x > midX);
		 if (TopQuad) {
			 if (RightQuad) return 0;
			 else return 1;
		 } else {
			 if (RightQuad) return 3;
			 else return 2;
		 }
	 }
	 /*
	  *Алгоритм вставляющий объект в наименьший узел
	  *Не требуется вводить максимальные величины - алгоритм отрегулирует это сам 
	  */
	 public boolean insert (Body body){
		 //проверка, что объект не вышел за пределы
		 //Возращает true, если вышел
		 if (body.getRect().one.x > bounds.two.x || body.getRect().two.x < bounds.one.x ) {
			return true;
		 }
		 if (body.getRect().one.y > bounds.two.y || body.getRect().two.y < bounds.one.y ) {
			return true;
		 }
		 
		 int index = getIndex (body.getRect());
		 if (index == -1){
			 objects.add(body);
		 } else {
			 if (nodes[0] != null) nodes[index].insert(body);
			 else {
				 split();
				 nodes[index].insert(body);
			 }
		 }
		 return false;
	 }
	 /*
	 private void collision(){
		 for (int i = 0; i <= MAE.quad.objects.size(); i++){
			 	//!!! Встречаются проверки с самим собой
				//проверка на столкновение в узле
				for (int j = i+1; j < MAE.quad.objects.size(); j++){
					if (Collision.detectColl(MAE.Bodies.get(i), MAE.Bodies.get(j))) {
						Collision.setColor(MAE.Bodies.get(i), MAE.Bodies.get(j),true);
					}
				}
				//проверка с узлами ниже
				if (nodes[0] != null) {
					for (int x = 0; x <= 3; x++)
						for (Body b : nodes[x].objects){
							//if (Collision.detectColl(MAE.Bodies.get(i), b)) {
								//Collision.setColor(MAE.Bodies.get(i), b,true);
							//}
						}
				}
			}
		 */
	 public void check (ArrayList<Body> obj){
		 if (nodes [0] != null){
			 obj.addAll(objects);
			 nodes [0].check(obj);
			 nodes [1].check(obj);
			 nodes [2].check(obj);
			 nodes [3].check(obj);
			 for (int i = 0; i < obj.size(); i++){
				 for (int j = 0; j < objects.size(); j++){
					 Collision.detectColl(obj.get(i), objects.get(j));
				 }
			 }
			 for (int i = 0; i <= objects.size(); i++){
				 for (int j = i+1; j < objects.size(); j++){
					 Collision.detectColl(objects.get(i), objects.get(j));
				 }
			 }
		 } else {
			 for (int i = 0; i < obj.size(); i++){
				 for (int j = 0; j < objects.size(); j++){
					 Collision.detectColl(obj.get(i), objects.get(j));
				 }
			 }
			 for (int i = 0; i <= objects.size(); i++){
				 for (int j = i+1; j < objects.size(); j++){
					 Collision.detectColl(objects.get(i), objects.get(j));
				 }
			 }
			
		 }
	 }
	public void draw() {
		if(nodes[0]!=null)
			for (int i = 0; i < 4; i++){
				nodes[i].draw();
			}
		else if (clicked){
			bounds.draw();
		} else bounds.draw();
	}
	
	public List<Body> getList () {
		//возвращает все низлежащие объекты
		List<Body> obj = new ArrayList<Body>();
		if (nodes[0] != null)
		for (int i = 0; i<=3; i++){
			obj.addAll(nodes[i].getList());
		}
		return obj;
	}
	public void write () {
		System.out.println(" "+level+": ");
		for (int i = 0; i < objects.size(); i++){
			//System.out.println(objects.get(i));
		}
		if (nodes[0] != null)
		for (int i = 0; i <= 3; i++){
			nodes[i].write();
		}
	}
	
	//TEST
	public ArrayList<Body> retrieve(Vec pointer, ArrayList<Body> obj) {
		obj.addAll(objects);
		if (nodes[getIndex(pointer)] != null){
			return nodes[getIndex(pointer)].retrieve(pointer,obj);
		} else {
			ShapeRenderer renderer = new ShapeRenderer();
			renderer.setColor(0.8f,0.3f,0.3f,1);
			renderer.begin(ShapeType.Rectangle);
			renderer.rect(bounds.one.x-1, bounds.one.y-1, bounds.width+2, bounds.height+2);
			renderer.end();
			
			return obj;
		}
		
	}
}
