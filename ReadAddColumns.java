
import java.io.BufferedReader;
import java.nio.file.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.Exception.*;
import java.util.*;
public class ReadAddColumns{
    public static Graph addCols(Graph graph, String name){
        try{
            Path path = FileSystems.getDefault().getPath(name);
            BufferedReader br = Files.newBufferedReader(path);
            LinkedList<Node> vertices = graph.getVertices();
            Node[] nodes = vertices.toArray(new Node[0]);
            int index1, index2;
            index1 = index2 = 0;

            String linea = br.readLine();
            String linea1;


            while((linea1 = br.readLine()) != null){
                if(linea.isEmpty() || linea1.isEmpty()) break;
                int split = linea.indexOf(" ");
                String s1 = linea.substring(0, split);
                String s2 = linea.substring(split + 1, linea.length());
                int x1 = Integer.parseInt(s1);
                int y1 = Integer.parseInt(s2);
                split = linea1.indexOf(" ");
                String d1 = linea1.substring(0, split);
                String d2 = linea1.substring(split + 1, linea1.length());
                int x2 = Integer.parseInt(d1);
                int y2 = Integer.parseInt(d2);

                for(int i = 0; i < nodes.length; i++){
                    if(nodes[i].getX() == x1 && nodes[i].getY() == y1) index1 = i;
                    if(nodes[i].getX() == x2 && nodes[i].getY() == y2) index2 = i;

                }
/*
                for(Node node : vertices){
                    if(node.getX() == x1 && node.getY() == y1) index1 = vertices.indexOf(node);
                    if(node.getX() == x2 && node.getY() == y2) index2 = vertices.indexOf(node);
                }
                */

                nodes[index1].addDestination(nodes[index2], 1);
                linea = linea1;
            }
            br.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Archivo de coordenadas no encontrado");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
