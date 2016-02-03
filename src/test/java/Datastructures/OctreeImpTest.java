package Datastructures;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class OctreeImpTest extends TestCase{
    OctreeImp tree;

    @Before
    public void setUp() throws Exception {
        tree = new OctreeImp();
    }


    @Test
    public void testInsertToRoot() throws Exception {
        Point3DRGB p = new Point3DRGB(new double[]{-0.3, 0.1 ,-0.3});
        tree.insert(p);
        assert tree.root.isLeaf == true;
        assert tree.root.points.contains(p);
        assert tree.root.points.size() == 1;
    }

    @Test
    public void testAnsert() throws Exception {
        tree.root.createChildren();
        assert tree.root.isLeaf ==false;

        Point3DRGB p0 = new Point3DRGB(new double[]{-0.3f, 0.1f ,0.3f});
        Point3DRGB p1 = new Point3DRGB(new double[]{-0.3f, 0.1f ,-0.3f});
        Point3DRGB p2 = new Point3DRGB(new double[]{0.3f, 0.1f ,-0.3f});
        Point3DRGB p3 = new Point3DRGB(new double[]{0.3f, 0.1f ,0.5f});
        Point3DRGB p4 = new Point3DRGB(new double[]{-0.3f, -0.1f ,0.3f});
        Point3DRGB p5 = new Point3DRGB(new double[]{-0.3f, -0.1f ,-0.3f});
        Point3DRGB p6 = new Point3DRGB(new double[]{0.3f, -0.1f ,-0.3f});
        Point3DRGB p7 = new Point3DRGB(new double[]{0.3f, -0.1f ,0.5f});
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

    @Test
    public void testExtendTree0() throws Exception {
        Point3DRGB p0 = new Point3DRGB(new double[]{-0.5,0.5,0.5});
        Point3DRGB p1 = new Point3DRGB(new double[]{-1.5,0.5,0.5});
        tree.insert(p0);
        tree.insert(p1);
        assert tree.root.octants[6].points.contains(p0);
        assert tree.root.octants[0].points.contains(p1);
    }

    @Test
    public void testExtendTree1() throws Exception {
        Point3DRGB p0 = new Point3DRGB(new double[]{-0.5, 0.5, 0.5});
        Point3DRGB p1 = new Point3DRGB(new double[]{1.5, -1.5, -1.5});
        tree.insert(p0);
        tree.insert(p1);
        assert tree.root.octants[0].points.contains(p0);
        assert tree.root.octants[6].points.contains(p1);
    }

    @Test
    public void testExtendTree2() throws Exception {
        Point3DRGB p0 = new Point3DRGB(new double[]{-0.5, 0.45, 0.45});
        Point3DRGB p1 = new Point3DRGB(new double[]{4.4, -4.4, -4.4});
        tree.insert(p0);
        tree.insert(p1);
        assert tree.root.octants[0].octants[0].octants[0].points.contains(p0);
        assert tree.root.octants[6].points.contains(p1);
        assert tree.root.isLeaf == false;
    }
}