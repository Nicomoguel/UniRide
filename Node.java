import java.util.*;
/**
 * Clase que define la que definitivamente es nuestra estructura de dato mas utilizada en todo el proyecto, el nodo.<br>
 * Cada nodo representa una ubicación en el mapa, todo el mapa que se ve en el programa esta lleno de nodos, existen alrededor de 140 nodos en todo el mapa, todos conectados entre si.<br>
 * Cada nodo tiene una ruta, que se inicializa luego de aplicar el algoritmo de Dijkstra sobre el grafo.
 */
public class Node{
    private String name;
    private Integer distance = Integer.MAX_VALUE;
    private Map<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();
    private List<Node> shortestPath  = new LinkedList<>();
    private int coordX, coordY;
    private final int radius = 5;

    /**
     * Método público que sirve para agregar adyacencias entre nuestro nodo y otro, este método lo hace de manera bidireccional
     * ya que hace la adyacencia normal y luego llama a si mismo para hacer la vuelta.
     * @param source El mismo nodo origen
     * @param destination El nodo destino
     * @param distance El peso de la arista
     */
    public void addDestination(Node source, Node destination, int distance){
        adjacentNodes.put(destination, distance);
        destination.addDestination(source, distance);
    }
    /**
     * 
     * Método sobre escrito del anterior que hace la adyacencia de forma unilateral, solo sirve para ser llamado por el método anterior y terminar la adyacencia.<br>
     * @param destination El nodo destino
     * @param distance El peso de la arista
     * 
     */
    public void addDestination(Node destination, int distance){
        adjacentNodes.put(destination, distance);
    }
    /**
     * Método público que remueve un enlace entre un nodo y otro, muy util en los programas que implementan nodos, ya que muchas veces se generaliza por la cantidad y los pocos que no nos sirven los eliminamos a mano<br>
     * Este método lo hace de manera bidireccional ya que hace la adyacencia normal y luego llama a si mismo para hacer la vuelta.
     * @param source El nodo origen
     * @param destination El nodo destino 
     */
    public void removeDestination(Node source, Node destination){
        adjacentNodes.remove(destination);
        destination.removeDestination(source);
    }
  /**
     * 
     * Método sobre escrito del anterior que elimina la adyacencia de forma unilateral, solo sirve para ser llamado por el método anterior y terminar de eliminar la adyacencia.<br>
     * @param destination El nodo destino
     * 
     */

    public void removeDestination(Node destination){
        adjacentNodes.remove(destination);
    }

    /**
     * El constructor de la clase Node, recibe algunos argumentos y los asigna a los atributos privados de la clase
     * @param name Nombre del nodo
     * @param coordX Coordenada X
     * @param coordY Coordenada Y
     */
    public Node(String name, int coordX, int coordY){
        this.name = name;
        this.coordX = coordX;
        this.coordY = coordY;
    }
    /** Setter del nombre
     * @param name Nombre del nodo
     */
    public void setName(String name){
        this.name = name; 
    }
    /** Setter del valor distancia
     * @param distance Distancia
     */
    public void setDistance(int distance){
        this.distance = distance;
    }
    /** Getter del nombre
     * @return String nombre
     */
    public String getName(){
        return name;
    }
    /** Getter de la distancia
     * @return distancia
     */
    public int getDistance(){
        return distance;
    }
    

    /**
     * Setter de la ruta mas corta desde el nodo de origen a este nodo, aplicado a cada nodo en el algoritmo de Dijkstra
     * @param path La lista ligada que contiene el camino mas corto
     */
    public void setShortestPath(List<Node> path){
        shortestPath = path;
    }

    /**
     * Getter con todos los nodos adyacentes a este nodo
     * @return Mapa con todos los nodos adyacentes
     */
    public Map<Node, Integer> getAdjacentNodes(){
        return adjacentNodes;
    
    }

    /**
     * Getter de la ruta mas corta
     * @return Ruta mas corta
     */
    public List<Node> getShortestPath(){
        return shortestPath;
    }

    /**
     * Getter coordenada X
     * @return La coordenada X del nodo
     */
    public int getX(){
        return coordX;
    }
    /**
     * Getter coordenada Y
     * @return La coordenada Y del nodo
     */
    public int getY(){
        return coordY;
    }
    /**
     * Getter del radio del nodo
     * @return El radio del nodo
     */
    public int getRadius(){
        return radius;
    }

}
