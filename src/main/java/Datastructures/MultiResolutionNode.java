package Datastructures;

import org.la4j.Vector;

import javax.json.JsonObjectBuilder;
import java.util.Map;

public class MultiResolutionNode extends OctreeNode {

    public Rasterization rasterization;
    Point3DRGB exampleVector;

    public MultiResolutionNode() {
        super();
        rasterization = new Rasterization(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double cellLength, String id) {
        super(center, cellLength, id);
        rasterization = new Rasterization(center, cellLength);
    }

    public MultiResolutionNode(Vector center, double d) {
        super(center, d, center.toString());
        rasterization = new Rasterization(center, cellLength);
    }

    @Override
    public void insert(Point3DRGB p) {
        super.insert(p);
        if (this.isLeaf)
            return;
        rasterization.addToRaster(p);
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
