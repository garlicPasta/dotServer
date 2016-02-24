package DataAccesLayer;

import Datastructures.MultiResTree;
import Datastructures.OctreeNode;
import fi.iki.elonen.NanoHTTPD;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiController extends NanoHTTPD {

    final static int PAGE_SIZE = 1000;

    MultiResTree mrt = new MultiResTree();
    HashMap<String, String> map = new HashMap<>();

    public ApiController(int port, MultiResTree mrt) throws IOException {
        super(port);
        this.mrt = mrt;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browers to http://localhost:8080/ \n Keep going Jakob!");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        switch (params.get("mode")){
            case "tree":
                byte[] protobuf = buildMRTProto(mrt).toByteArray();
                return  NanoHTTPD.newFixedLengthResponse(Response.Status.OK,"application/octet-stream",
                        new ByteArrayInputStream(protobuf), protobuf.length);
            case "samples":
                String key =  params.get("depth") + " " + params.get("node") + " " + params.get("page");
                if (map.containsKey(key)){
                    return NanoHTTPD.newFixedLengthResponse(map.get(key));
                }
                return NanoHTTPD.newFixedLengthResponse("Invalid Parameters \n depth:" + params.get("depth")
                        + "\n node:" + params.get("node"));
            default:
                return NanoHTTPD.newFixedLengthResponse("Invalid Mode");
        }
    }

    static private MultiResTreeProtos.MRTree buildMRTProto(MultiResTree mrt){
        MultiResTreeProtos.MRTree.Builder b = MultiResTreeProtos.MRTree.newBuilder();
        b.setRoot(_buildMRTProto(MultiResTreeProtos.MRTree.MRNode.newBuilder(), mrt.getRoot()));
        return b.build();
    }

    static private MultiResTreeProtos.MRTree.MRNode _buildMRTProto( MultiResTreeProtos.MRTree.MRNode.Builder b,
                                                                    OctreeNode node){
        b.setId(node.id);
        b.setCellLength(node.cellLength);
        b.setPointCount(node.pointCount);
        b.setIsLeaf(node.isLeaf);
        b.addAllCenter(node.center);

        if (!node.isLeaf) {
            for (int i = 0; i < 8; i++){
                b.addOctant(i, _buildMRTProto(MultiResTreeProtos.MRTree.MRNode.newBuilder(), node.octants[i]));
            }
        }
        return b.build();
    }


}