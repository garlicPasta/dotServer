package Datastructures;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import static org.junit.Assert.assertEquals;


public class RasterizationTest {

    Vector center;

    @Before
    public void setUp(){
        center = new BasicVector(new double[]{0,0,0});
    }

    @Test
    public void testAddToRaster0() throws Exception {
        Point3DRGB p= new Point3DRGB(new double[]{-4,-4,-4});
        double cellSize = 8;
        Raster r = new Raster(center, cellSize);
        r.addToRaster(p);
        assertEquals(r.getRaster().get(new BasicVector(new double[]{0,0,0})).getValue1(), new Integer(1));
    }

    @Test
    public void testAddToRaster1() throws Exception {
        Point3DRGB p = new Point3DRGB(new double[]{4,4,4});
        double cellSize = 8;
        Raster r = new Raster(center, cellSize);
        r.addToRaster(p);
        assertEquals(r.getRaster().get(new BasicVector(new double[]{Raster.RASTER_SIZE,
                Raster.RASTER_SIZE,Raster.RASTER_SIZE})).getValue1(), new Integer(1));
    }

    private void fillRaster(Raster r){
        int count = Raster.RASTER_SIZE / 2;
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

    }

    @Test
    public void testGetDownsampledRaster0() {
        center = new BasicVector(new double[]{0,0,0});
        double cellSize = 8;
        Raster r = new Raster(center, cellSize);
        fillRaster(r);
        int sum = 0;
        for (Pair<float[], Integer> p : r.getRaster().values()){
            sum += p.getValue1();
        }
        int count = Raster.RASTER_SIZE / 2;
        assertEquals(count*count*count , sum);

        sum = 0;
        for (Pair<float[], Integer> p : r.getDownSampledRaster().values()){
            sum += p.getValue1();
        }
        assertEquals(count*count*count , sum);
    }

    @Test
    public void testGetDownsampledRaster1() {
        center = new BasicVector(new double[]{0,0,0});
        int count = Raster.RASTER_SIZE / 2;
        double cellSize = 8;
        Raster r = new Raster(center, cellSize);
        fillRaster(r);
        fillRaster(r);
        int sum = 0;
        for (Pair<float[], Integer> p : r.getRaster().values()){
            sum += p.getValue1();
        }
        assertEquals(2 * count*count*count , sum);

        sum = 0;
        for (Pair<float[], Integer> p : r.getDownSampledRaster().values()){
            sum += p.getValue1();
        }
        assertEquals(2 * count*count*count , sum);
    }
}