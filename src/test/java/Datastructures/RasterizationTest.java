package Datastructures;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import static org.junit.Assert.assertEquals;

@Ignore
public class RasterizationTest {

    Vector center;

    @Before
    public void setUp(){
        center = new BasicVector(new double[]{0,0,0});
    }

    @Test
    public void testAddToRaster0() throws Exception {
        Point3DRGB p= new Point3DRGB(new double[]{1,-4,-4});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.getRaster().get(new BasicVector(new double[]{80,0,0})).equals(
                new Pair<double[], Integer>( new double[]{0,0,0}, new Integer(1)));
    }

    @Test
    public void testAddToRaster1() throws Exception {
        Point3DRGB p = new Point3DRGB(new double[]{-4,1,-4});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.getRaster().get(new BasicVector(new double[]{0,80,0})).equals(1);
    }

    @Test
    public void testAddToRaster2() throws Exception {
        Point3DRGB p= new Point3DRGB(new double[]{-4,-4,1});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.getRaster().get(new BasicVector(new double[]{0,0,80})).equals(1);
    }

    @Test
    public void testAddToRaster3() throws Exception {
        Point3DRGB p0= new Point3DRGB(new double[]{-4,-4,1});
        Point3DRGB p1= new Point3DRGB(new double[]{-4,-4,1});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p0);
        r.addToRaster(p1);
        assert r.getRaster().get(new BasicVector(new double[]{0,0,80})).equals(2);
    }

    @Test
    public void testAddToRaster4() throws Exception {
        center = new BasicVector(new double[]{2,0,2});
        Point3DRGB p = new Point3DRGB(new double[]{5,0,5});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.getRaster().get(new BasicVector(new double[]{112,64,112})).equals(1);
    }

    @Test
    public void testPrintRaster0() throws Exception {
        center = new BasicVector(new double[]{2,0,2});
        Point3DRGB p = new Point3DRGB(new double[]{-2,-4,-2});
        Point3DRGB c = new Point3DRGB(new double[]{255,255,255});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p);
        assert r.getRaster().get(new BasicVector(new double[]{0,0,0})).equals(1);
        assertEquals("-2.000 -4.000 -2.000 255.000 255.000 255.000 1\n", r.toString());
    }

    @Test
    public void testGetDownsampledRaster() {
        center = new BasicVector(new double[]{2,0,2});
        Vector c = new BasicVector(new double[]{255,255,255});
        int count = 25;
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.setColors(c);
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                for (int k = 0; k < count; k++) {
                    r.addToRaster(new Point3DRGB(new double[]{
                            i * r.rasterStep,
                            j * r.rasterStep,
                            k * r.rasterStep}));
                }
            }
        }
        int sum = 0;
        for (Pair<double[], Integer> p : r.getRaster().values()){
            sum += p.getValue1();
        }
        assertEquals(count*count*count , sum);

        sum = 0;
        for (Pair<double[], Integer> p : r.getDownSampledRaster().values()){
            sum += p.getValue1();
        }
        assertEquals(count*count*count , sum);
    }

    @Test
    public void testPrintRaster1() throws Exception {
        center = new BasicVector(new double[]{2,0,2});
        Point3DRGB p0 = new Point3DRGB(new double[]{-2,-4,-2});
        Point3DRGB p1 = new Point3DRGB(new double[]{-1,-2,-2});
        Vector c = new BasicVector(new double[]{255,255,255});
        double cellSize = 8;
        Rasterization r = new Rasterization(center, cellSize);
        r.addToRaster(p0);
        r.addToRaster(p1);
        r.setColors(c);
        assert r.getRaster().get(new BasicVector(new double[]{0,0,0})).equals(1);
        String[] lines = r.toString().split("\n");
        assertEquals("-2.000 -4.000 -2.000 255.000 255.000 255.000 1", lines[0]);
        assertEquals("-1.000 -2.000 -2.000 255.000 255.000 255.000 1", lines[1]);
    }
}