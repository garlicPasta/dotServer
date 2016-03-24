package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Point3DRGB{
    public Vector position;
    public float[] color;

    public Point3DRGB(){
        this.position = new BasicVector(new double[3]);
        this.color = new float[3];
    }

    public Point3DRGB(Vector position, float[] color){
        this.position = position;
        this.color = color;
    }

    public Point3DRGB(double[] d){
        this(new BasicVector(d), new float[3]);
    }

    public Point3DRGB(double[] pos, float[] color){
        this(new BasicVector(pos), color);
    }

}
