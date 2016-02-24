import Datastructures.MultiResTree;
import Datastructures.Point3DRGB;
import DataAccesLayer.ApiController;
import utils.NvmParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class App {
    private static final int PORT= 8080;
    private static final int PAGESIZE= 1000;
    private static Map<String, String> map = new HashMap<>();
    private static Properties properties;
    private static ApiController ac;

    public App() throws IOException {
    }

    public static void main(String[] args) throws Exception {
        NvmParser parser = new NvmParser("/model2.nvm");
        MultiResTree mt = new MultiResTree();
        for (Point3DRGB p : parser ){
            mt.insert(p);
        }

        mt.createIndex();

        ac = new ApiController(PORT, mt);


        properties = loadProperties();
    }

    public static Properties loadProperties() throws IOException {
        Properties defaultProps = new Properties();
        FileInputStream  in = null;
        try {
            in = new FileInputStream(App.class.getResource("config.properties").getPath());
            defaultProps.load(in);
        } finally {
            in.close();
        }
        return defaultProps;
    }
}
