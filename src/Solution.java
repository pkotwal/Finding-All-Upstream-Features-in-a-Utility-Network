import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Solution {
	
	static Set<String> output;
	static Set<Node> toExplore;
	
	public static void main(String args[]){
		String InputDataSet = args[0];
		String StartingPointsFile = args[1];
		String OutputFile = args[2];
		
		ReadInput readInputFast = new ReadInput(InputDataSet, StartingPointsFile);
		
		output = new HashSet<>();
		toExplore = new HashSet<>();
		
		readInputFast.getInput();
		
		// Add All controllers found to be end nodes
		for(Node n: ReadInput.controllerList){
			n.isEndPoint = true;	
		}
		
		// For every start node perform DFS
		for(Entry<String, Boolean> entry: ReadInput.startingPointList.entrySet()){
			// If this starting point is a node
			if(!entry.getValue()){
				findPath(ReadInput.nodeMap.get(entry.getKey()));
			}
			// Starting point is an edge
			else{
				ArrayList<Edge> startingEdges = ReadInput.edgeMap.get(entry.getKey());
				for(Edge e: startingEdges){
					Node n1 = e.connection1;
					Node n2 = e.connection2;
					n1.edges.remove(e);
					n2.edges.remove(e);
					boolean n1Added = findPath(n1);
					boolean n2Added = findPath(n2);
					n1.addEdge(e);
					n2.addEdge(e);
					if(n1Added || n2Added){
						output.add(e.id);
					}
				}
			}
		}
		
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile));
			Iterator<String> it = output.iterator();
			while(it.hasNext()) {
				String feature = it.next();
			    out.write(feature.substring(0,feature.indexOf("}")+1));
			    out.newLine();
			}
			out.close();	
		}catch(Exception e){}
	}
	
	private static boolean findPath(Node node){
		toExplore.add(node);
		boolean newFeaturesAdded = false;
		while(!toExplore.isEmpty()){
			Node start = toExplore.iterator().next();
			Set<Node> visited = new HashSet<>();
			visited.add(start);
			
			// if starting point is a controller
			if(start.isEndPoint){
				output.add(start.id);	
			}
			Set<String> featuresAdded = DFS(0, start, visited);
			if(!featuresAdded.isEmpty()){
				newFeaturesAdded = true;
			}
			output.addAll(featuresAdded);
		}
		return newFeaturesAdded;
	}
	
	// DFS Flag = 0, when dfs is called for the first time
	// A node on the solution path will be in end but if it is not yet fully explored, must not return
	private static Set<String> DFS(int flag, Node start, Set<Node> visited){

		Set<String> features = new HashSet<>();
		toExplore.add(start);
		if(start.isEndPoint && flag==1){
			features.add(start.id);
			return features;
		}
		
		for(Edge e : start.edges){
			if(!e.removed){
				Node next = e.pathFrom(start);

				Set<String> toAppend = new HashSet<>();
				if(!visited.contains(next)){
					visited.add(next);
					toAppend.addAll(DFS(1, next, visited));
					if(!toAppend.isEmpty()){
						e.removed = true;
						start.isEndPoint = true;
						toAppend.add(start.id);
						toAppend.add(e.id);
						return toAppend;
					}
				}
			}
		}
		toExplore.remove(start);
		return features;
	}
}