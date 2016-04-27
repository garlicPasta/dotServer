package Datastructures;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Vector;
import utils.NvmParser;

import java.util.List;

import static org.junit.Assert.*;

public class MultiResolutionTreeNVMTest {

    MultiResolutionTree mt;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mt = new MultiResolutionTree();
        NvmParser parser = new NvmParser("/model2.nvm");
        for (Point3DRGB p : parser ){
            mt.insert(p);
        }
    }

    @Test
    public void testInsertRoot0(){
        Integer sum = 0;
        for (Pair<float[], Integer> p : mt.root.raster.getRaster().values())
            sum += p.getValue1();
        assertEquals( new Integer(mt.totalInserts), sum);
    }

    @Test
    public void testOctantcontainsPoints(){
        _checkNodeCondition(mt.root);
    }

    private void _checkNodeCondition(MultiResolutionNode node){
        if (node == null)
            return;

        for (Triplet<Vector,float[], Integer> sample: node.raster.iterateAbsolutSample())
            assertTrue(node.isInBoundingBox(sample.getValue0()));
        for (OctreeNode n : node.octants)
            _checkNodeCondition((MultiResolutionNode) n);
    }

    @Test
    public void testInsertRaster(){
        assertTrue(mt.root.raster.rasterHash.keySet().size() < Math.pow(Raster.RASTER_SIZE, 3));
    }

    @Test
    public void testLeafPointCoint(){
        List<OctreeNode> list = mt.getAllLeafs();
        int sum = 0;
        for (OctreeNode node : list)
            sum+= node.points.size();
        assertEquals(34653, sum);
    }
}