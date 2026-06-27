import java.util.*;
public class Node{
    private String name;
    private Integer distance = Integer.MAX_VALUE;
    private Map<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();
    private List<Node> shortestPath  = new LinkedList<>();
    private int coordX, coordY;
    public void addDestination(Node destination, int distance){
        adjacentNodes.put(destination, distance);
    }
    public Node(String name, int coordX, int coordY){
        this.name = name;
        this.coordX = coordX;
        this.coordY = coordY;
    }
    //setters
    public void setName(String name){
        this.name = name; 
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
    //getters
    public String getName(){
        return name;
    }
    public int getDistance(){
        return distance;
    }
    

    
    public void setShortestPath(List<Node> path){
        shortestPath = path;
    }
    public Map<Node, Integer> getAdjacentNodes(){
        return adjacentNodes;
    
    }
    public List<Node> getShortestPath(){
        return shortestPath;
    }


    public int getX(){
        return coordX;
    }

    public int getY(){
        return coordY;
    }

}
