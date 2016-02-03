package Datastructures;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.*;

public class MultiResTree{

    double rootLength = 1.0d; // Shortest distance from zeroVector to border
    int totalInserts;

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
        Vector offset = getOctantOffset(newIndex);
        stamp3dArray(newRoot.rasterization.getRaster(), root.rasterization.getDownSampledRaster(), offset);
        return newRoot;
    }

    /**
     * @param index
     * @return
     * Returns first vector for stamping.
     */
    private Vector getOctantOffset(int index){
        int halfOffset = Rasterization.rasterSize / 2;
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

    private void stamp3dArray(Map<Vector, Integer> base, Map<Vector, Integer> pattern, Vector offset) {
        for (Map.Entry<Vector, Integer> rasterPoint : pattern.entrySet()) {
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
            max.add(_getDepth(n.octants[0]) + 1);
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
