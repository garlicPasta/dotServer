package utils;


import Datastructures.Point3DRGB;
import org.la4j.vector.dense.BasicVector;

import java.io.*;
import java.util.Iterator;

public abstract class Parser implements Iterable<Point3DRGB>{

    private class PointIterator implements Iterator<Point3DRGB>{
        int currentElement = 0;

        @Override
        public boolean hasNext() {
            return currentElement < vertices.length;
        }

        @Override
        public Point3DRGB next() {
            Point3DRGB p =new Point3DRGB(new BasicVector(vertices[currentElement]),
                    colors[currentElement]);
            currentElement++;
            return p;
        }
    }

    @Override
    public Iterator<Point3DRGB> iterator() {
        return new PointIterator();
    }

    protected enum State{
        Header, Vertices
    }

    protected double[][] vertices;
    protected float[][] colors;
    protected State parserState;

    public Parser(File file){
        parserState= State.Header;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
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
        vertices = new double[verticesCounter][3];
        colors = new float[verticesCounter][3];
    }

    public double[][] getVertices() {
        return vertices;
    }

    public float[][] getColors() {
        return colors;
    }

    protected abstract void parseLine(String line);

}
