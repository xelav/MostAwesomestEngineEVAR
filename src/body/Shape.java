package body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.maee.MAE;
import com.me.maee.Stats;
import com.me.maee.Utils;
import com.me.maee.Vec;

public class Shape extends Body {

	public ArrayList<Vec> points = new ArrayList<Vec>();
	private ShapeRenderer CircleRenderer;
	
	private void constructor(ArrayList<Vec> points, Vec pos,Vec vel, float mass, Vec angle ){
		this.renderer = new ShapeRenderer();
		this.vel = vel;
		this.pos = pos;
		this.points = points;
		this.mass = mass;
		
		type = BodyType.SHAPE;
		aabb = buildAABB();
		
		if (R != 0) {
			CircleRenderer = new ShapeRenderer();
			CircleRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
		}
		posRenderer = new ShapeRenderer();
		
	}
	
	
	public Shape(ArrayList<Vec> points, Vec pos, float mass) {
		constructor(points,pos,new Vec(),mass,new Vec());
	}
	public Shape(ArrayList<Vec> points, Vec pos, float mass, float angle){
		constructor (points,pos, new Vec(), mass, Utils.getRadiusVector(angle));
	}
	public Shape (ArrayList<Vec> points, Vec pos, Vec vel, float mass, float angle){
		constructor (points,pos, vel, mass, Utils.getRadiusVector(angle));
	}
	public Shape (ArrayList<Vec> points, Vec pos, Vec vel, float mass){
		constructor (points,pos, vel, mass, new Vec());
	}
	
	public Shape (){
		//generate(); // Algorhytm, generating ugly, angular shapes
		generate2();
	}

	private void defineCircle() {
		//define circumscribed circle
		//Why? In hail of Satan of course!
	}

	int n;
	
	public boolean isOriented(ArrayList<Vec> p){
		return false;
	}

	@Override
	public void draw() {
		//System.out.println("=====");
		aabb.draw();
		setColor();
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
		if (CircleRenderer != null) {
			CircleRenderer.begin(ShapeType.Circle);
			CircleRenderer.circle(pos.x, pos.y, R);
			CircleRenderer.end();
		}
		posRenderer.begin(ShapeType.Point);
		posRenderer.setColor(0, 1, 0, 1);;
		posRenderer.point(pos.x, pos.y, 0);
		posRenderer.end();
	}
	
	@Override
	public void update(float deltaTime) {
		updateRotation(deltaTime);
		updatePosition(deltaTime);
		aabb = buildAABB();
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
		//! учитывая ориентированость формы, должен быть алгоритм быстрее, хотя сложность та же
		//System.out.println(points);
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
	/*
	private void generate(){
		//System.out.println("Generating new shape");
		Random rand = new Random();
		
		float vx,vy;
		if (rand.nextInt(2) == 1) vx = rand.nextFloat() * MAE.MAX_VELOCITY;
		else vx = - rand.nextFloat() * MAE.MAX_VELOCITY;
		if (rand.nextInt(2) == 1) vy = rand.nextFloat() * MAE.MAX_VELOCITY;
		else vy = - rand.nextFloat() * MAE.MAX_VELOCITY;
		
		float square = 0;
		Vec pos = new Vec (rand.nextFloat()*MAE.GLOBAL_WIDTH,rand.nextFloat()*MAE.GLOBAL_HEIGHT);
		R = rand.nextFloat() * (MAE.MAX_RADIUS-MAE.MIN_RADIUS)+MAE.MIN_RADIUS;
		float angle = (float) (rand.nextFloat()*Math.PI*R*2);
		int amount = rand.nextInt(MAE.MAX_DOTS-2) + 4 ;
		//System.out.println(amount);
		ArrayList<Vec> dots = new ArrayList<Vec>();
		////
		//генерируем верхнюю половину
		dots.add(new Vec(pos.x-R,pos.y)); // первая точка, обязательно лежащая на конце диаметра
		
		int N = rand.nextInt(amount - 2);
		//System.out.println(" "+amount+" = "+N+" + "+(amount-N-2));
		
		float curH = 0;
		float prevH = 0;
		float[] K = splitLine(N,R+R);
		for (int i = 0; i < N; i++){
			//System.out.println(" "+K[i]);
			//D = D - D*a;
			float maxH =(float)Math.sin( (Math.acos( K[i]/R-1 )))*R;
			//System.out.println(maxH);
			curH = rand.nextFloat()*maxH;
			
			dots.add(new Vec (pos.x+(K[i]-R),pos.y+curH ));
			//System.out.println("square:"+square);
			if (i != 0)
				square = square +  ((prevH+curH)*(K[i]-K[i-1])); // площадь новой трапеции
			else square = square + ((prevH+curH)*(K[0])*R); 
			prevH = curH;
		}
		////
		//Теперь нижняя половина
		dots.add(new Vec(pos.x+R,pos.y));// вторая точка, лежащая на другом конце диаметра
		square = square; //TODO: еще нужно учесть площадь последнего треугольника
		K = splitLine(amount - N -2,R+R);
		for (int i = 0; i <amount - N-2; i++){
			//System.out.println(" "+K[i]);
			//D = D - D*a;
			float maxH =(float)Math.sin( (Math.acos( K[i]/R-1 )))*R;
			//System.out.println(maxH);
			curH = rand.nextFloat()*maxH;
			
			dots.add(new Vec (pos.x-(K[i]-R),pos.y-curH ));
		
			if (i != 0)
				square = square +  ((prevH+curH)*(K[i]-K[i-1])); // площадь новой трапеции
			else square = square + ((prevH+curH)*(K[0])*R); 
			prevH = curH;
			//System.out.println("square:"+square);
		}
		////
		this.pos = pos;
		this.angle = Utils.getRadiusVector(angle);
		rotate(angle);
		this.points = dots;
		aabb = buildAABB();
		type = BodyType.SHAPE;
		setRenderer();
		posRenderer = new ShapeRenderer();
		//System.out.println("square:"+square);
		mass = square;
		//System.out.println("Final square:"+square);
		CircleRenderer= new ShapeRenderer();
		CircleRenderer.setColor(0.3f, 0.3f, 0.3f, 1);
	}

	private static float[] splitLine (int N,float L){
		//разбиение отрезка на N случайных частей
		Random rand = new Random();
		float[] M = new float[N];
		float[] K = new float[N];
		float s = 0;
		for (int i = 0; i < N; i++) {
			M[i] = rand.nextFloat();
			s += M[i]; 
		}
		float a = 0;
		for (int i = 0; i < N; i++) {
			//K[i] = M[i]/s*L; // возвращаются длины отрезков
			a = a + M[i]/s*L;
			K[i] = a; // так возращаются положения всех точек относительно "нулевой" 
		}
		return K;
	}
	*/

	private void generate2(){
		System.out.println("Generating new shape...");
		Random rand = new Random();
		
		float vx,vy;
		vx = rand.nextFloat() * (MAE.MAX_VELOCITY+MAE.MAX_VELOCITY)-MAE.MAX_VELOCITY;
		vy = rand.nextFloat() * (MAE.MAX_VELOCITY+MAE.MAX_VELOCITY)-MAE.MAX_VELOCITY;

		float square = 0;
		Vec pos = new Vec (rand.nextFloat()*MAE.GLOBAL_WIDTH,rand.nextFloat()*MAE.GLOBAL_HEIGHT);
		R = rand.nextFloat() * (MAE.MAX_RADIUS-MAE.MIN_RADIUS)+MAE.MIN_RADIUS;
		float angle = (float) (rand.nextFloat()*Math.PI*R*2);
		//int amount = MAE.MAX_DOTS;
		int amount = rand.nextInt((MAE.MAX_DOTS-3)) + 3;
		
		//System.out.println("Amount:"+ amount);
		//System.out.println("Radius:"+R);
		//dots - primal list of points in the circle ; points - treated list of points that will be in shape
		ArrayList<Vec> dots = new ArrayList<Vec>();
		ArrayList<Vec> points = new ArrayList<Vec>();
		//These points necessarily must be in the shape; these points lies on the ends of diameter of bounding circle;
		points.add(new Vec(pos.x - R, pos.y));
		dots.add(new Vec(pos.x + R, pos.y));
		for (int i = 0; i < amount-2; i++){
			float X = rand.nextFloat()*(R+R)-R;
			float maxY = (float) (Math.sqrt(R*R-X*X));
			float Y = rand.nextFloat()*(maxY+maxY)-maxY;
			Vec dot = new Vec(X+pos.x,  Y+pos.y);
			//dot.write();
			dots.add(dot);
		}
		
		int i = 0,j = 0;
		float prevMin = -1;
		while (true) {
			if (dots.size() == 0) break;
			Vec p = points.get(i);
			float min = 10000;
			for (int d = 0; d < dots.size(); d++){
				float a = getPolarAngle (dots.get(d).x-p.x,dots.get(d).y-p.y);
				//System.out.println( ": "+a);
				if (min > a) {
					min = a;
					j = d;
				}
			};
			//System.out.println("final dot - "+j+" ,"+min);
			if (prevMin> min) break;
			
			//площадь трапеции
			float s = ( Math.abs((dots.get(j).y-pos.y)) + Math.abs((points.get(i).y-pos.y)) ) * Math.abs(  dots.get(j).x-points.get(i).x )/2;
			square += s;
			//System.out.println(square);
			
			points.add(dots.get(j));
			dots.remove(j);
			
			i++;
			prevMin = min;
		}
		
		constructor(points,pos,new Vec(vx,vy),square,Utils.getRadiusVector(angle));
		
		rotate(angle);
	}
	/*
	public static float getPolarAngle (float x, float y){
		float PI = (float) Math.PI;
		if (x == 0){
			if (y == 0) return 0;
			else if (y > 0) return (PI/2);
			else return (PI*3/2);
		}
		float arc = (float)Math.atan(y/x);
		if (x < 0) return (arc+PI);
		else if (y<0) return (arc+PI+PI);
		else return (arc);
	}
	*/
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
	private void drawPoints (ArrayList<Vec> points){
		ShapeRenderer sr = new ShapeRenderer(); // memory leak
		sr.begin(ShapeType.Point);
		sr.setColor(1, 0, 0, 1);
		for (Vec p : points){
			sr.point(p.x+5, p.y+5, 0);
		}
		sr.end();
	}

}
	