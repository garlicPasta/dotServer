package Datastructures;

import org.la4j.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class OctreeImp {

    double rootLength = 1.0d; // Shortest distance from zeroVector to border
    OctreeNode root;
    int totalInserts;

    public OctreeImp(){
        root = new OctreeNode();
    }

    public void insert(Point3DRGB p) throws InstantiationException, IllegalAccessException {
        if (root.isInBoundingBox(p.position)){
            root.insert(p);
            totalInserts++;
            return;
        }
        root = createBiggerRoot(p);
        insert(p);
    }

    private OctreeNode createBiggerRoot(Point3DRGB p) {
        this.rootLength *= 2;
        Vector newCenter = root.calculateEdge(root.determineOctant(p.position));
        OctreeNode newRoot = new OctreeNode(newCenter, this.rootLength);
        newRoot.createChildren();

        int newIndex = newRoot.determineOctant(root.center);
        newRoot.octants[newIndex] = root;
        return newRoot;
    }

    public Integer getDepth(){
        return _getDepth(root);
    }

    private Integer _getDepth(OctreeNode n){
        if (n == null) {
            return 0;
        }
        ArrayList<Integer> max = new ArrayList<>();

        for (int i=0; i <8; i++){
            max.add(_getDepth(n.octants[0]) + 1);
        }
        return Collections.max(max);
    }

}
