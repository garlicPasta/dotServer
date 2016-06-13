# dotServer

dotServer is part of a client-server system  for displaying and exploring 
huge Pointclouds on Android Devices.

The corresponding client u can find [here](https://github.com/garlicPasta/dotViewer).

![example_picture](https://raw.githubusercontent.com/garlicPasta/AndroidPointCloudVisualizer/master/readme/img/bstelle_mid4.png)
![example_picture2](https://raw.githubusercontent.com/garlicPasta/AndroidPointCloudVisualizer/master/readme/img/close_terra1.png)

## Dependencies :
* gradle
* goggle protocol buffers

## Build
For building run the following command in the project root

    gradle build

## Build big jar
For building a jar with all dependencies run the following command

    gradle fatJar

## Usage
For running the server its necessary to supply pointcloud file and a port.
The server supports the following filetypes nvm,ply and txt (xyzrgb)

    -h         prints help
    -f <arg>   specifies input file
    -p <arg>   specifies port of server
    -t <arg>   specifies type of input file

### Example
After running gradle fatJar u can find the jar in build/libs. Now run the server with the following command:

    java -jar dotServer-all-1.0-SNAPSHOT.jar -f foo.nvm

