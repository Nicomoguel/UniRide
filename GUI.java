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
    //private Node nodeD = new Node("D", 500, 200);
    //private Node nodeE = new Node("E", 350, 450);
    //private Node nodeF = new Node("F", 300, 250);
    private Node nodeG = new Node("G", 417, 66);
    private Node nodeH = new Node("H", 433, 68);
    private Node nodeI = new Node("I", 452, 72);
    private Node nodeJ = new Node("J", 486, 75);
    private Node nodeK = new Node("K", 537, 74);

    private Node nodeL = new Node("L", 227, 11);
    private Node nodeM = new Node("M", 273, 29);
    private Node nodeN = new Node("N", 318, 48);
    private Node nodeO = new Node("O", 365, 67);
    private Node nodeP = new Node("P", 410, 83);
    private Node nodeQ = new Node("Q", 462, 95);
    private Node nodeR = new Node("R", 474, 95);
    private Node nodeS = new Node("S", 489, 94);
    private Node nodeT = new Node("T", 508, 93);
    private Node nodeU = new Node("U", 523, 93);
    private Node nodeV = new Node("V", 540, 93);

    private Node nodeW = new Node("W", 221, 28);
    private Node nodeX = new Node("X", 269, 46);
    private Node nodeY = new Node("Y", 315, 65);
    private Node nodeZ = new Node("Z", 360, 82);
    private Node nodeA1 = new Node("A1", 404, 98);


    private Node nodeA2 = new Node("A2", 214, 42);
    private Node nodeA3 = new Node("A3", 263, 60);
    private Node nodeA4 = new Node("A4", 307, 77);
    private Node nodeA5 = new Node("A5", 354, 95);
    private Node nodeA6 = new Node("A6", 398, 112);
    private Node nodeA7 = new Node("A7", 506, 120);
    private Node nodeA8 = new Node("A8", 524, 122);

    private Node nodeA9 = new Node("A9", 209, 56);
    private Node nodeB1 = new Node("B1", 258, 74);
    private Node nodeB2 = new Node("B2", 303, 93);
    private Node nodeB3 = new Node("B3", 392, 126);
    private Node nodeB4 = new Node("B4", 428, 142);
    private Node nodeB5 = new Node("B5", 433, 133);
    private Node nodeB6 = new Node("B6", 445, 137);
    private Node nodeB7 = new Node("B7", 472, 137);
    private Node nodeB8 = new Node("B8", 490, 139);
    private Node nodeB9 = new Node("B9", 540, 140);


    private Node nodeC1 = new Node("C1", 203, 71);
    private Node nodeC2 = new Node("C2", 252, 90);
    private Node nodeC3 = new Node("C3", 298, 107);
    private Node nodeC4 = new Node("C4", 345, 125);
    private Node nodeC5 = new Node("C5", 387, 142);
    private Node nodeC6 = new Node("C6", 442, 154);
    private Node nodeC7 = new Node("C7", 490, 156);
    private Node nodeC8 = new Node("C8", 539, 158);
    private Node nodeC9 = new Node("C9", 410, 132);

    private Node nodeD1 = new Node("D1", 198, 87);
    private Node nodeD2 = new Node("D2", 247, 105);
    private Node nodeD3 = new Node("D3", 294, 121);
    private Node nodeD4 = new Node("D4", 340, 141);
    private Node nodeD5 = new Node("D5", 418, 171);
    private Node nodeD6 = new Node("D6", 441, 170);
    private Node nodeD7 = new Node("D7", 486, 172);
    private Node nodeD8 = new Node("D8", 538, 175);
    private Node nodeD9 = new Node("D9", 682, 228);
    private Node nodeD10 = new Node("D10", 1172, 400);

    


//    private Node nodeD1 = new

    private Node[] nodes = {nodeA,nodeB,nodeC,nodeG,nodeH,nodeI,nodeJ,nodeK,
        nodeL,nodeM,nodeN,nodeO,nodeP,nodeQ,nodeR,nodeS,nodeT,nodeU,nodeV,
        nodeW,nodeX,nodeY,nodeZ,nodeA1,
        nodeA2,nodeA3,nodeA4,nodeA5,nodeA6,nodeA7,nodeA8,
        nodeA9,nodeB1,nodeB2,nodeB3,nodeB4,nodeB5,nodeB6,nodeB7,nodeB8,nodeB9,
        nodeC1,nodeC2,nodeC3,nodeC4,nodeC5,nodeC6,nodeC7,nodeC8, nodeC9, nodeD1, nodeD2, nodeD3, nodeD4, nodeD5, nodeD6, nodeD7, nodeD8, nodeD9, nodeD10
    };
    Graph graph = new Graph(nodes);


    public GUI(){
/*
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
        graph.addNode(nodeG);
        graph.addNode(nodeH);
        graph.addNode(nodeI);
        graph.addNode(nodeJ);
        graph.addNode(nodeK); 

        graph.addNode(nodeL); 
        graph.addNode(nodeM); 
        graph.addNode(nodeN); 
        graph.addNode(nodeO); 
        graph.addNode(nodeP); 
        graph.addNode(nodeQ); 
        graph.addNode(nodeR); 
        graph.addNode(nodeS); 
        graph.addNode(nodeT); 
        graph.addNode(nodeU); 
        graph.addNode(nodeV); 

        graph.addNode(nodeW); 
        graph.addNode(nodeX); 
        graph.addNode(nodeY); 
        graph.addNode(nodeZ); 
        graph.addNode(nodeA1); 

        graph.addNode(nodeA2); 
        graph.addNode(nodeA3); 
        graph.addNode(nodeA4); 
        graph.addNode(nodeA5); 
        graph.addNode(nodeA6); 
        graph.addNode(nodeA7); 
        graph.addNode(nodeA8); 

        graph.addNode(nodeA9); 
        graph.addNode(nodeB1); 
        graph.addNode(nodeB2); 
        graph.addNode(nodeB3); 
        graph.addNode(nodeB4); 
        graph.addNode(nodeB5); 
        graph.addNode(nodeB6); 
        graph.addNode(nodeB7); 
        graph.addNode(nodeB8); 
        graph.addNode(nodeB9); 

        graph.addNode(nodeC1); 
        graph.addNode(nodeC2); 
        graph.addNode(nodeC3); 
        graph.addNode(nodeC4); 
        graph.addNode(nodeC5); 
        graph.addNode(nodeC6); 
        graph.addNode(nodeC7); 
        graph.addNode(nodeC8); 
        */

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
       // paintLinks(g);
       // paintShortestPath(g);
    }
}
