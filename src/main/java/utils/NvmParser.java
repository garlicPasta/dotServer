package utils;

import java.io.File;

public class NvmParser extends Parser {

    int lineCounter;
    int cameraCounter;
    int verticesCounter;

    public NvmParser(File file){
        super(file);
    }

    @Override
    protected void parseLine(String line) {
        if (line.isEmpty()) {
            return;
        }
        if (parserState == State.Header) {
            if (lineCounter == 0) {
                lineCounter++;
                return;
            }
            if (lineCounter == 1) {
                lineCounter++;
                cameraCounter = Integer.parseInt(line);
                return;
            } else if (cameraCounter > 0) {
                cameraCounter--;
                return;
            } else {
                parserState = State.Vertices;
                verticesCounter = Integer.parseInt(line);
                createVertexArrays(verticesCounter);
                return;
            }
        }
        if (parserState == State.Vertices){
            if (verticesCounter > 0){
                verticesCounter--;
                String[] list = line.split(" ");
                for(int i=0; i < 3 ; i++) {
                    vertices[verticesCounter][i] = Float.valueOf(list[i]);
                    colors[verticesCounter][i] = Integer.valueOf(list[3 + i]);
                }
            }
            return;
        }
    }
}
