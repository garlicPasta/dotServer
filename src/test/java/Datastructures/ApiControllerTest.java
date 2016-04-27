package Datastructures;

import org.junit.Before;
import org.junit.Test;
import utils.NvmParser;

public class ApiControllerTest {

    MultiResolutionTree mrt;

    @Before
    public void setUp() {
        NvmParser p = new NvmParser("/model2.nvm");
        mrt = new MultiResolutionTree();
    }

    @Test
    public void testFillUpMap() throws Exception {
    }
}