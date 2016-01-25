package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public abstract class Node {
    Node[] octants;
    BasicVector center;
    double length;

    public Node(BasicVector b, double length) {
    }

    public abstract void insert(Point3DRGB p);
    public abstract void createChildren();
    public abstract BasicVector calculateEdge(int i);
    public abstract int determineOctant(Vector p);
    public abstract boolean isInBoundingBox(Vector p);
}
