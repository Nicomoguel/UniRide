import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GUI extends JPanel{

    private BufferedImage map;
    private Graph graph = new Graph();
    private List<Node> listNodes;
    private Node[] nodes;
    private User user;
    private JLabel userIdLabel = new JLabel("");
    private JLabel userToleranceLabel = new JLabel("");
    private JLabel userIDMEXLabel = new JLabel("");
    private JLabel userPoints = new JLabel("");
    private JFrame frame = new JFrame();
    public GUI(User user, JFrame frame){
        this.user = user;
        this.frame = frame;
        userPanel();
        ReadNodes.readDoc(graph, "coordenadas.txt");
        AddAllAdjacents.add(graph);
        ReadAddColumns.addCols(graph, "columnas.txt");
        ReadRemoveAdjacents.removeAdjacents(graph, "RemoveNodes.txt");
        this.listNodes = graph.getVertices();
        this.nodes = this.listNodes.toArray(new Node[0]);
        Dijkstra.shortestPath(graph, nodes[0]);
        try{
            map = ImageIO.read(new File("Map2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("No se pudo leer la imagen");
        }
    }

    private void userPanel(){
        String tolerance = Short.toString(user.getTolerance()); 
        String points = String.valueOf(user.getUserPoints());
        userIdLabel.setBounds(600, 20, 100, 25);
        userToleranceLabel.setBounds(600, 50, 100, 25);
        userIDMEXLabel.setBounds(600, 80, 100, 25);
        userPoints.setBounds(600, 110, 100, 25);
        userIdLabel.setText("ID: "+user.getStudentId());
        userToleranceLabel.setText("Tolerance: "+tolerance);
        userIDMEXLabel.setText("IDMEX: "+user.getIDMEX());
        userPoints.setText("User points: " + points);
        frame.add(userIdLabel);
        frame.add(userToleranceLabel);
        frame.add(userPoints);
        frame.add(userIDMEXLabel);
    }

    private void paintNodes(Graphics g){
        List<Node> vertices = graph.getVertices();
        for(Node node : vertices){
            g.drawOval(node.getX(), node.getY(), 10, 10);
        }
    }
    
    private void paintLinks(Graphics g){
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
        return new Dimension(800,700);
    }

    private void paintShortestPath(Graphics g){
        Node test = this.nodes[this.nodes.length-1];
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
        //paintLinks(g);
        paintShortestPath(g);
    }
}
