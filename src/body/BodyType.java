package body;

public class BodyType {

	public final static int CIRCLE = 0;
	public final static int SHAPE = 1;
	
	public static void write(int type){
		if (type == 0)
			System.out.println("Circle");
		else
			System.out.println("Shape");
	}
	
}
