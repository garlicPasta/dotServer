package utils;


import java.io.*;
import java.nio.FloatBuffer;

public abstract class Parser {

    protected enum State{
        Header, Vertices
    }

    protected float[][] vertices;
    protected float[][] colors;
    protected State parserState;

    public Parser(String fileName){
        parserState= State.Header;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(this.getClass().getResource(fileName).getFile()));
            String line;
            while( (line=br.readLine()) != null )
            {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void createVertexArrays(int verticesCounter){
        vertices = new float[verticesCounter][3];
        colors = new float[verticesCounter][3];
    }

    public float[][] getVertices() {
        return vertices;
    }

    public float[][] getColors() {
        return colors;
    }

    protected abstract void parseLine(String line);

}
