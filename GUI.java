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
    private Graph graph = new Graph();
    public GUI(){
        ReadNodes.readDoc(graph, "coordenadas.txt");
        AddAllAdjacents.add(graph);
        ReadRemoveAdjacents.removeAdjacents(graph, "RemoveNodes.txt");
        try{
            map = ImageIO.read(new File("Map2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("No se pudo leer la imagen");
        }
    }
    private void paintNodes(Graphics g){
        List<Node> vertices = graph.getVertices();
        for(Node node : vertices){
            g.drawOval(node.getX(), node.getY(), 10, 10);
        }
    }
    
    private void paintLinks(Graphics g){

        /*
        for(int i = 0; i < nodes.length; i++){
            Map<Node, Integer> adjacents = nodes[i].getAdjacentNodes();
            for(Map.Entry<Node, Integer> entry : adjacents.entrySet()){
                Node adjacent = entry.getKey();
                g.drawLine(nodes[i].getX(), nodes[i].getY(), adjacent.getX(), adjacent.getY());
            }
        }
        */
        List<Node> vertices = graph.getVertices();
        for(Node node : vertices){
            Map<Node, Integer> adjacents = node.getAdjacentNodes();
            for(Map.Entry<Node, Integer> entry : adjacents.entrySet()){
                Node adjacent = entry.getKey();
                g.drawLine(node.getX(), node.getY(), adjacent.getX(), adjacent.getY());
            }
        }

    }
    

    public Dimension getPreferredSize() {
        return new Dimension(566,698);
    }
/*
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

  */ 

    protected void paintComponent(Graphics g){
        g.drawImage(map, 0, 0, this);
        paintNodes(g);
        paintLinks(g);
       // paintShortestPath(g);
    }
}
