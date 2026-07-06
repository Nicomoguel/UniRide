import java.util.LinkedList;
import java.io.BufferedReader;
import java.nio.file.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.Exception.*;

public class ReadRemoveAdjacents{
    public static Graph removeAdjacents(Graph graph, String name){
        try{
            Path path = FileSystems.getDefault().getPath(name);
            BufferedReader br = Files.newBufferedReader(path);
            String linea;
            LinkedList<Node> vertices = graph.getVertices();
            while((linea = br.readLine()) != null){
                int split = linea.indexOf(" ");
                String s1 = linea.substring(0, split);
                String s2 = linea.substring(split + 1, linea.length());
                int x = Integer.parseInt(s1);
                int y = Integer.parseInt(s2);
                Node source = vertices.get(x);
                Node other = vertices.get(y);
                source.removeDestination(source, other);
            }
            br.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Archivo de enlaces no encontrado");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
