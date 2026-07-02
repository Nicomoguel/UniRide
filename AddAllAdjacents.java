import java.util.*;

public class AddAllAdjacents{
    public static Graph add(Graph graph){
        List<Node> vertices = graph.getVertices();
        Node[] nodes = vertices.toArray(new Node[0]);

        for(int i = 0; i < nodes.length; i++){
            if(i != nodes.length - 1){
                nodes[i].addDestination(nodes[i], nodes[i+1], 2);
            }
        }
        nodes[93].addDestination(nodes[93],nodes[102], 2);
        nodes[42].addDestination(nodes[42],nodes[32], 2);
        nodes[4].addDestination(nodes[4],nodes[42], 2);
        nodes[42].addDestination(nodes[42],nodes[64], 2);
        nodes[64].addDestination(nodes[64],nodes[92],2);
        nodes[92].addDestination(nodes[92], nodes[128],2);
        nodes[128].addDestination(nodes[128], nodes[159],2);
        nodes[159].addDestination(nodes[159], nodes[185],2);
        return graph;
    }
}
