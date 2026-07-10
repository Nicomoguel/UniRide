import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Clase encargada de mostrar la aplicación principal, inicializar el grafo, nodos, realizar los algoritmos
 * y mostrar toda la información y funcionalidades relevantes
 * @author HoodCodeDepartment
 */
public class GUI extends JPanel implements ActionListener{

    private BufferedImage map;
    private BufferedImage logo;
    private Graph graph = new Graph();
    private List<Node> listNodes;
    private Node[] nodes;
    private User user;
    private JButton logoutButton = new JButton("Cerrar sesión");
    private JFrame frame = new JFrame();
    private ArrayList<Passenger> passengers;
    private MatchMaker matchmaker;

    // Ruta del conductor (calculada una sola vez con Dijkstra)
    private List<Node> driverRoute = new ArrayList<>();
    private Node routeSource;
    private Node routeDestination;

    // Resultados del matchmaking (solo cuando el usuario es Driver)
    private List<MatchResult> matchResults = new ArrayList<>();


    /**
     * Constructor de GUI, muestra la información del usuatio, asigna atributos privados, luego de eso
     * llama a metodos de otras clases para agregar los nodos al grafo junto con sus adyacencias, inicializa el matchmaking solo si el usuario 
     * que inició sesión es Driver y lee las imagenes necesarias
     * @param user Usuario con el cual iniciamos sesion
     * @param passengers Lista de posibles pasajeros mandada por el database manager
     * @param frame nuestro Frame principal sobre el cual se implementa todo el front end
     */
    public GUI(User user, ArrayList<Passenger> passengers,JFrame frame){
        this.user = user;
        this.frame = frame;
        this.passengers = passengers;
        this.setLayout(null);
        userPanel();
        logoutPanel();
        ReadNodes.readDoc(graph, "coordenadas.txt");
        AddAllAdjacents.add(graph);
        ReadAddColumns.addCols(graph, "columnas.txt");
        ReadRemoveAdjacents.removeAdjacents(graph, "RemoveNodes.txt");
        this.listNodes = graph.getVertices();
        this.nodes = this.listNodes.toArray(new Node[0]);
        computeRoute();
        if(user instanceof Driver){
            runMatchmaking();
            resultsTable();
        }
        try{
            map = ImageIO.read(new File("mapa3.png"));
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

    /**
     * Método privado que asigna los valores necesarios a cada JPanel para mostrar los datos del usuario, como las coordenadas del panel
     * tamaño, fuente, y asigna informacion llamando a los getters del usuario, mostrando informacion distinta dependiendo de si es Driver o Passenger
     */
    private void userPanel(){
        int x = 585;
        JLabel heading = new JLabel("Datos del usuario");
        heading.setFont(new Font("SansSerif", Font.BOLD, 17));
        heading.setBounds(x, 140, 230, 26);
        this.add(heading);

        java.util.List<String> info = new java.util.ArrayList<>();
        info.add("Tipo: " + (user instanceof Driver ? "Conductor" : "Pasajero"));
        info.add("ID: " + user.getStudentId());
        info.add("IDMEX: " + user.getIDMEX());
        info.add("Edad: " + user.getAge());
        info.add("Tolerancia: " + user.getTolerance() + " min");
        info.add("Puntos: " + user.getUserPoints());
        Schedule sch = user.getSchedule();
        info.add("Horario: " + sch.getArrival() + " - " + sch.getDeparture());
        if(user instanceof Driver d){
            info.add("Licencia: " + (d.isLicenseValid() ? "Sí" : "No"));
            info.add("Desviación máx: " + d.getDesviation());
        }

        Font f = new Font("SansSerif", Font.PLAIN, 15);
        int y = 172, dy = 28;
        for(String s : info){
            JLabel lbl = new JLabel(s);
            lbl.setFont(f);
            lbl.setBounds(x, y, 230, 24);
            this.add(lbl);
            y += dy;
        }
        logoutButton.setBounds(x, y + 10, 150, 34);
    }
    /**
     * Método privado que asigna todos los atributos al boton de cerrar sesión
     */
    private void logoutPanel(){
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(false);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(this);
        this.add(logoutButton);
    }
    /**
     * Método que valida si la accion del mouse fue dentro de el botón de cerrar sesión para volver al inicio de sesión
     */
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == logoutButton){
            frame.dispose();
            new LoginPage();
        }
    }
        
    /**
     * Método que calcula la ruta mas corta entre el nodo de inicio del conductor y su destino utilizando el algoritmo de Dijkstra
     * Esta ruta solo la asigna si el usuario que inicio sesión es un Driver, ya que en base a esta ruta se podra determinar si los Passengers hacen match o no
     */
    private void computeRoute(){
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
        List<Node> route = new ArrayList<>(destination.getShortestPath());
        route.add(destination);
        this.driverRoute = route;
        this.routeSource = source;
        this.routeDestination = destination;
        if(user instanceof Driver){
            ((Driver) user).setRoute(route);
        }
    }

    /**
     * Método que inicializa el matchmaking y llama al método que realiza el matchmaking para todos los usuarios de la aplicación
     */
    private void runMatchmaking(){
        Driver driver = (Driver) user;
        matchmaker = new MatchMaker(driver.getDesviation());
        matchResults = matchmaker.evaluateAll(driver, passengers);
    }

    // Construye la tabla de resultados con etiquetas y setBounds (igual que la info del usuario)
    /**
     *  Inicializa la tabla con la informacion de los Passengers, después de hacer el matchmaking para mostrar la información y si hubo match o no
     */
    private void resultsTable(){
        Font headFont = new Font("SansSerif", Font.BOLD, 14);
        Font rowFont = new Font("SansSerif", Font.PLAIN, 14);
        int xId = 30, xDist = 180, xHor = 330, xRes = 470;
        int y = 480, dy = 28;

        // Encabezados de las columnas
        addLabel("Pasajero", xId, y, 140, headFont);
        addLabel("Distancia", xDist, y, 140, headFont);
        addLabel("Horario", xHor, y, 140, headFont);
        addLabel("Resultado", xRes, y, 260, headFont);

        // Una fila por cada pasajero evaluado
        for(int i = 0; i < matchResults.size(); i++){
            MatchResult r = matchResults.get(i);
            y = y + dy;

            String distancia;
            if(r.status() == MatchStatus.REJECTED_SCHEDULE){
                distancia = "-";
            } else {
                distancia = (int) r.getDistanceMeters() + " m";
            }

            String resultado;
            if(r.status() == MatchStatus.MATCH){
                resultado = "MATCH";
            } else if(r.status() == MatchStatus.REJECTED_DISTANCE){
                resultado = "RECHAZADO (distancia)";
            } else {
                resultado = "RECHAZADO (horario)";
            }

            addLabel(r.getPassenger().getStudentId(), xId, y, 140, rowFont);
            addLabel(distancia, xDist, y, 140, rowFont);
            addLabel(r.getMinutesDifference() + " min", xHor, y, 140, rowFont);
            addLabel(resultado, xRes, y, 260, rowFont);
        }

        // Leyenda
        addLabel("Verde = Pasajero    Azul = Ruta del conductor    MATCH = compatible    RECHAZADO = no compatible", xId, y + dy + 6, 760, rowFont);
    }

    /** Método que crea una etiqueta y la coloca en (x, y), igual que en userPanel
     */
    private void addLabel(String text, int x, int y, int w, Font font){
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setBounds(x, y, w, 24);
        this.add(lbl);
    }

    /**
     * Método que pinta en el mapa el nodo en donde se encuentra cada Passenger
     * @param g El componente gráfico 
     * @param passengers El conjunto de pasajeros a dibujar
     */
    private void paintPassengers(Graphics g, ArrayList<Passenger> passengers){
        Color myColor = new Color(37, 161, 74);
        for(Passenger passenger : passengers){
            Node node = passenger.getSource();
            g.setColor(myColor);
            g.fillOval(node.getX() - node.getRadius(), node.getY() - node.getRadius(), 2*node.getRadius(), 2*node.getRadius());
            drawNodeLabel(g, node, passenger.getStudentId());
        }
        g.setColor(Color.BLACK);
    }

    /** Método que dibuja una cadena de caracteres debajo de cada nodo mostrado
     *  Utiliza java.awt.FontMetrics para conseguir el alto y ancho de la cadena que vamos a dibujar
     *  @param g El componente gráfico
     *  @param node El nodo de referencia
     *  @param text La cadena de caracteres que se quiere mostrar
     *
     */
    private void drawNodeLabel(Graphics g, Node node, String text){
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        java.awt.FontMetrics fm = g.getFontMetrics();
        int tx = node.getX() - fm.stringWidth(text) / 2;
        int ty = node.getY() + node.getRadius() + 12;
        g.setColor(Color.BLACK);
        g.drawString(text, tx, ty);
    }





    /**
     * Método que dibuja las líneas de la tabla de resultados usando solo g.drawLine.
     * Dibuja una línea horizontal por cada fila (encabezado + pasajeros) y una
     * línea vertical en el límite de cada columna.
     * @param g El componente gráfico
     */
    private void drawTableLines(Graphics g){
        int left = 20, right = 740;   // extremos horizontales de la tabla
        int top = 476, dy = 28;       // borde superior y alto de cada fila
        int rows = matchResults.size();          // filas de pasajeros
        int bottom = top + (rows + 1) * dy;      
        g.setColor(Color.BLACK);

        // Líneas horizontales
        for(int y = top; y <= bottom; y += dy){
            g.drawLine(left, y, right, y);
        }

        // Líneas verticales
        int[] cols = {20, 170, 320, 460, 740};
        for(int x : cols){
            g.drawLine(x, top, x, bottom);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,430);
    }

    /** Método que dibuja la ruta del conductor ya calculada (azul), y sus extremos
     *  Itera sobre la ruta mas corta de nodos y dibuja un enlace entre ellos
     *  Utiliza un color personalizado con parametros enteros (RGB)
     *  Rellena los nodos de origen y destino
     *  @param g El componente gráfico
     */
    private void paintShortestPath(Graphics g){
        if(driverRoute == null || driverRoute.isEmpty()) return;
        Color routeColor = new Color(66, 133, 244);
        g.setColor(routeColor);
        for(int i = 1; i < driverRoute.size(); i++){
            Node prev = driverRoute.get(i-1);
            Node act = driverRoute.get(i);
            g.drawLine(prev.getX(), prev.getY(), act.getX(), act.getY());
        }
        if(routeSource != null){
            g.setColor(routeColor);
            g.fillOval(routeSource.getX() - routeSource.getRadius(), routeSource.getY() - routeSource.getRadius(), 2*routeSource.getRadius(), 2*routeSource.getRadius());
            drawNodeLabel(g, routeSource, "Inicio");
        }
        if(routeDestination != null){
            g.setColor(routeColor);
            g.fillOval(routeDestination.getX() - routeDestination.getRadius(), routeDestination.getY() - routeDestination.getRadius(), 2*routeDestination.getRadius(), 2*routeDestination.getRadius());
            drawNodeLabel(g, routeDestination, "Destino");
        }
        g.setColor(Color.BLACK);
    }

    /**
     *  Override al método paintComponent que llama a todos los métodos que requieren el componente gráfico
     *  @param g El componente gráfico
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(map, 0, 0, this);
        g.drawImage(logo, 585, 5, 130, 130, this);
        paintShortestPath(g);
        paintPassengers(g, passengers);
        if(user instanceof Driver){
            drawTableLines(g);
        }
        g.setColor(Color.WHITE);
        // Para el conductor: tapa la parte baja del mapa para que la tabla tenga fondo limpio
        /*
        if(user instanceof Driver){
            g.setColor(Color.WHITE);
            g.fillRect(0, 455, getWidth(), getHeight() - 455);
        }
        */
    }
}
