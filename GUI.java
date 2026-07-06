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

    // Panel lateral derecho: logo (arriba, dibujado en paintComponent) + datos del usuario debajo
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
        // El botón de cerrar sesión queda debajo de la info del usuario
        logoutButton.setBounds(x, y + 10, 150, 34);
    }

    // Botón rojo para cerrar sesión: cierra esta ventana y abre un LoginPage nuevo
    private void logoutPanel(){
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusable(false);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(this);
        this.add(logoutButton);
    }

    // Igual que en LoginPage: aquí se maneja el clic del botón "Cerrar sesión"
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == logoutButton){
            frame.dispose();
            new LoginPage();
        }
    }

    // Calcula la ruta más corta source->destination y se la asigna al Driver
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

    // Evalúa a todos los pasajeros contra el conductor
    private void runMatchmaking(){
        Driver driver = (Driver) user;
        matchmaker = new MatchMaker(driver.getDesviation());
        matchResults = matchmaker.evaluateAll(driver, passengers);
    }

    // Construye la tabla de resultados con etiquetas y setBounds (igual que la info del usuario)
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

    // Crea una etiqueta y la coloca en (x, y), igual que en userPanel
    private void addLabel(String text, int x, int y, int w, Font font){
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setBounds(x, y, w, 24);
        this.add(lbl);
    }

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

    // Dibuja un texto pequeño centrado debajo de un nodo
    private void drawNodeLabel(Graphics g, Node node, String text){
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        java.awt.FontMetrics fm = g.getFontMetrics();
        int tx = node.getX() - fm.stringWidth(text) / 2;
        int ty = node.getY() + node.getRadius() + 12;
        g.setColor(Color.BLACK);
        g.drawString(text, tx, ty);
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
        return new Dimension(800,430);
    }

    // Dibuja la ruta del conductor ya calculada (azul), y sus extremos
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


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(map, 0, 0, this);
        g.drawImage(logo, 585, 5, 130, 130, this);
        //paintLinks(g);
        paintShortestPath(g);
        paintPassengers(g, passengers);
        // Para el conductor: tapa la parte baja del mapa para que la tabla tenga fondo limpio
        if(user instanceof Driver){
            g.setColor(Color.WHITE);
            g.fillRect(0, 455, getWidth(), getHeight() - 455);
        }
    }
}
