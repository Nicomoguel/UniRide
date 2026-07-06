import java.util.*;
public class Node{
    private String name;
    private Integer distance = Integer.MAX_VALUE;
    private Map<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();
    private List<Node> shortestPath  = new LinkedList<>();
    private int coordX, coordY;
    private final int radius = 5;


    public void addDestination(Node source, Node destination, int distance){
        adjacentNodes.put(destination, distance);
        destination.addDestination(source, distance);
    }

    public void addDestination(Node destination, int distance){
        adjacentNodes.put(destination, distance);
    }

    public void removeDestination(Node source, Node other){
        adjacentNodes.remove(other);
        other.removeDestination(source);
    }

    public void removeDestination(Node other){
        adjacentNodes.remove(other);
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

    public int getRadius(){
        return radius;
    }

}
