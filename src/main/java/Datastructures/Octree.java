package Datastructures;

import org.la4j.vector.dense.BasicVector;

public class Octree {

    static double rootLength = 1.0d; // Shortest distance from zeroVector to border

    OctreeNode root;

    Octree(){
        root = new OctreeNode(new BasicVector(new double[]{0, 0,0}), rootLength, "0.0");
    }

    public void insert(BasicVector p){
        if (root.isInBoundingBox(p)){
            root.insert(p);
            return;
        }
        root = createBiggerRoot(p);
        insert(p);
    }

    private OctreeNode createBiggerRoot(BasicVector p) {
        this.rootLength *= 2;
        BasicVector newCenter = root.calculateEdge(root.determineOctant(p));
        OctreeNode newRoot = new OctreeNode(newCenter, this.rootLength);
        newRoot.createChildren();
        int newIndex = newRoot.determineOctant(root.center);
        newRoot.octants[newIndex] = root;
        return newRoot;
    }
}
