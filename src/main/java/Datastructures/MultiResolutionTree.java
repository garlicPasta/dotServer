package Datastructures;


import DataAccesLayer.MultiResTreeProtos;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.la4j.Vector;

import java.util.*;

public class MultiResolutionTree {

    double rootLength = 1.0d; // Length of the root octant
    int totalInserts;

    public Map<String, OctreeNode> index;
    private MultiResolutionNode root;

    public MultiResolutionNode getRoot() {
        return root;
    }

    public MultiResolutionTree() {
        super();
        this.root = new MultiResolutionNode();
        index = new HashMap<>();
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

    public MultiResTreeProtos.MRTree buildMRTProto(){
        MultiResTreeProtos.MRTree.Builder b = MultiResTreeProtos.MRTree.newBuilder();
        b.setRoot(_buildMRTProto(MultiResTreeProtos.MRTree.MRNode.newBuilder(), getRoot()));
        return b.build();
    }

    private MultiResTreeProtos.MRTree.MRNode _buildMRTProto( MultiResTreeProtos.MRTree.MRNode.Builder b,
                                                                    OctreeNode node){
        b.setId(node.id);
        b.setCellLength(node.cellLength);
        b.setPointCount(node.getSampleCount());
        b.setIsLeaf(node.isLeaf);
        b.addAllCenter(node.center);

        if (!node.isLeaf) {
            for (int i = 0; i < 8; i++){
                b.addOctant(i, _buildMRTProto(MultiResTreeProtos.MRTree.MRNode.newBuilder(), node.octants[i]));
            }
        }
        return b.build();
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

    public OctreeNode getNodeByID(String key){
        return index.get(key);
    }

    public boolean containsKey(String key){
        return index.containsKey(key);
    }
}
