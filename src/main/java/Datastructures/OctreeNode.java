package Datastructures;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OctreeNode{

    public static int MAXPOINTS = 1000;

    public boolean isLeaf;
    public double cellLength;
    public Vector center;
    public String id;
    List<Point3DRGB> points = new LinkedList<>();
    public OctreeNode[] octants;
    Map<String, OctreeNode > index;
    public int pointCount;

    public OctreeNode(){
        this(new BasicVector(new double[3]), 1);
    }

    public OctreeNode(Vector center, double d, String id) {
        this.center = center;
        this.cellLength = d;
        this.id = id.replaceAll("\\s+","");
        this.isLeaf = true;
        octants= new OctreeNode[8];
    }

    public OctreeNode(Vector center, double d) {
        this(center, d, center.toString());
    }

    public void insert(Point3DRGB p){
        if (this.isLeaf){
            if (++pointCount > MAXPOINTS ){
                this.isLeaf = false;
                this.createChildren();
                for (Point3DRGB v : points){
                    insert(v);
                }
                points = null;
            } else {
                points.add(p);
                return;
            }
        }
        int octant_index = determineOctant(p.position);
        octants[octant_index].insert(p);
    }

    public void createChildren() {
        this.isLeaf = false;
        for (int i=0; i<8; i++){
            Vector p = calculateNodeCenter(i);
            octants[i] = new MultiResolutionNode(p, cellLength / 2);
            octants[i].index = index;
        }
    }

    /**
     * @param octant_index
     * @return BasicVector
     * Function returns the center point of a child
     */
    public Vector calculateNodeCenter(int octant_index){
        double d = cellLength / 4;
        double u=1;
        BasicVector c;
        if (octant_index > 3)
            u=-1;
        switch (octant_index % 4){
            case 0:
                c = new BasicVector(new double[]{center.get(0) - d, center.get(1) + u*d, center.get(2) + d });
                break;
            case 1:
                c = new BasicVector(new double[]{center.get(0) - d, center.get(1) + u*d, center.get(2) - d });
                break;
            case 2:
                c = new BasicVector(new double[]{center.get(0) + d, center.get(1) + u*d, center.get(2) - d });
                break;
            case 3:
                c = new BasicVector(new double[]{center.get(0) + d, center.get(1) + u*d, center.get(2) + d });
                break;
            default:
                throw new IllegalArgumentException();
        }
        return c;
    }

    /**
     * @param p
     * @return
     * Returns the octant index which contains the point p
     */
    public int determineOctant(Vector p){
        int offset=0;
        if (center.get(1) > p.get(1)){
            offset=4;
        }
        if (center.get(0) > p.get(0) &&  center.get(2) <= p.get(2))
            return 0 + offset;
        if (center.get(0) > p.get(0) &&  center.get(2) > p.get(2))
            return 1 + offset;
        if (center.get(0) <= p.get(0) &&  center.get(2) > p.get(2))
            return 2 + offset;
        if (center.get(0) <= p.get(0) &&  center.get(2) <= p.get(2))
            return 3 + offset;
        throw new IllegalArgumentException("Point is not member of the octant");
    }

    public int inverseOctant(int octant_index){
        switch(octant_index){
            case 0:
                return 6;
            case 1:
                return 7;
            case 2:
                return 4;
            case 3:
                return 5;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 0;
            case 7:
                return 1;
            default:
                throw new IllegalArgumentException("Octant indices must be between 0 and 7");
        }
    }

    /**
     * @param octantIndex
     * @return BasicVector
     *
     * Calculates edges of the current cube regarding to to octant.
     */
    public BasicVector calculateEdge(int octantIndex) {
        double d = this.cellLength / 2;

        switch (octantIndex){
            case 0:
                return (BasicVector) this.center.add(new BasicVector(new double[]{-d,d,d}));
            case 1:
                return (BasicVector) this.center.add(new BasicVector(new double[]{-d,d,-d}));
            case 2:
                return (BasicVector) this.center.add(new BasicVector(new double[]{d,d,-d}));
            case 3:
                return (BasicVector) this.center.add(new BasicVector(new double[]{d,d,d}));
            case 4:
                return (BasicVector) this.center.add(new BasicVector(new double[]{-d,-d,d}));
            case 5:
                return (BasicVector) this.center.add(new BasicVector(new double[]{-d,-d,-d}));
            case 6:
                return (BasicVector) this.center.add(new BasicVector(new double[]{d,-d,-d}));
            case 7:
                return (BasicVector) this.center.add(new BasicVector(new double[]{d,-d,d}));
            default:
                throw new IllegalArgumentException("Octant indices must be between 0 and 7");
        }
    }


    public boolean isInBoundingBox(Vector p){
        double cellLength = this.cellLength / 2;
        return  ((center.get(0) + cellLength) >= p.get(0)) && ((center.get(0) - cellLength) <=  p.get(0)) &&
                ((center.get(1) + cellLength) >= p.get(1)) && ((center.get(1) - cellLength) <=  p.get(1)) &&
                ((center.get(2) + cellLength) >= p.get(2)) && ((center.get(2) - cellLength) <=  p.get(2));
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public  JsonObject toJson(){
        return toJsonBuilder().build();
    }

    protected JsonObjectBuilder toJsonBuilder(){
        JsonObjectBuilder jB = Json.createObjectBuilder()
                .add("center", center.toString())
                .add("cellLength", String.valueOf(cellLength))
                .add("isLeaf", String.valueOf(isLeaf));
        if (!isLeaf)
                jB.add("children", childrenToJson());
        return jB;
    }

    private JsonObject childrenToJson() {
        JsonObjectBuilder jB = Json.createObjectBuilder();

        if(octants[0] == null)
            return jB.build();

        for (int i=0; i < 8 ; i++) {
            jB.add(Integer.toString(i), octants[i].id);
        }
        return jB.build();
    }
}

