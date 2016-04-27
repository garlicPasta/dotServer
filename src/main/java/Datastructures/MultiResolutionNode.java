package Datastructures;

import DataAccesLayer.RasterProtos;
import org.javatuples.Triplet;
import org.la4j.Vector;

import javax.json.JsonObjectBuilder;
import java.util.List;

public class MultiResolutionNode extends OctreeNode {

    public Raster raster;

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
        jB.add("pointCount", this.getSampleCount());
        return jB;

    }

    @Override
    public String toString() {
        return super.toString();
    }

    public RasterProtos.Raster getSampleProto(){
        if (isLeaf) {
            return buildRasterProto(this.points);
        }
        return buildRasterProto(raster);
    }


     private RasterProtos.Raster buildRasterProto(Raster r){
        RasterProtos.Raster.Builder b = RasterProtos.Raster.newBuilder();
        for (Triplet<float[], float[], Integer> t: r){
            RasterProtos.Raster.Point3DRGB.Builder bP = RasterProtos.Raster.Point3DRGB.newBuilder();
            for (int i = 0; i < 3; i++) {
                bP.addPosition( t.getValue0()[i]);
                bP.addColor(t.getValue1()[i]);
            }
            bP.setSize(t.getValue2());
            b.addSample(bP);
        }
        return b.build();
    }

     private RasterProtos.Raster buildRasterProto(List<Point3DRGB> pointList){
        RasterProtos.Raster.Builder b = RasterProtos.Raster.newBuilder();
        for (Point3DRGB p: pointList){
            RasterProtos.Raster.Point3DRGB.Builder bP = RasterProtos.Raster.Point3DRGB.newBuilder();
            for (int i = 0; i < 3; i++) {
                bP.addPosition((float) p.position.get(i));
                bP.addColor( p.color[i]);
            }
            bP.setSize(1);
            b.addSample(bP);
        }
        return b.build();
    }

    @Override
    public int getSampleCount(){
        return isLeaf? points.size() : raster.getSampleCount();
    }
}
