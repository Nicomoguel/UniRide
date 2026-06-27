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

    public static void main(String[]s){



        Node nodeA = new Node("A",10,20);
        Node nodeB = new Node("B",30,20);
        Node nodeC = new Node("C",30,50);
        Node nodeD = new Node("D",30,40);
        Node nodeE = new Node("E",60,70);
        Node nodeF = new Node("F",80,70);
        

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);
        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeC.addDestination(nodeE, 10);
        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeF.addDestination(nodeE, 5);
        Graph graph = new Graph();
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        graph = Dijkstra.shortestPath(graph, nodeA);


        //List<Node> shortest = nodeE.getShortestPath();
        //for(Node node : shortest){
        //    System.out.print(node.getName() + "->");
        //}
        //    System.out.println();

        List<Node> vertices = graph.getVertices();
        for(Node vertice : vertices){
            List<Node> shortest = vertice.getShortestPath();
            System.out.print(vertice.getName() + ": ");
            for(Node node : shortest){
                System.out.print(node.getName() + "->");
                
            }
            System.out.println();
        }


    }

}
