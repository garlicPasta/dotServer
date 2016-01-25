package Datastructures;

import org.junit.Before;
import org.junit.Test;
import utils.NvmParser;

import static org.junit.Assert.*;

public class MultiResTreeTest {

    MultiResTree mt;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mt = new MultiResTree();
        NvmParser parser = new NvmParser("/model2.nvm");
        for (Point3DRGB p : parser ){
            mt.insert(p);
        }
    }

    @Test
    public void testInsert(){
        int sum = 0;
        int [][][] raster = mt.root.rasterization.raster;
        for (int i = 0; i < raster.length ; i++) {
            for (int j = 0; j < raster.length ; j++) {
                for (int k = 0; k < raster.length ; k++) {
                    sum += raster[i][j][k];
                }
            }
        }
        assertEquals( mt.totalInserts , sum);
    }
}