package Datastructures;


import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.la4j.Vector;

import java.util.*;

import org.apache.commons.collections.map.DefaultedMap;
import org.la4j.vector.dense.BasicVector;
import org.la4j.vector.functor.VectorFunction;


public class Raster implements Iterable<Triplet<float[], float[], Integer>>{

    public static final int RASTER_SIZE = 32;
    double rasterStep;
    double cellLength;
    private Vector zeroVector;
    Map<Vector, Pair<float[], Integer>> rasterHash;

    public Raster(Vector center, double cellLength){
        this.zeroVector = center.subtract(cellLength / 2);
        this.rasterStep = cellLength / RASTER_SIZE;
        this.cellLength = cellLength;
        rasterHash = new HashMap<>();
        this.rasterHash = DefaultedMap.decorate(rasterHash, new Pair<>(new double[3], 0));
    }

    public void addToRaster(Point3DRGB prgb){
        Vector n =  normalizeVector(prgb.position);
        Pair<float[], Integer> value = rasterHash.get(n);
        rasterHash.put(n, new Pair<>(prgb.color ,value.getValue1() + 1));
    }

    public void addToRaster(Vector v, Pair<float[],Integer> pair){
        Vector normV = normalizeVector(v);
        Pair updatePair = pair.setAt1(rasterHash.get(normV).getValue1() + pair.getValue1());
        rasterHash.put(normV, updatePair);
    }

    private Vector normalizeVector(Vector v){
        Vector n = v.subtract(this.zeroVector);
        n.update(new VectorFunction() {
            @Override
            public double evaluate(int i, double value) {
                return Math.floor(value / rasterStep);
            }
        });

        if (n.get(0) > RASTER_SIZE || n.get(1) > RASTER_SIZE || n.get(2) > RASTER_SIZE) {
            throw new IllegalArgumentException("Vector is outside raster");
        }
        return n;

    }

    public Map<Vector, Pair<float[], Integer>> getDownSampledRaster() {
        Set<Vector> keys = rasterHash.keySet();
        Map<Vector, Pair<float[], Integer>> newRasterHash = DefaultedMap.decorate(new HashMap<>(), new Integer(0));

        for (Vector key: keys){
            Vector keyStart = key.copy();
            keyStart.update(new VectorFunction() {
                @Override
                public double evaluate(int i, double value) {
                    return value - value % 2 ;
                }
            });
            int sum = 0;
            float[] color = new float[3];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2 ; k++) {
                        Vector keyBase = keyStart.add(new BasicVector(new double[]{i,j,k}));
                        if ( rasterHash.containsKey(keyBase)) {
                            Pair<float[], Integer> v = rasterHash.get(keyBase);
                            color = v.getValue0();
                            sum += rasterHash.get(keyBase).getValue1();
                        }
                    }
                }
            }
            if (sum > 0)
                newRasterHash.put(keyStart.divide(2), new Pair<>(color, sum));
        }
        return newRasterHash;
    }

    public Map<Vector, Pair<float[], Integer>> getRaster() {
        return rasterHash;
    }

    /**
     * @return String
     * Returns rasterized samples as String: x z y r g b weight
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Vector, Pair<float[], Integer>> entry : rasterHash.entrySet()) {
            Vector position = entry.getKey().multiply(rasterStep).add(zeroVector);
            sb.append(position.toString());
            sb.append(' ');
            for (double d : entry.getValue().getValue0()){
                sb.append(Double.toString(d));
                sb.append(' ');
            }
            sb.append(entry.getValue().getValue1().toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public Iterator<Triplet<float[], float[], Integer>> iterator() {
        class RasterIterator implements Iterator<Triplet<float[], float[], Integer>> {
            Iterator<Map.Entry<Vector,Pair<float[],Integer>>> i = rasterHash.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public Triplet<float[], float[], Integer> next() {
                Map.Entry<Vector,Pair<float[],Integer>> e = i.next();
                Vector key = e.getKey().multiply(rasterStep).add(zeroVector);
                float[] pos = new float[] {(float) key.get(0), (float) key.get(1), (float) key.get(2)};
                return new Triplet<>(pos, e.getValue().getValue0(), e.getValue().getValue1());
            }
        }
        return new RasterIterator();
    }

    public int getSampleCount(){
        return rasterHash.size();
    }

    public Iterable<Triplet<Vector,float[], Integer>> iterateAbsolutSample() {
        class AbsolutRasterIterator implements Iterator<Triplet<Vector,float[], Integer>>, Iterable<Triplet<Vector,float[], Integer>> {
            Iterator<Map.Entry<Vector,Pair<float[],Integer>>> i = rasterHash.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public Triplet<Vector, float[], Integer> next() {
                Map.Entry<Vector,Pair<float[],Integer>> e = i.next();
                return new Triplet<>(e.getKey().multiply(rasterStep).add(zeroVector),
                        e.getValue().getValue0(),
                        e.getValue().getValue1());
            }

            @Override
            public Iterator<Triplet<Vector, float[], Integer>> iterator() {
                return this;
            }
        }
        return new AbsolutRasterIterator();
    }
}
