import Datastructures.MultiResTree;
import Datastructures.MultiResolutionNode;
import Datastructures.Point3DRGB;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiController extends NanoHTTPD {

    MultiResTree mrt = new MultiResTree();
    HashMap<String[], String> map = new HashMap<>();


    public ApiController(int port, MultiResTree mrt) throws IOException {
        super(port);
        this.mrt = mrt;
        fillUpMap();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browers to http://localhost:8080/ \n");
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
            int page = 0;
            while (mrn.hasNext()) {
                MultiResolutionNode n = mrn.next();
                map.put(new String[]{Integer.toString(depth), Integer.toString(page)}, n.rasterization.toString());
                page++;
            }
        }

    }
}
