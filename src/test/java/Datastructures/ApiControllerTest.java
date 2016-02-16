package Datastructures;

import org.junit.Before;
import org.junit.Test;
import utils.NvmParser;

import static org.junit.Assert.*;

public class ApiControllerTest {

    MultiResTree mrt;

    @Before
    public void setUp() {
        NvmParser p = new NvmParser("/model2.nvm");
        mrt = new MultiResTree();
    }

    @Test
    public void testFillUpMap() throws Exception {
    }
}