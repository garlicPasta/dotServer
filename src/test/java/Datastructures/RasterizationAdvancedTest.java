package Datastructures;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.Map;

import static org.junit.Assert.assertEquals;


public class RasterizationAdvancedTest {

    Vector center;
    Raster raster;

    @Before
    public void setUp(){
        center = new BasicVector(new double[]{16,16,16});
        raster = new Raster(center, 32);
        for (double i = 0; i < 32; i+=2) {
            for (double j = 0; j < 32; j+=2) {
                for (double k = 0; k < 32; k += 2) {
                    raster.addToRaster(new Point3DRGB(new double[]{i, j, k}));
                }
            }
        }
    }

    @Test
    public void testDownSampled() throws Exception {
        Map<Vector, Pair<float[], Integer>> downSampled = raster.getDownSampledRaster();
        assert downSampled.size() == raster.getSampleCount();
        for (double i = 0; i < 16; i++) {
            for (double j = 0; j < 16; j++) {
                for (double k = 0; k < 16; k++) {
                    assert downSampled.containsKey(new BasicVector(new double[]{i,j,k}));
                }
            }
        }
    }
}