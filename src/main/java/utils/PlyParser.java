package utils;

import Datastructures.Point3DRGB;
import org.la4j.vector.dense.BasicVector;
import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReader;
import org.smurn.jply.PlyReaderFile;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class PlyParser implements Iterable<Point3DRGB>{

    /* data fields to store points, colors, faces information read from PLY file */
    private double[] points = null;
    private float[] colors = null;

    public void loadPLY(File file) throws IOException {
        PlyReader ply = new PlyReaderFile(file);

        int vertexCount = ply.getElementCount("vertex");

        ElementReader reader;


		/* Iterate to read elements */
        while ((reader = ply.nextElementReader()) != null) {
            String elementType = reader.getElementType().getName();

            if (elementType.equals("vertex")) {
                if (points != null)
                    continue;
                points = new double[3 * vertexCount];
                colors = new float[3 * vertexCount];

                Element element;
                int x = 0;
                float maxZ = 0;
                while ((element = reader.readElement()) != null) {
                                   /* manipulated array indexes to store  */
                    points[3 * x + 0] = element.getDouble("x");
                    points[3 * x + 1] = element.getDouble("y");
                    points[3 * x + 2] = element.getDouble("z");
                    colors[3 * x + 0] = (float) points[3 * x + 2];
                    colors[3 * x + 1] = 0;
                    colors[3 * x + 2] = 0;
                    maxZ = Math.max(maxZ, (float) points[3 * x + 2]);
                    x++;
                }
                for (int i = 0; i < x; i++){
                    colors[3 * i] = 205;
                }
                System.out.println("PLY importer: " + x + " vertices counted");
            }
            reader.close();
        }
       ply.close();
    }

    @Override
    public Iterator<Point3DRGB> iterator() {
        return new PointIterator();
    }

    private class PointIterator implements Iterator<Point3DRGB>{
        int currentElementIndex = 0;

        @Override
        public boolean hasNext() {
            return currentElementIndex < points.length;
        }

        @Override
        public Point3DRGB next() {
            double[] currentXYZ = new double[]{points[currentElementIndex],
                    points[currentElementIndex+1],
                    points[currentElementIndex + 2]};
            float[] currentRGB = new float[]{colors[currentElementIndex],
                    colors[currentElementIndex+1],
                    colors[currentElementIndex + 2]};

            Point3DRGB p =new Point3DRGB(currentXYZ, currentRGB);
            currentElementIndex += 3;
            return p;
        }
    }

}

