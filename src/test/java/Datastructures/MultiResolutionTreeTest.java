package Datastructures;

import org.junit.Before;
import org.junit.Test;


public class MultiResolutionTreeTest {

    MultiResolutionTree mt;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mt = new MultiResolutionTree();
    }

    @Test
    public void testExtendRoot(){
        for (double i = -0.5; i < 0.5; i+=0.0625) {
            for (double j = -0.5; j < 0.5; j+=0.0625) {
                for (double k = -0.5; k < 0.5; k += 0.0625) {
                    mt.insert(new Point3DRGB(new double[]{i, j, k}));
                }
            }
        }
        assert mt.root.raster.getSampleCount() == 16 * 16 * 16;
        mt.insert(new Point3DRGB(new double[]{1,0.5,0.5}));
        assert mt.root.raster.getSampleCount() == 16 * 16 * 16 + 1;
    }

}

