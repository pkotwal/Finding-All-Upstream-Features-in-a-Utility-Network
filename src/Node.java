import java.util.ArrayList;

public class Node {
	String id;
	ArrayList<Edge> edges;
	boolean isEndPoint;
	
	public Node(String id){
		this.edges = new ArrayList<>();
		this.id = id;
	}
	
	public void addEdge(Edge e){
		edges.add(e);
	}
}
