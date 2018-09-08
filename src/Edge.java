
public class Edge {
	String id;
	Node connection1;
	Node connection2;
	boolean removed;
	
	public Edge(String id, Node to, Node from){
		this.id = id;
		this.connection1 = from;
		this.connection2 = to;
	}
	
	public Node pathFrom(Node startNode){
		if(startNode == connection1)
			return connection2;
		if(startNode == connection2)
			return connection1;
		return null;
	}
}
