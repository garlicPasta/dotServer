package Datastructures;

import org.la4j.Vector;

public class MultiResolutionNode extends OctreeNode {

    int resolution = 128;
    Rasterization rasterization;
    Point3DRGB exampleVector;

    public MultiResolutionNode() {
        super();
        rasterization = new Rasterization(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double cellLength, String dataKey) {
        super(center, cellLength, dataKey);
        rasterization = new Rasterization(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double d) {
        super(center, d);
        rasterization = new Rasterization(center, cellLength);
    }

    @Override
    public void insert(Point3DRGB p) {
        super.insert(p);
        if (exampleVector == null){
            exampleVector = p;
            rasterization.setColors(p.color);
        }
        if (this.isLeaf)
            return;
        rasterization.addToRaster(p.position);
    }
}
