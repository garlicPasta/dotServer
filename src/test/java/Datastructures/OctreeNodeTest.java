package Datastructures;

import org.junit.Test;
import org.la4j.vector.dense.BasicVector;

public class OctreeNodeTest {

    @Test
    public void testCalculateNodeCenter() throws Exception {

    }

    @Test
    public void testDetermineOctant() throws Exception {
        OctreeNode node = new OctreeNode(new BasicVector(new double[]{1,1,1}),4);
        node.createChildren();
        BasicVector[] vectors = new BasicVector[]{
                new BasicVector(new double[]{0,2,2}),
                new BasicVector(new double[]{0,2,0}),
                new BasicVector(new double[]{2,2,0}),
                new BasicVector(new double[]{2,2,2}),
                new BasicVector(new double[]{0,0,2}),
                new BasicVector(new double[]{0,0,0}),
                new BasicVector(new double[]{2,0,0}),
                new BasicVector(new double[]{2,0,2}),
        };
        for (int i=0; i< vectors.length; i++){
            assert node.determineOctant(vectors[i]) == i;
        }
    }

    @Test
    public void testCalculateClosestEdge() throws Exception {
    }
}