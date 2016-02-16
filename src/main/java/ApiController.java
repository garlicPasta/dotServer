import Datastructures.MultiResTree;
import Datastructures.MultiResolutionNode;
import fi.iki.elonen.NanoHTTPD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ApiController extends NanoHTTPD {

    final static int PAGE_SIZE = 1000;

    MultiResTree mrt = new MultiResTree();
    HashMap<String, String> map = new HashMap<>();


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
        String key =  params.get("depth") + " " + params.get("node") + " " + params.get("page");
        if (map.containsKey(key)){
            return NanoHTTPD.newFixedLengthResponse(map.get(key));
        }
        return NanoHTTPD.newFixedLengthResponse("Invalid Parameters \n depth:" + params.get("depth")
                + "\n num:" + params.get("num"));
    }

    public void fillUpMap() throws IOException {
        for (int depth = 0; depth <= mrt.getDepth(); depth++) {
            int node = 0;
            int c = 0;
            StringBuilder sb = new StringBuilder();
            for (Iterator<MultiResolutionNode> mrn = mrt.iterateSampleLevel(depth); mrn.hasNext();) {
                MultiResolutionNode n = mrn.next();
                BufferedReader br = new BufferedReader(new StringReader(n.rasterization.toString()));
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\n');
                    if (++i == PAGE_SIZE ) {
                        i=0;
                        map.put(Integer.toString(depth)+ " " + Integer.toString(node)+ " " + Integer.toString(c++),
                                sb.toString());
                        sb.setLength(0);
                    }
                }
                map.put(Integer.toString(depth)+ " " + Integer.toString(node)+ " " + Integer.toString(c++),
                        sb.toString());
                sb.setLength(0);
                node++;
                c = 0;
            }
        }

    }
}
