package Datastructures;

import org.junit.Test;

public class OctreeTest {

    @Test
    public void testInsertToRoot() throws Exception {
        Octree tree = new Octree();
        Point3D p = new Point3D(new float[]{-0.3f, 0.1f ,-0.3f});
        tree.insert(p);
        assert tree.root.isLeaf == true;
        assert tree.root.points.contains(p);
    }

    @Test
    public void testInsert() throws Exception {
        Octree tree = new Octree();
        tree.root.createChildren();
        assert tree.root.isLeaf ==false;

        Point3D p0 = new Point3D(new float[]{-0.3f, 0.1f ,0.3f});
        Point3D p1 = new Point3D(new float[]{-0.3f, 0.1f ,-0.3f});
        Point3D p2 = new Point3D(new float[]{0.3f, 0.1f ,-0.3f});
        Point3D p3 = new Point3D(new float[]{0.3f, 0.1f ,0.7f});
        Point3D p4 = new Point3D(new float[]{-0.3f, -0.1f ,0.3f});
        Point3D p5 = new Point3D(new float[]{-0.3f, -0.1f ,-0.3f});
        Point3D p6 = new Point3D(new float[]{0.3f, -0.1f ,-0.3f});
        Point3D p7 = new Point3D(new float[]{0.3f, -0.1f ,0.7f});
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