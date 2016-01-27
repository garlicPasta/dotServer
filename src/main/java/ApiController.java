import Datastructures.MultiResTree;
import Datastructures.MultiResolutionNode;
import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiController extends NanoHTTPD {

    MultiResTree mrt = new MultiResTree();
    HashMap<String[], String> map = new HashMap<>();


    public ApiController(int port) {
        super(port);
    }

    public ApiController(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        return NanoHTTPD.newFixedLengthResponse(map.get(new String[] {params.get("depth"),
                params.get("num")}));
    }

    private void fillUpMap(){
        for (int depth = 0; depth <= mrt.getDepth(); depth++) {
            Iterator<MultiResolutionNode> mrn = mrt.iterateSampleLevel(depth);
            while (mrn.hasNext()) {
                MultiResolutionNode n = mrn.next();
            }
        }

    }

}
