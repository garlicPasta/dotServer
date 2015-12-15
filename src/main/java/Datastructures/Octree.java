package Datastructures;

import org.la4j.vector.dense.BasicVector;

import java.util.LinkedList;
import java.util.List;

public class Octree {

    static double dist= 1.0d; // Shortest distance from center to border
    OctreeNode root;

    Octree(){
        root = new OctreeNode(new BasicVector(new double[]{0, 0,0}), "0.0");
    }

    public void insert(BasicVector p){
        OctreeNode currentNode = root;

        if (root.isInBoundingBox(p)){
            root.insert(p);
            return;
        }
        root = extendTree(p);
    }

    private OctreeNode extendTree(BasicVector p) {
        this.dist *= 2;
        int new_index = root.inverseOctant(root.determineOctant(p));
        BasicVector newCenter = new BasicVector(new double[]{0, 0, 0});
        OctreeNode newRoot = new OctreeNode(newCenter);
        return null;
    }

    public class OctreeNode{

        boolean isLeaf;
        String dataKey;
        BasicVector center;
        int pointCount;
        List<BasicVector> points = new LinkedList<>();
        OctreeNode[] octants;

        public OctreeNode(BasicVector center, String dataKey) {
            this.center = center;
            this.dataKey = dataKey;
            this.pointCount = 0;
            this.isLeaf = true;
            octants= new OctreeNode[8];
        }

        public OctreeNode(BasicVector center) {
            this(center, center.toString());
        }

        public void insert(BasicVector p){
            if (this.isLeaf){
                pointCount++;
                addPointToDataBase(p);
                points.add(p);
                return;
            }
            int octant_index = determineOctant(p);
            octants[octant_index].insert(p);
        }

        public void createChildren(){
            for (int i=0; i<8; i++){
                BasicVector p = calculateNodeCenter(i);
                octants[i] = new OctreeNode(p, p.toString());
            }
            this.isLeaf = false;
        }

        public BasicVector calculateNodeCenter(int octant_index){
            double d = dist / 2;
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
                    c = new BasicVector(new double[]{center.get(0) + d, center.get(1) + u*d, center.get(2) + d });
                    break;
                case 3:
                    c = new BasicVector(new double[]{center.get(0) - d, center.get(1) + u*d, center.get(2) + d });
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
        private int determineOctant(BasicVector p){
            int offset=0;
            if (center.get(1) > p.get(1)){
                offset=4;
            }
            if (center.get(0) > p.get(0) &&  center.get(2) < p.get(2))
                return 0 + offset;
            if (center.get(0) > p.get(0) &&  center.get(2) > p.get(2))
                return 1 + offset;
            if (center.get(0) < p.get(0) &&  center.get(2) > p.get(2))
                return 2 + offset;
            if (center.get(0) < p.get(0) &&  center.get(2) < p.get(2))
                return 3 + offset;
            throw new IllegalArgumentException("Point is not member of the octant");
        }

        public void addPointToDataBase(BasicVector p){}

        private int inverseOctant(int octant_index){
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
                    throw new IllegalArgumentException("Octant indcies must be between 0 and 7");
            }
        }

        public Point3D calculateClosestEdge(Point3D p) {
            Point3D[] cornerVertices = new Point3D[8];
            return null;
        }

        public boolean isInBoundingBox(BasicVector p){
            return  ((center.get(0) + dist) > p.get(0)) && ((center.get(0) - dist) <  p.get(0)) &&
                    ((center.get(1) + dist) > p.get(1)) && ((center.get(1) - dist) <  p.get(1)) &&
                    ((center.get(2) + dist) > p.get(2)) && ((center.get(2) - dist) <  p.get(2));
        }
    }
}
