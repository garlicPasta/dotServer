package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Point3DRGB{
    Vector position;
    int[] color;

    public Point3DRGB(){
        this.position = new BasicVector(new double[3]);
        this.color = new int[3];
    }

    public Point3DRGB(Vector position, int[] color){
        this.position = position;
        this.color = color;
    }

    public Point3DRGB(double[] d){
        this(new BasicVector(d), new int[3]);
    }

    public Point3DRGB(double[] pos, int[] color){
        this(new BasicVector(pos), color);
    }

}
