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
    private BufferedImage logo;
    private Graph graph = new Graph();
    private List<Node> listNodes;
    private Node[] nodes;
    private User user;
    private JLabel userIdLabel = new JLabel("");
    private JLabel userToleranceLabel = new JLabel("");
    private JLabel userIDMEXLabel = new JLabel("");
    private JLabel userPoints = new JLabel("");
    private JFrame frame = new JFrame();
    private ArrayList<Passenger> passengers;
    public GUI(User user, ArrayList<Passenger> passengers,JFrame frame){
        this.user = user;
        this.frame = frame;
        this.passengers = passengers;
        userPanel();
        ReadNodes.readDoc(graph, "coordenadas.txt");
        AddAllAdjacents.add(graph);
        ReadAddColumns.addCols(graph, "columnas.txt");
        ReadRemoveAdjacents.removeAdjacents(graph, "RemoveNodes.txt");
        this.listNodes = graph.getVertices();
        this.nodes = this.listNodes.toArray(new Node[0]);
        try{
            map = ImageIO.read(new File("Map2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("Couldn't read the image");
        }
        try{
            logo = ImageIO.read(new File("icon2.jpeg"));
        }
        catch(IOException ex){
            System.out.println("Couldn't read the image");
        }


    }

    private void userPanel(){
        String tolerance = Short.toString(user.getTolerance()); 
        String points = String.valueOf(user.getUserPoints());
        userIdLabel.setBounds(600, 270, 100, 25);
        userToleranceLabel.setBounds(600, 300, 100, 25);
        userIDMEXLabel.setBounds(600, 330, 100, 25);
        userPoints.setBounds(600, 360, 100, 25);
        userIdLabel.setText("ID: "+user.getStudentId());
        userToleranceLabel.setText("Tolerance: "+tolerance);
        userIDMEXLabel.setText("IDMEX: "+user.getIDMEX());
        userPoints.setText("User points: " + points);
        frame.add(userIdLabel);
        frame.add(userToleranceLabel);
        frame.add(userPoints);
        frame.add(userIDMEXLabel);
    }

    private void paintPassengers(Graphics g, ArrayList<Passenger> passengers){
        Color myColor = new Color(37, 161, 74);
        g.setColor(myColor);
        for(Passenger passenger : passengers){
            Node node = passenger.getSource();
            g.fillOval(node.getX() - node.getRadius(), node.getY() - node.getRadius(), 2*node.getRadius(), 2*node.getRadius());
        }
        g.setColor(Color.BLACK);
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
        Node source = this.user.getSource();
        Node destination = this.user.getDestination();
        for(int i = 0; i < nodes.length; i++){
            if(source.getX() == nodes[i].getX() && source.getY() == nodes[i].getY()){
                source = nodes[i];
            }
            if(destination.getX() == nodes[i].getX() && destination.getY() == nodes[i].getY()){
                destination = nodes[i];
            }

        }

        Dijkstra.shortestPath(graph, source);
        List<Node> route = destination.getShortestPath();

        for(int i = 0; i < route.size(); i++){
            if(i > 0){
                Node prevNode = route.get(i-1);
                Node actNode = route.get(i);
                g.drawLine(prevNode.getX(), prevNode.getY(), actNode.getX(), actNode.getY());
            }
            if(i == (route.size() - 1)){
                Node actNode = route.get(i);
                g.drawLine(actNode.getX(), actNode.getY(), destination.getX(), destination.getY());
            }
        }

        g.fillOval(source.getX() - source.getRadius(), source.getY() - source.getRadius(), 2*source.getRadius(), 2*source.getRadius());
        g.fillOval(destination.getX() - destination.getRadius(), destination.getY() - destination.getRadius(), 2*destination.getRadius(), 2*destination.getRadius());


    }

   
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(map, 0, 0, this);
        g.drawImage(logo, 565, 0, 250, 250, this);
        //paintLinks(g);
        paintShortestPath(g);
        paintPassengers(g, passengers);
    }
}
