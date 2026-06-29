import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GUI extends JPanel{

    private BufferedImage map;
    private Node node1 = new Node("1", 280, 13);
    private Node node2 = new Node("2", 325, 30);
    private Node node3 = new Node("3", 373, 49);
    private Node node4 = new Node("4", 432, 67);
    private Node node5 = new Node("5", 489, 74);
    private Node node6 = new Node("6", 538, 73);
    private Node node7 = new Node("7", 228, 12);
    private Node node8 = new Node("8", 274, 30);
    private Node node9 = new Node("9", 320, 49);
    private Node node10 = new Node("10", 365, 68);
    private Node node11 = new Node("11", 406, 84);
    private Node[] nodes = {node1,node2,node3,node4,node5,node6, node7, node8, node9, node10, node11};
    Graph graph = new Graph();


    public GUI(){
        /*
        node1.addDestination(node2, 10);
        node1.addDestination(node3, 15);
        node2.addDestination(node4, 12);
        node2.addDestination(node6, 15);
        node3.addDestination(node5, 10);
        node4.addDestination(node5, 2);
        node4.addDestination(node6, 1);
        node6.addDestination(node5, 5);
        */
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);
        graph = Dijkstra.shortestPath(graph, node1);
        try{
            map = ImageIO.read(new File("Map2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("No se pudo leer la imagen");
        }
    }
    private void paintNodes(Graphics g){

        //g.drawOval(node1.getX(), nodeA.getY(), 10, 10);
        //
        for(int i = 0; i < nodes.length; i++){
            g.drawOval(nodes[i].getX(), nodes[i].getY(), 10, 10);
        }
    }
    private void paintLinks(Graphics g){
        for(int i = 0; i < nodes.length; i++){
            Map<Node, Integer> adjacents = nodes[i].getAdjacentNodes();
            for(Map.Entry<Node, Integer> entry : adjacents.entrySet()){
                Node adjacent = entry.getKey();
                g.drawLine(nodes[i].getX(), nodes[i].getY(), adjacent.getX(), adjacent.getY());
            }
        }
    }

    private void paintShortestPath(Graphics g){
        Node test = node6;
        List<Node> nodes = test.getShortestPath();
        for(int i = 0; i < nodes.size(); i++){
            if(i > 0){
                Node prevNode = nodes.get(i-1);
                Node actNode = nodes.get(i);
                g.drawLine(prevNode.getX(), prevNode.getY(), actNode.getX(), actNode.getY());
            }
            if(i == (nodes.size() - 1)){
                Node actNode = nodes.get(i);
                g.drawLine(actNode.getX(), actNode.getY(), test.getX(), test.getY());
            }
        }
    }

   

    protected void paintComponent(Graphics g){
        g.drawImage(map, 0, 0, this);
        paintNodes(g);
       // paintLinks(g);
       // paintShortestPath(g);
    }
}
