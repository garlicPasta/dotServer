package Datastructures;

import org.junit.Test;
import org.la4j.vector.dense.BasicVector;

public class OctreeTest {

    @Test
    public void testInsertToRoot() throws Exception {
        Octree tree = new Octree();
        BasicVector p = new BasicVector(new double[]{-0.3, 0.1 ,-0.3});
        tree.insert(p);
        assert tree.root.isLeaf == true;
        assert tree.root.points.contains(p);
        assert tree.root.pointCount == 1;
    }

    @Test
    public void testInsert() throws Exception {
        Octree tree = new Octree();
        tree.root.createChildren();
        assert tree.root.isLeaf ==false;

        BasicVector p0 = new BasicVector(new double[]{-0.3f, 0.1f ,0.3f});
        BasicVector p1 = new BasicVector(new double[]{-0.3f, 0.1f ,-0.3f});
        BasicVector p2 = new BasicVector(new double[]{0.3f, 0.1f ,-0.3f});
        BasicVector p3 = new BasicVector(new double[]{0.3f, 0.1f ,0.7f});
        BasicVector p4 = new BasicVector(new double[]{-0.3f, -0.1f ,0.3f});
        BasicVector p5 = new BasicVector(new double[]{-0.3f, -0.1f ,-0.3f});
        BasicVector p6 = new BasicVector(new double[]{0.3f, -0.1f ,-0.3f});
        BasicVector p7 = new BasicVector(new double[]{0.3f, -0.1f ,0.7f});
        tree.insert(p0);
        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);
        tree.insert(p5);
        tree.insert(p6);
        tree.insert(p7);
        assert tree.root.octants[0].points.contains(p0);
        assert tree.root.octants[1].points.contains(p1);
        assert tree.root.octants[2].points.contains(p2);
        assert tree.root.octants[3].points.contains(p3);
        assert tree.root.octants[4].points.contains(p4);
        assert tree.root.octants[5].points.contains(p5);
        assert tree.root.octants[6].points.contains(p6);
        assert tree.root.octants[7].points.contains(p7);
    }
}