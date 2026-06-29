import java.util.*;
public class Graph{
    private Set<Node> nodes = new HashSet<Node>();
    private List<Node> vertices = new LinkedList<Node>(); 

    
    public Graph(Node[] nodes){
        for(int i = 0; i < nodes.length; i++){
            this.addNode(nodes[i]);
        }
    }

    public void addNode(Node node){
        nodes.add(node);
        vertices.add(node);
    }
    //getters and setters
    public List<Node> getVertices(){
        return vertices;
    }


}
