package Datastructures;

import java.util.LinkedList;
import java.util.List;

public class Octree {

    static int dist= 1;
    OctreeNode root;

    Octree(){
        root = new OctreeNode(new Point3D(new float[]{0f, 0f,0f}), "0.0");
    }

    public void insert(Point3D p){
        OctreeNode currentNode = root;

        if (root.isInBoundingBox(p)){
            root.insert(p);
        }
    }

    public class OctreeNode{

        boolean isLeaf;
        String dataKey;
        Point3D center;
        int pointCount;
        List<Point3D> points;
        OctreeNode[] octants;

        public OctreeNode(Point3D center, String dataKey) {
            this.center = center;
            this.dataKey = dataKey;
            this.pointCount = 0;
            this.isLeaf = true;
            this.points = new LinkedList<>();
            octants= new OctreeNode[8];
        }

        public void insert(Point3D p){
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
                Point3D p = calculateNodeCenter(i);
                octants[i] = new OctreeNode(p, p.toString());
            }
            this.isLeaf = false;
        }

        public Point3D calculateNodeCenter(int octant_index){
            float d = dist / 2;
            float u=1;
            Point3D c;
            if (octant_index > 3)
                u=-1;

            switch (octant_index % 4){
                case 0:
                    c = new Point3D(new float[]{center.getX() - d, center.getY() + u*d, center.getZ() + d });
                    break;
                case 1:
                    c = new Point3D(new float[]{center.getX() - d, center.getY() + u*d, center.getZ() - d });
                    break;
                case 2:
                    c = new Point3D(new float[]{center.getX() + d, center.getY() + u*d, center.getZ() + d });
                    break;
                case 3:
                    c = new Point3D(new float[]{center.getX() - d, center.getY() + u*d, center.getZ() + d });
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return c;
        }

        private int determineOctant(Point3D p){
            int offset=0;
            if (center.getY() > p.getY()){
                offset=4;
            }
            if (center.getX() > p.getX() &&  center.getZ() < p.getZ())
                return 0 + offset;
            if (center.getX() > p.getX() &&  center.getZ() > p.getZ())
                return 1 + offset;
            if (center.getX() < p.getX() &&  center.getZ() > p.getZ())
                return 2 + offset;
            if (center.getX() < p.getX() &&  center.getZ() < p.getZ())
                return 3 + offset;
            throw new IllegalArgumentException("Point is not member of the octant");
        }

        public void addPointToDataBase(Point3D p){}

        public boolean isInBoundingBox(Point3D p){
            return  (((center.getX() + dist) > p.getX()) && ((center.getX() - dist) <  p.getX())) &&
                    (((center.getY() + dist) > p.getY()) && ((center.getY() - dist) <  p.getY())) &&
                    (((center.getZ() + dist) > p.getZ()) && ((center.getZ() - dist) <  p.getZ()));
        }
    }
}
