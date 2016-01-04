package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class MultiResolutionNode extends OctreeNode {

    int resolution = 128;
    Rasterization rasterization;
    Vector exampleVector;

    public MultiResolutionNode(BasicVector center, double cellLength, String dataKey) {
        super(center, cellLength, dataKey);
        rasterization = new Rasterization(center, cellLength);
    }

    public MultiResolutionNode(BasicVector center, double d) {
        super(center, d);
    }

    @Override
    public void insert(BasicVector p) {
        super.insert(p);
        rasterization.addToRaster(p);
    }
}
