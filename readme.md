# Finding All Upstream Features in a Utility Network

This project was for the GISCUP2018 for the ACM SigSpatial Conference http://sigspatial2018.sigspatial.org/giscup2018/

A presentation explaining the proposed solution can be found here: https://docs.google.com/presentation/d/1a15c5V--R_PvbYulSrYkpDOBfgcFpvefXwDs_qbOR-Y/edit?usp=sharing

A technical paper for the same can be found here: https://drive.google.com/file/d/1GiPLSKhkb1kvXEAMDD_D8uURLAK0xGrR/view?usp=sharing

The main source files are persent in the folder src.
The SampleDataset1 folder is added to provide an example as how to run the files.

## Steps to run:

cd into src folder 

1. Compile java files with its dependencies. json-simple library is used to help in parsing the JSON file.
javac -classpath <Path-to-json-simple-library>; *.java
javac -classpath ./libraries/json-simple-1.1.1.jar; *.java

2. The File Solution.java is the file to be run. Run this file with its dependencies
java -classpath <Path-to-json-simple-library>; Solution <Path-to-input-json-file> .<Path-to-starting-points-file> <Path-to-json-output-file>
java -classpath ./libraries/json-simple-1.1.1.jar; Solution ../SampleDataset1/SampleDataset1.json ../SampleDataset1/startingpoints.txt ../output.txt

## Main Idea:
- The main idea behind the program is to perform multiple depth first search (DFS) from the starting points to any of the end points (controllers).
- In the process of doing so, all points on the path from the starting point to the end point are also added to the list of end points (as any path to these points can eventually lead to a controller).
- The process repeats till all points are explored.
