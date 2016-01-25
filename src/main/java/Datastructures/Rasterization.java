package Datastructures;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Rasterization {

    Vector zeroVector;
    int rasterSize;
    double rasterStep;
    int[][][] raster;

    public Rasterization(Vector center, double cellLength){
        this.zeroVector = center.subtract(cellLength / 2);
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
}
