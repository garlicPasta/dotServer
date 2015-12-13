package Datastructures;

public class Point3D {
    float[] cords;

    public Point3D(float[] cords) {
        if (cords.length!=3){
            throw new IllegalArgumentException("Invalid array length/Dimension");
        }
        this.cords = cords;
    }

    public float getX(){
        return cords[0];
    }

    public float getY(){
        return cords[1];
    }

    public float getZ(){
        return cords[2];
    }

    public String toString(){
        return "(" + cords[0] + ","+ cords[1] + ","+ cords[2] + ")";
    }
}