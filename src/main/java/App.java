import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class App extends NanoHTTPD {
    private static int PORT= 8080;

    public App() throws IOException {
        super(PORT);
        System.out.println("\nRunning! Point your browers to http://localhost:"+ PORT +"/ \n");
    }

    public static void main(String[] args) {
        ServerRunner.run(App.class);
    }

    @Override
    public Response serve(IHTTPSession session) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.getClass().getResource("model2.nvm").getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return NanoHTTPD.newChunkedResponse(Response.Status.OK, MIME_PLAINTEXT, fis);
    }
}
