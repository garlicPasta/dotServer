package utils;

public class TxtParser extends Parser{
    int verticesCounter=0;
    int colorCounter=0;
    int lineCount;

    public TxtParser(String fileName) {
        super(fileName);
    }

    @Override
    protected void parseLine(String line) {
        if (parserState == State.Header) {
            lineCount = 20000000;
            createVertexArrays(lineCount);
            parserState = State.Vertices;
        }
        if (lineCount-- <= 0)
            return;
        String[] list = line.split("\\s+");
        vertices[verticesCounter++] = new double[] {Double.parseDouble(list[0]),
                Double.parseDouble(list[1]), Double.parseDouble(list[2])};
        colors[colorCounter++] = new float[] {Float.parseFloat(list[3]),
                Float.parseFloat(list[4]), Float.parseFloat(list[5])};
    }
}
