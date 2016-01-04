package Datastructures;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Rasterization {

    Vector zeroVector;
    int rasterSize;
    double rasterStep;
    int[][][] raster;

    public Rasterization(Vector center, double cellLength){
        this.zeroVector = center.subtract(cellLength);
        this.rasterSize = 128;
        this.rasterStep = cellLength / 128;
        this.raster = new int[rasterSize][rasterSize][rasterSize];
    }

    public void addToRaster(Vector p){
        Vector n = p.subtract(this.zeroVector);
        int x = (int) (n.get(0) / this.rasterStep);
        int y = (int) (n.get(1) / this.rasterStep);
        int z = (int) (n.get(2) / this.rasterStep);
        raster[x][y][z] += 1;
    }


}
