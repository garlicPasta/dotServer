package Datastructures;

import org.junit.Test;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class RasterizationTest {

    @Test
    public void testAddToRaster0() throws Exception {
        Vector center = new BasicVector(new double[]{0,0,0});
        Vector p= new BasicVector(new double[]{1,-4,-4});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.raster[80][0][0]==1;
    }

    @Test
    public void testAddToRaster1() throws Exception {
        Vector center = new BasicVector(new double[]{0,0,0});
        Vector p= new BasicVector(new double[]{-4,1,-4});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.raster[0][80][0]==1;
    }

    @Test
    public void testAddToRaster2() throws Exception {
        Vector center = new BasicVector(new double[]{0,0,0});
        Vector p= new BasicVector(new double[]{-4,-4,1});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.raster[0][0][80]==1;
    }

    @Test
    public void testAddToRaster3() throws Exception {
        Vector center = new BasicVector(new double[]{0,0,0});
        Vector p0= new BasicVector(new double[]{-4,-4,1});
        Vector p1= new BasicVector(new double[]{-4,-4,1});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p0);
        r.addToRaster(p1);
        assert r.raster[0][0][80]==2;
    }

    @Test
    public void testAddToRaster4() throws Exception {
        Vector center = new BasicVector(new double[]{2,0,2});
        Vector p = new BasicVector(new double[]{5,0,5});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.raster[112][64][112]==1;
    }

    @Test
    public void testPrintRaster0() throws Exception {
        Vector center = new BasicVector(new double[]{2,0,2});
        Vector p = new BasicVector(new double[]{-2,-4,-2});
        Vector c = new BasicVector(new double[]{255,255,255});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        r.setColors(c);
        assert r.raster[0][0][0]==1;
        assertEquals("-2.000000 -4.000000 -2.000000 255.000000 255.000000 255.000000 1", r.toString());
    }

    @Test
    public void testPrintRaster1() throws Exception {
        Vector center = new BasicVector(new double[]{2,0,2});
        Vector p0 = new BasicVector(new double[]{-2,-4,-2});
        Vector p1 = new BasicVector(new double[]{-1,-2,-2});
        Vector c = new BasicVector(new double[]{255,255,255});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p0);
        r.addToRaster(p1);
        r.setColors(c);
        assert r.raster[0][0][0]==1;
        String[] lines = r.toString().split("\n");
        assertEquals("-2.000000 -4.000000 -2.000000 255.000000 255.000000 255.000000 1", lines[0]);
        assertEquals("-1.000000 -2.000000 -2.000000 255.000000 255.000000 255.000000 1", lines[1]);
    }
}