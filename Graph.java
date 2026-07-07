import java.util.*;
/**
 * Clase que define un grafo tal cual por su definición en las matemáticas
 * Como sabemos, un grafo es un conjunto de Vértices (Nodos) y enlaces, en este caso nosotros necesitamos un grafo ponderado
 * @author HoodCodeDepartment
 */
public class Graph{
    private Set<Node> nodes = new HashSet<Node>();
    private LinkedList<Node> vertices = new LinkedList<Node>(); 

    /**
     * Método para agregar un nodo a la lista ligada de nodos que nos sirve para agregar las adyacencias despues
     * @param node Nodo se agregará al grafo
     */
    public void addNode(Node node){
        nodes.add(node);
        vertices.add(node);
    }
    /**
     * Getter de la lista de vertices del grafo
     * @return La lista de vertices
     */
    public LinkedList<Node> getVertices(){
        return vertices;
    }
}
