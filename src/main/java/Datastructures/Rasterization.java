package Datastructures;


import org.la4j.Vector;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.DefaultedMap;
import org.la4j.vector.dense.BasicVector;
import org.la4j.vector.functor.VectorFunction;

public class Rasterization {

    Vector zeroVector;
    Vector colors;
    public static final int rasterSize = 128;
    double rasterStep;
    double cellLength;
    Map<Vector, Integer> rasterHash;

    public Rasterization(Vector center, double cellLength){
        this.zeroVector = center.subtract(cellLength / 2);
        this.rasterStep = cellLength / rasterSize;
        this.cellLength = cellLength;
        rasterHash = new HashMap<>();
        this.rasterHash = DefaultedMap.decorate(rasterHash, new Integer(0));
    }

    public void addToRaster(Vector p){
        Vector n = p.subtract(this.zeroVector);
        n.update(new VectorFunction() {
            @Override
            public double evaluate(int i, double value) {
                return Math.floor(value / rasterStep);
            }
        });

        if (n.get(0) > rasterSize || n.get(1) > rasterSize || n.get(2) > rasterSize) {
            throw new IllegalArgumentException("Vector is outside raster");
        }
        rasterHash.put(n, rasterHash.get(n) + 1);
    }

    public Map<Vector, Integer> getDownSampledRaster() {
        Set<Vector> keys = rasterHash.keySet();
        Map<Vector, Integer> newRasterHash = DefaultedMap.decorate(new HashMap<>(), new Integer(0));

        for (Vector key: keys){
            Vector keyStart = key.copy();
            keyStart.update(new VectorFunction() {
                @Override
                public double evaluate(int i, double value) {
                    return value - value % 2 ;
                }
            });
            int sum = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2 ; k++) {
                        Vector keyBase = keyStart.add(new BasicVector(new double[]{i,j,k}));
                        if ( rasterHash.containsKey(keyBase)) {
                            sum += rasterHash.get(keyBase);
                        }
                    }
                }
            }
            newRasterHash.put(keyStart.divide(2), sum);
        }
        return newRasterHash;
    }

    public Map<Vector, Integer> getRaster() {
        return rasterHash;
    }

    /**
     * @return String
     * Returns rasterized samples as String: x z y r g b weight
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Vector, Integer> entry : rasterHash.entrySet()) {
            Vector position = entry.getKey().multiply(rasterStep).add(zeroVector);
            sb.append(position.toString());
            sb.append(' ');
            sb.append(colors.toString());
            sb.append(' ');
            sb.append(entry.getValue().toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    public Vector getColors() {
        return colors;
    }

    public void setColors(Vector colors) {
        this.colors = colors;
    }
}
