import java.io.BufferedReader;
import java.nio.file.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.Exception.*;

public class ReadNodes{
    public static Graph readDoc(Graph graph, String name){
        try{
            Path path = FileSystems.getDefault().getPath(name);
            eredReader br = Files.newBufferedReader(path);
            String linea;
            while((linea = br.readLine()) != null){
                int split = linea.indexOf(" ");
                String s1 = linea.substring(0, split);
                String s2 = linea.substring(split + 1, linea.length());
                int x = Integer.parseInt(s1);
                int y = Integer.parseInt(s2);
                System.out.println(s1 + " " + s2);
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

    public static void main(String[] s){
        readDoc(null, "coordenadas.txt");
    }
}
