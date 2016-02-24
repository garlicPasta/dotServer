package Datastructures;

import org.la4j.Vector;

import javax.json.JsonObjectBuilder;

public class MultiResolutionNode extends OctreeNode {

    public Raster raster;
    Point3DRGB exampleVector;

    public MultiResolutionNode() {
        super();
        raster = new Raster(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double cellLength, String id) {
        super(center, cellLength, id);
        raster = new Raster(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double d) {
        super(center, d, center.toString());
        raster = new Raster(center, cellLength);
    }

    @Override
    public void insert(Point3DRGB p) {
        super.insert(p);
        if (this.isLeaf)
            return;
        raster.addToRaster(p);
    }


    @Override
    public JsonObjectBuilder toJsonBuilder(){
        JsonObjectBuilder jB = super.toJsonBuilder();
        jB.add("pointCount", this.pointCount);
        return jB;

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
