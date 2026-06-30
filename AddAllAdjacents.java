import java.util.*;

public class AddAllAdjacents{
    public static Graph add(Graph graph){
        List<Node> vertices = graph.getVertices();
        Node[] nodes = vertices.toArray(new Node[0]);

        for(int i = 0; i < nodes.length; i++){
            if(i != nodes.length - 1){
                nodes[i].addDestination(nodes[i+1], 2);
            }
        }
        for(int i = nodes.length - 1; i >= 0; i--){
            if(i != 0){
                nodes[i].addDestination(nodes[i-1], 2);
            }
        }
        nodes[7].removeDestination(nodes[7], nodes[8]);
        nodes[18].removeDestination(nodes[18], nodes[19]);
        nodes[26].remno

            /*
        nodes[0].addDestination(nodes[1], 2);
        nodes[0].addDestination(nodes[9], 1);
        nodes[1].addDestination(nodes[2], 2);
        nodes[1].addDestination(nodes[10], 1);
        nodes[2].addDestination(nodes[3], 2);
        nodes[2].addDestination(nodes[11], 1);
        nodes[3].addDestination(nodes[4], 1);
        nodes[3].addDestination(nodes[12], 1);
        nodes[4].addDestination(nodes[5], 1);
        nodes[4].addDestination(nodes[42], 2);
        nodes[5].addDestination(nodes[6], 2);
        nodes[5].addDestination(nodes[4], 1);
        nodes[6].addDestination(nodes[7], 2);
        nodes[6].addDestination(nodes[14], 1);
        nodes[6].addDestination(nodes[5], 2);
        nodes[7].addDestination(nodes[6], 2);
        nodes[7].addDestination(nodes[17], 1);
        nodes[8].addDestination(nodes[9], 2);
        nodes[8].addDestination(nodes[19], 1);
        nodes[9].addDestination(nodes[8], 2);
        nodes[9].addDestination(nodes[0], 1);
        nodes[9].addDestination(nodes[10], 2);
        nodes[9].addDestination(nodes[20], 1);

        */
        return graph;
    }
}
