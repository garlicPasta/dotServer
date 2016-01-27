package Datastructures;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.Formatter;

public class Rasterization {

    Vector zeroVector;
    Vector colors;
    public static final int rasterSize = 128;
    double rasterStep;
    double cellLength;
    int[][][] raster;

    public Rasterization(Vector center, double cellLength){
        this.zeroVector = center.subtract(cellLength / 2);
        this.rasterStep = cellLength / 128;
        this.cellLength = cellLength;
        this.raster = new int[rasterSize][rasterSize][rasterSize];
    }

    public void addToRaster(Vector p){
        Vector n = p.subtract(this.zeroVector);
        int x = (int) (n.get(0) / this.rasterStep);
        int y = (int) (n.get(1) / this.rasterStep);
        int z = (int) (n.get(2) / this.rasterStep);
        raster[x][y][z] += 1;
    }

    public int[][][] getDownSampledRaster() {
        int newRasterSize = rasterSize / 2;
        int[][][] downsampledRaster = new int[newRasterSize][newRasterSize][newRasterSize];
        for (int i = 0; i < newRasterSize ; i++) {
            for (int j = 0; j < newRasterSize ; j++) {
                for (int k = 0; k < newRasterSize ; k++) {
                    downsampledRaster[i][j][k] = getSumOfSuccesors(2 * i, 2 * j , 2 * k);
                }
            }
        }
        return downsampledRaster;
    }

    private int getSumOfSuccesors(int i, int j, int k) {
        int sum = 0;
        for (int l = 0; l < 2; l++) {
            for (int m = 0; m < 2; m++) {
                for (int n = 0; n < 2; n++) {
                    sum += raster[i + l][j + m][k + n];
                }
            }
        }
        return sum;
    }

    public int[][][] getRaster() {
        return raster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter f = new Formatter(sb);
        double x = zeroVector.get(0);
        double y = zeroVector.get(1);
        double z = zeroVector.get(2);

        for (int i = 0; i < rasterSize ; i++) {
            for (int j = 0; j < rasterSize; j++) {
                for (int k = 0; k < rasterSize; k++) {
                    if (raster[i][j][k] > 0 ){
                        f.format("%f %f %f %f %f %f %d",
                                x+i*rasterStep, y+j*rasterStep, z+k*rasterStep,
                                colors.get(0), colors.get(1), colors.get(2),
                                raster[i][j][k]);
                        sb.append('\n');
                    }
                }
            }
        }
        return f.toString();
    }

    public Vector getColors() {
        return colors;
    }

    public void setColors(Vector colors) {
        this.colors = colors;
    }
}
