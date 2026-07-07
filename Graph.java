import java.util.*;
public class Graph{
    private Set<Node> nodes = new HashSet<Node>();
    private LinkedList<Node> vertices = new LinkedList<Node>(); 

    public void addNode(Node node){
        nodes.add(node);
        vertices.add(node);
    }
    public LinkedList<Node> getVertices(){
        return vertices;
    }
}
