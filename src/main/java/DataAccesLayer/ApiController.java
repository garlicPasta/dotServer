package DataAccesLayer;

import Datastructures.*;
import fi.iki.elonen.NanoHTTPD;
import utils.CompressionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class ApiController extends NanoHTTPD {

    MultiResolutionTree mrt = new MultiResolutionTree();

    public ApiController(int port, MultiResolutionTree mrt) throws IOException {
        super(port);
        this.mrt = mrt;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browers to http://localhost:8080/");
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        switch (params.get("mode")){
            case "tree":
                byte[] protobuf = mrt.buildMRTProto().toByteArray();
                return  NanoHTTPD.newFixedLengthResponse(Response.Status.OK,"application/octet-stream",
                        new ByteArrayInputStream(protobuf), protobuf.length);

            case "samples":
                String key =  params.get("id");
                if (mrt.containsKey(key)){
                    //return NanoHTTPD.newFixedLengthResponse(map.get(key).toString());
                    MultiResolutionNode n = (MultiResolutionNode) mrt.getNodeByID(key);
                    byte[] protobufRaster = n.getSampleProto().toByteArray();
                    byte[] compressed = null;
                    try {
                        compressed = CompressionUtils.compress(protobufRaster);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return  NanoHTTPD.newFixedLengthResponse(Response.Status.OK,"application/octet-stream",
                            new ByteArrayInputStream(compressed), compressed.length);
                }
                return NanoHTTPD.newFixedLengthResponse("Invalid Parameters \n depth:" + params.get("depth")
                        + "\n node:" + params.get("node"));
            default:
                return NanoHTTPD.newFixedLengthResponse("Invalid Mode");
        }
    }

}
