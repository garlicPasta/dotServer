import Datastructures.MultiResolutionTree;
import Datastructures.Point3DRGB;
import DataAccesLayer.ApiController;
import org.apache.commons.cli.*;
import org.apache.commons.lang.IllegalClassException;
import utils.NvmParser;
import utils.PlyParser;
import utils.TxtParser;

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
    private enum FileType {
        PLY, TXT, NVM
    }

    public App() throws IOException {}

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption(new Option("t", true, "specifies type of input file"));
        options.addOption(new Option("f", true, "specifies input file"));
        options.addOption(new Option("p", true, "specifies port of server"));
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);
        //NvmParser parser = new NvmParser("/model3.nvm");
        //TxtParser parser = new TxtParser("/tower.xyz");
        MultiResolutionTree mt = new MultiResolutionTree();
        for (Point3DRGB p : getPointData(cmd) ){
            mt.insert(p);
        }
        parser = null;
        mt.createIndex();

        ac = new ApiController(PORT, mt);
    }

    static public Iterable<Point3DRGB> getPointData(CommandLine cmd) throws MissingArgumentException {
        String fileType = new String();
        if (!cmd.hasOption('f'))
            throw new MissingArgumentException("Specify an input file");
        if (cmd.hasOption("t")){
            fileType = cmd.getOptionValue('t');
        }else{
            String[] filename = cmd.getOptionValue('f').split(".");
            if (filename.length > 2)
                throw new IllegalArgumentException("Malformed filename");
            fileType = filename[1];
        }
        String filePath = System.getProperty("user.dir") + "/" + cmd.getOptionValue('f');
        File f = new File(filePath);
        if (!f.exists()){
            throw new IllegalArgumentException("File does not exist");
        }

        switch (fileType){
            case "nvm":
                return new NvmParser(f);
            case "ply":
                PlyParser parser = new PlyParser();
                try {
                    parser.loadPLY(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return parser;
            case "txt":
                return new TxtParser(f);
            default:
                throw new IllegalArgumentException("Invalid file type");
        }
    }
}
