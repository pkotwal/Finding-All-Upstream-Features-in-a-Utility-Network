import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadInput {
	
	String folder_name;
	String json_file_path;
	String starting_points_file_path;
	
	public static Map<String, ArrayList<String>> nodeTerminals;
	public static Map<String, Node> nodeMap;
	public static Map<String, ArrayList<Edge>> edgeMap; // <ID, List_of_edges>
	public static Map<String, Boolean> startingPointList; // <ID, IsEdge>
	public static Set<Node> controllerList;
	
	public ReadInput(String InputFileName, String StartingPointsFile){
		this.json_file_path = InputFileName;
		this.starting_points_file_path = StartingPointsFile;
	}
	
	public void getInput(){
		nodeMap = new HashMap<String, Node>();
		edgeMap = new HashMap<String, ArrayList<Edge>>();
		startingPointList = new HashMap<String, Boolean>();
		controllerList = new HashSet<Node>();
		nodeTerminals = new HashMap<String, ArrayList<String>>();
		readJSON();
		readStartingPointsFile();
	}
	
	private void readStartingPointsFile(){
		try {
			File file = new File(starting_points_file_path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String startingpoint;
			while ((startingpoint = bufferedReader.readLine()) != null) {
				startingpoint =  startingpoint.trim();
				if(nodeTerminals.containsKey(startingpoint)){
					// if starting point is a node
					ArrayList<String> terminals = nodeTerminals.get(startingpoint);
					for(String terminal: terminals){
						startingPointList.put(startingpoint+terminal, false);	
					}
				}else{
					// if starting point is an edge
					if(edgeMap.containsKey(startingpoint)){
						startingPointList.put(startingpoint, true);
					}
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readJSON(){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(json_file_path));
			
			// Create JSONObject of JSON file
			JSONObject jsonObject = (JSONObject) obj;
			
			// Extract Features from rows array
			JSONArray rows = (JSONArray) jsonObject.get("rows");
			Iterator<?> rowsIterator = rows.iterator();
			while (rowsIterator.hasNext()) {
				JSONObject innerObj = (JSONObject) rowsIterator.next();        
				networkBuilder(innerObj);      
			}  
			
			// Extract Controllers from controller array
			JSONArray controllers = (JSONArray) jsonObject.get("controllers");
			Iterator<?> controllersIterator = controllers.iterator();
			while (controllersIterator.hasNext()) {
				JSONObject innerObj = (JSONObject) controllersIterator.next();        
				String controller = (String)innerObj.get("globalId");
				String terminalId = String.valueOf((Long)innerObj.get("terminalId"));
				controllerList.add(nodeMap.get(controller+terminalId));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();   
		}
	}
	
	private void networkBuilder(JSONObject row){
		String from = (String)row.get("fromGlobalId");
    	String to = (String)row.get("toGlobalId");
    	String via = (String) row.get("viaGlobalId");
    	
    	String fromTerminalId = String.valueOf((Long)row.get("fromTerminalId"));
    	String toTerminalId = String.valueOf((Long)row.get("toTerminalId"));
    	
    	if(nodeTerminals.containsKey(from)){
    		ArrayList<String> terminals = nodeTerminals.get(from);
    		terminals.add(fromTerminalId);
    		nodeTerminals.put(from, terminals);
    	}else{
    		ArrayList<String> terminals = new ArrayList<String>();
    		terminals.add(fromTerminalId);
    		nodeTerminals.put(from, terminals);
    	}
    	
    	if(nodeTerminals.containsKey(to)){
    		ArrayList<String> terminals = nodeTerminals.get(to);
    		terminals.add(toTerminalId);
    		nodeTerminals.put(to, terminals);
    	}else{
    		ArrayList<String> terminals = new ArrayList<String>();
    		terminals.add(toTerminalId);
    		nodeTerminals.put(to, terminals);
    	}
    	Node nodeTo, nodeFrom;
    	
    	// check if node object already created
    	// returns object if created
    	// else creates new object
    	if(nodeMap.containsKey(from+fromTerminalId)){
    		nodeFrom = nodeMap.get(from+fromTerminalId);
    	}else{
    		nodeFrom = new Node(from+fromTerminalId);
    		nodeMap.put(from+fromTerminalId, nodeFrom);
    	}
    	
    	if(nodeMap.containsKey(to+toTerminalId)){
    		nodeTo = nodeMap.get(to+toTerminalId);
    	}else{
    		nodeTo = new Node(to+toTerminalId);
    		nodeMap.put(to+toTerminalId, nodeTo);
    	}
    	
    	Edge edge = new Edge(via, nodeTo, nodeFrom);
    	if(edgeMap.containsKey(via)){
    		ArrayList<Edge> edgeList = edgeMap.get(via);
    		edgeList.add(edge);
    		edgeMap.put(via, edgeList);
    	}else{
    		ArrayList<Edge> edgeList = new ArrayList<Edge>();
    		edgeList.add(edge);
    		edgeMap.put(via, edgeList);
    	}
    	nodeFrom.addEdge(edge);
    	nodeTo.addEdge(edge);
	}
}