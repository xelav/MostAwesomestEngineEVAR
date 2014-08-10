package com.me.maee;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import body.AABB;
import body.Body;
import body.BodyType;
import body.Circle;
import body.Line;
import body.Point;
import body.Shape;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Rectangle;
import com.me.quadtree.QuadTree;

public class MAE implements ApplicationListener {

	public static final int GLOBAL_WIDTH = 800;
	public static final int GLOBAL_HEIGHT = 800;
	public static final int LIMIT_FPS = 60;
	public static final int OBJECTS = 1000;
	public static final float MAX_RADIUS = 20;
	public static final float MIN_RADIUS = 10;
	public static final float MAX_VELOCITY = 10;
	public static final float MAX_WIDTH = 50;
	public static final float MAX_HEIGHT = 50;
	public static final int MAX_DOTS = 8;
	
	public static ArrayList<Body> Bodies = new ArrayList<Body>();
	public static java.util.Iterator<Body> it;
	ShapeRenderer shapeRenderer;
	public static QuadTree quad;
	
	public Circle c1,c2;
	Point point;
	Vec pointer;
	
	FPSLogger fps = new FPSLogger();
	
	Line line, line2;
	public void create() {
		
		line = new Line ();
		
		Gdx.graphics.setDisplayMode(GLOBAL_WIDTH, GLOBAL_HEIGHT, false);
		shapeRenderer = new ShapeRenderer();
		AABB.sr = new ShapeRenderer();
		Body.renderer = new ShapeRenderer();
		
		createScene();
		
		for (Body b : Bodies){
			//System.out.println(b);
		}
		Collision.create();
		
		quad = new QuadTree(1, new AABB (0,0,GLOBAL_WIDTH,GLOBAL_HEIGHT));
	}

	private void createScene(){
		//TODO: Добавляем необходимые тела
		/*
		c1 = new Circle(200, 200, 20);
		c2 = new Circle(300, 200, 50);
		
		Bodies.add(c1);
		Bodies.add(c2);
		ArrayList<Vec> dots = new ArrayList<Vec>();
		dots.add(new Vec(250,250));
		dots.add(new Vec(250,350));
		dots.add(new Vec(350,350));
		dots.add(new Vec(350,250));
		Bodies.add(new Shape(dots, new Vec(305,305)));
		*/
	
		for (int i = 1; i <= OBJECTS/2; i++) {
			Bodies.add(new Shape());
		}
		for (int i = 1; i <= OBJECTS/2; i++){
			float x,y,r,vx,vy;
			Random rand = new Random();
			x = rand.nextFloat() * MAE.GLOBAL_WIDTH;
			y = rand.nextFloat() * MAE.GLOBAL_HEIGHT;
			r = rand.nextFloat() * (MAX_RADIUS-MIN_RADIUS)+MIN_RADIUS;
			if (rand.nextInt(2) == 1) vx = rand.nextFloat() * MAX_VELOCITY;
			else vx = - rand.nextFloat() * MAX_VELOCITY;
			if (rand.nextInt(2) == 1) vy = rand.nextFloat() * MAX_VELOCITY;
			else vy = - rand.nextFloat() * MAX_VELOCITY;
			Bodies.add(new Circle(x,y,r,new Vec (vx,vy)));
		}
	}
	
	@Override
	public void dispose() {
		
	}
	@Override
	public void pause() {
		
	}
	
	@Override
	public void render() {
		
		try {
			Thread.sleep((long)(1000/LIMIT_FPS-Gdx.graphics.getDeltaTime()));
		} catch (InterruptedException e) {}
		
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		it = Bodies.iterator();
		input();
		update();
		draw();	
		
	}
	
	Vec A;
	boolean Pressed = false ;
	Body Dragged;
	
	private void input() {
		
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if (Pressed){
				if (Dragged != null){
					line.A = Dragged.getPosition(); // привязка к окружности
					line.B = pointer;
					line.draw();
					Dragged.applyImpulse(Dragged.getPosition().x, Dragged.getPosition().y, new Vec(line.getdX(),line.getdY()) , (float) 5);
				}
			} else { 
				Pressed = true;
				A = pointer;
				line = new Line(A,pointer);
				for (Body b : quad.retrieve(A, new ArrayList<Body>())){
					//if (b.type = BodyType.SHAPE) break;
					//Circle c = (Circle)b; // пускай так
					if  (Utils.inRange(b.getPosition(), A, b.R)){
						Dragged = b;
						break;
					}
				}
		} 
			// TEST
			for (Body b : quad.retrieve(pointer,new ArrayList<Body>())){
				ShapeRenderer sr = new ShapeRenderer();
				sr.begin(ShapeType.Circle);
				sr.setColor(0.8f, 0.3f, 0.3f, 1);
				sr.circle(b.getPosition().x, b.getPosition().y, b.R);
				sr.end();
				}
			
		} else {
			Pressed = false;
			Dragged = null;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			c1.applyImpulse(c1.getPosition().x, c1.getPosition().y, new Vec(1,0), 10000);
		} 
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			c1.applyImpulse(c1.getPosition().x, c1.getPosition().y, new Vec(-1,0), 10000);
		}
	}
	
	private void update() {
		System.out.println("===========");
		Collision.count = 0;
		Collision.cor = 0;
		
		float dTime = Gdx.graphics.getDeltaTime();
		for(Body b: Bodies){
			b.update(dTime);
		}
		
		pointer = new Vec(Gdx.input.getX(),GLOBAL_HEIGHT - Gdx.input.getY());
		
		quad.clear();
		quad = new QuadTree(1,new AABB (0,0,GLOBAL_WIDTH,GLOBAL_HEIGHT));
		Iterator<Body> it = Bodies.iterator();
		while (it.hasNext()){
			Body b = it.next();
			if (quad.insert(b) && b != Dragged)
				it.remove();
		}
		
		Collision.check();
		System.out.println("Objects: "+Bodies.size());
		System.out.println("count: "+Collision.count + " - "+Collision.cor);
		//quad.write();
	}
	
	private void draw() {
		
		fps.log();
		Iterator<Body> it = Bodies.iterator();
		while (it.hasNext()){
			it.next().draw();
		}
		quad.draw();
		//quad.getIndex(c1);
		
		Stats.write(new float[]{pointer.x,pointer.y}, 7);
	}
	
	public void resize(int arg0, int arg1) {
		
	}

	public void resume() {
		
	}

}
