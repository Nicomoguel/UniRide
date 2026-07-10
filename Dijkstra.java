import java.util.*;
/**
 * Clase que realiza el algoritmo de Dijkstra, un algoritmo diseñado para calcular la ruta mas corta entre un nodo origen y 
 * todos los demas nodos de un grafo ponderado
 * @author HoodCodeDepartment
 */
public class Dijkstra{
    /**
     * Método estático que calcula la ruta mas corta entre el nodo origen y todos los demás nodos del grafo
     * (Algoritmo de Dijkstra)
     * @param graph Grafo sobre el que se aplicará el algoritmo
     * @param source Nodo de origen
     * @return Devuelve el grafo modificado, cada nodo ahora tiene la ruta mas corta entre el origen y si mismo, una lista de nodos
     */
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
    
    /**
     * Método estatico que devuelve el nodo aun no marcado con la menor distancia
     * Itera sobre todos los nodos no marcados y consigue el nodo con la distancia menor
     * @param unsettled El conjunto de nodos aun no marcados
     * @return El nodo con la distancia mas corta
     */
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

    /**
     * Método estatico que determina si la ruta mas corta al nodo de evaluacion es la actual o la que tenía anteriormente
     * Si la ruta actual tiene un peso menor, se reemplaza con la que ya tenía el nodo
     * @param evaluationNode Nuestro nodo de evaluacion
     * @param edgeWeight Peso de la arista (conexion)
     * @param sourceNode Nodo de origen
     */

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
