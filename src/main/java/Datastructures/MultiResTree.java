package Datastructures;


import org.la4j.Vector;
import java.util.ArrayList;
import java.util.Collections;

public class MultiResTree extends OctreeImp {

    MultiResolutionNode root;

    public MultiResTree() {
        super();
        this.root = new MultiResolutionNode();
    }

    public void insert(Point3DRGB p) {
        if (root.isInBoundingBox(p.position)){
            root.insert(p);
            totalInserts++;
            return;
        }
        root = createBiggerRoot(p);
        insert(p);
    }

    private MultiResolutionNode createBiggerRoot(Point3DRGB p) {
        this.rootLength *= 2;
        Vector newCenter = root.calculateEdge(root.determineOctant(p.position));
        MultiResolutionNode newRoot = new MultiResolutionNode(newCenter, this.rootLength);
        newRoot.createChildren();

        int newIndex = newRoot.determineOctant(root.center);
        newRoot.octants[newIndex] = root;
        int[] offset = getOctantOffset(newIndex);
        stamp3dArray(newRoot.rasterization.getRaster(), root.rasterization.getDownSampledRaster(), offset);
        return newRoot;
    }

    private int[] getOctantOffset(int index){
        int halfOffset = root.rasterization.rasterSize / 2;
        int m = index > 3 ? halfOffset: 0;

        if (index % 4 == 0) {
            return new int[]{m, 0, 0};
        } else if (index % 4 == 1) {
            return new int[]{m, 0, halfOffset};
        } else if (index % 4 == 2) {
            return new int[]{m, halfOffset, halfOffset};
        }
        return new int[]{m, halfOffset, 0};
    }

    private void stamp3dArray(int[][][] base, int[][][] pattern, int []offset) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                for (int k = 0; k < pattern.length; k++) {
                    base[ i + offset[0] ][ j + offset[1] ][ k + offset[2] ] = pattern[i][j][k];
                }
            }
        }

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
