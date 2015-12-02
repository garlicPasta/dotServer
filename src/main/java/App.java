import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import utils.NvmParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;


public class App extends NanoHTTPD {
    private static int PORT= 8080;

    private static int PAGESIZE= 100;
    private static Map<String, String> map = new HashMap<>();

    public App() throws IOException {
        super(PORT);
        System.out.println("\nRunning! Point your browers to http://localhost:"+ PORT +"/ \n");
    }

    public static void main(String[] args) {
        fillMap();
        System.out.println("Dictionary filled up");
        ServerRunner.run(App.class);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();
        return NanoHTTPD.newFixedLengthResponse(map.get(params.get("key")));
    }

    public static void fillMap(){
        NvmParser parser = new NvmParser("/model4.nvm");
        float[][] vertices = parser.getVertices();
        float[][] colors = parser.getColors();
        StringBuilder sB = new StringBuilder();

        int key = 0;

        for (int i=0; i< vertices.length; i++){
            if (i % PAGESIZE == PAGESIZE -1){
                map.put(Integer.toString(key), sB.toString());
                sB = new StringBuilder();
                key++;
            }

            for (int j=0; j<3; j++){
                sB.append(vertices[i][j]);
                sB.append(' ');
            }

            for (int j=0; j<3; j++){
                sB.append(colors[i][j]);
                sB.append(' ');
            }
            sB.append('\n');
        }
    }
}
