package Datastructures;


import org.javatuples.Pair;
import org.javatuples.Triplet;
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

        if (root.isLeaf){
            for (Point3DRGB point : root.points){
                newRoot.raster.addToRaster(point);
            }
        }else{
            for ( Triplet<Vector,float[], Integer> e : root.raster.iterateAbsolutSample())
                newRoot.raster.addToRaster(e.getValue0(), new Pair<>(e.getValue1(), e.getValue2()));
        }
        newRoot.octants[newIndex] = root;
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
    public Iterator<MultiResolutionNode> iterateSampleLevel(final int level){
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

    public List<OctreeNode> getAllLeafs(){
        List<OctreeNode> nodes = new LinkedList<>();
        _getAllLeafs(root, nodes);
        return nodes;
    }

    private void _getAllLeafs(OctreeNode currentNode, List<OctreeNode> nodes) {
        if (currentNode.isLeaf){
            nodes.add(currentNode);
            return;
        }
        for (OctreeNode child: currentNode.octants){
            _getAllLeafs(child, nodes);
        }
    }

    public List<String> getLeafsIDs(){
        List<String> list = new LinkedList<>();
        _getChildrenIDs(root, list);
        return list;
    }

    public  void _getChildrenIDs(OctreeNode node, List<String> list){
        if (node.isLeaf){
            list.add(node.id);
            return;
        }
        for (OctreeNode n : node.octants){
            _getChildrenIDs(n, list);
        }
    }
}
