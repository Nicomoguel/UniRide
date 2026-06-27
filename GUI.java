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
    private Node nodeA = new Node("A", 280, 13);
    private Node nodeB = new Node("B", 325, 30);
    private Node nodeC = new Node("C", 373, 49);
    private Node nodeD = new Node("D", 500, 200);
    private Node nodeE = new Node("E", 350, 450);
    private Node nodeF = new Node("F", 300, 250);
    private Node[] nodes = {nodeA,nodeB,nodeC,nodeD,nodeE,nodeF};
    Graph graph = new Graph();


    public GUI(){
        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);
        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeC.addDestination(nodeE, 10);
        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeF.addDestination(nodeE, 5);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        graph = Dijkstra.shortestPath(graph, nodeA);
        try{
            map = ImageIO.read(new File("Map2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("No se pudo leer la imagen");
        }
    }
    private void paintNodes(Graphics g){

        //g.drawOval(nodeA.getX(), nodeA.getY(), 10, 10);
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
        Node test = nodeF;
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
        paintShortestPath(g);
    }
}
