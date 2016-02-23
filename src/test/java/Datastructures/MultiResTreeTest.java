package Datastructures;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.InexactComparisonCriteria;
import utils.NvmParser;

import java.util.Iterator;

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
    public void testInsertRoot(){
        Integer sum = 0;
        for (Pair<double[], Integer> p : mt.root.rasterization.getRaster().values())
            sum += p.getValue1();
        assertEquals( new Integer(mt.totalInserts), sum);
    }
}