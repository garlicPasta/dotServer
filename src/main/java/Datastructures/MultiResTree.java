package Datastructures;


import org.javatuples.Pair;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.*;

public class MultiResTree{

    double rootLength = 1.0d; // Length of the root octant
    int totalInserts;

    public Map<String, OctreeNode> index;
    MultiResolutionNode root;

    public MultiResolutionNode getRoot() {
        return root;
    }

    public MultiResTree() {
        super();
        this.root = new MultiResolutionNode();
        index = new HashMap<>();
        this.root.index = index;
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
        Vector offset = getOctantOffset(newIndex);
        stamp3dArray(newRoot.raster.getRaster(), root.raster.getDownSampledRaster(), offset);
        return newRoot;
    }

    @Override
    public String toString(){
        return root.toString();
    }

    public void createIndex(){
         _createIndex(root);
    }

    private void _createIndex(OctreeNode n){
        if (n == null) {
            return;
        }
        index.put(n.id, n);

        for (int i=0; i <8; i++){
            _createIndex(n.octants[i]);
        }
    }


    /**
     * @param index
     * @return
     * Returns first vector for stamping.
     */
    private Vector getOctantOffset(int index){
        int halfOffset = Raster.rasterSize / 2;
        int m = index > 3 ? halfOffset: 0;

        if (index % 4 == 0) {
            return new BasicVector(new double[]{m, 0, 0});
        } else if (index % 4 == 1) {
            return new BasicVector(new double[]{m, 0, halfOffset});
        } else if (index % 4 == 2) {
            return new BasicVector(new double[]{m, halfOffset, halfOffset});
        }
        return new BasicVector(new double[]{m, halfOffset, 0});
    }

    private void stamp3dArray(Map<Vector, Pair<int[], Integer>> base,
                              Map<Vector, Pair<int[], Integer>> pattern, Vector offset) {
        for (Map.Entry<Vector, Pair<int[], Integer>> rasterPoint : pattern.entrySet()) {
            base.put(rasterPoint.getKey().add(offset), rasterPoint.getValue());
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
            max.add(_getDepth(n.octants[i]) + 1);
        }
        return Collections.max(max);
    }

    /**
     * @param level
     * @return
     * Returns iterates over all nodes in a certain level
     */
    public Iterator<MultiResolutionNode> iterateSampleLevel(int level){
        return new Iterator<MultiResolutionNode>() {
            MultiResolutionNode currentNode;
            LinkedList<MultiResolutionNode> stack;
            LinkedList<Point3DRGB> points;
            {
                currentNode = root;
                stack = new LinkedList<>();
                fillUpStack(root, level);
            }

            private void fillUpStack(MultiResolutionNode node , int level) {
                if (node == null) {
                    return;
                }
                if (level == 0 ){
                    stack.add(node);
                    return;
                }
                for (int i = 0; i < 8 ; i++) {
                    fillUpStack((MultiResolutionNode) node.octants[i], level -1);
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public MultiResolutionNode next() {
                return stack.pop();
            }
        };
    }
}
