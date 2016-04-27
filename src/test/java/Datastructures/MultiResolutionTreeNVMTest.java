package Datastructures;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Vector;
import utils.NvmParser;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

public class MultiResolutionTreeNVMTest {

    MultiResolutionTree mt;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        mt = new MultiResolutionTree();
        File f = null;
        try {
            f = new File(this.getClass().getClassLoader().getResource("model2.nvm").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        NvmParser parser = new NvmParser(f);
        for (Point3DRGB p : parser ){
            mt.insert(p);
        }
    }

    @Test
    public void testInsertRoot0(){
        Integer sum = 0;
        for (Pair<float[], Integer> p : mt.getRoot().raster.getRaster().values())
            sum += p.getValue1();
        assertEquals( new Integer(mt.totalInserts), sum);
    }

    @Test
    public void testOctantcontainsPoints(){
        _checkNodeCondition(mt.getRoot());
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
        assertTrue(mt.getRoot().raster.rasterHash.keySet().size() < Math.pow(Raster.RASTER_SIZE, 3));
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