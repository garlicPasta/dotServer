package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Point3DRGB{
    Vector position;
    Vector color;

    public Point3DRGB(){
        this.position = new BasicVector(new double[3]);
        this.color = new BasicVector(new double[3]);
    }

    public Point3DRGB(Vector position, Vector color){
        this.position = position;
        this.color = color;
    }

    public Point3DRGB(double[] d){
        this(new BasicVector(d), new BasicVector());
    }

    public Point3DRGB(double[] pos, double[] color){
        this(new BasicVector(pos), new BasicVector(color));
    }

}
