import java.util.*;
public class Dijkstra{
    //Dijkstra algorithm
    public static Graph shortestPath(Graph graph, Node source){
        source.setDistance(0);
        Set<Node> settled = new HashSet<Node>();
        Set<Node> unsettled = new HashSet<Node>();
        unsettled.add(source);
        while(!unsettled.isEmpty()){
            Node currentNode = getLowestDistance(unsettled);
            unsettled.remove(currentNode);
            for(Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()){
                Node adjacentNode = adjacencyPair.getKey();
                int edgeWeight = adjacencyPair.getValue();
                if(!settled.contains(adjacentNode)){
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettled.add(adjacentNode);
                }
            }
            settled.add(currentNode);
        }
        return graph;
    }
    

    private static Node getLowestDistance(Set<Node> unsettled){
        Node shortest = null;
        int lowestDistance = Integer.MAX_VALUE;
        for(Node node : unsettled){
            int nodeDistance = node.getDistance();
            if(nodeDistance < lowestDistance){
                lowestDistance = nodeDistance;
                shortest = node;
            }
        }
        return shortest;

    }

    private static void calculateMinimumDistance(Node evaluationNode, int edgeWeight, Node sourceNode){
        int sourceDistance = sourceNode.getDistance();
        if(sourceDistance + edgeWeight < evaluationNode.getDistance()){
            evaluationNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
