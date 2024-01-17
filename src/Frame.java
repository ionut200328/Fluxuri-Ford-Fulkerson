import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Frame extends JPanel{
    private static Vector<Node> nodes;
    private static Vector<Edge> edges;
    static Vector<Integer> path = new Vector<>();
    private int nodeNr = 1;
    private int radacina=-1;

    public static Vector<Edge> getEdges() {
        return edges;
    }

    public static Vector<Node> getNodes() {
        return nodes;
    }
    private static int node_diam = 30;
    static int getNode_diam() {
        return node_diam;
    }
    private Point pointStart = null;
    private Point pointEnd = null;
    private boolean isDragging = false;

    private static Node startNode = null;
    private static Node endNode = null;

    private static Vector<Color> colors;
    public Frame() {
        nodes = new Vector<Node>();
        edges = new Vector<Edge>();
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.addMouseListener(new MouseAdapter() {
            //double click to change color
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                if (e.getClickCount() == 2) {
                    Node selectedNode = null;
                    for (Node n : nodes) {
                        if (n.isInside(e.getPoint())) {
                            selectedNode = n;
                            break;
                        }
                    }
                    if(selectedNode!=null)
                    {
                        if(startNode==null)
                        {
                            startNode=selectedNode;
                            startNode.setColor(Color.RED);
                            repaint();
                        }
                        else if(endNode==null)
                        {
                            endNode=selectedNode;
                            if(endNode!=startNode) {
                                endNode.setColor(Color.RED);
                                Vector<Edge> fEdges = FordFulkerson.fordFulkerson(edges, startNode, endNode, nodes.size());
                                //update edges based on ford fulkerson
                                for (Edge e1 : edges) {
                                    for (Edge e2 : fEdges) {
                                        if (e1.getStart().getID() == e2.getStart().getID() && e1.getEnd().getID() == e2.getEnd().getID()) {
                                            e1.setWeight(e2.getWeight());
                                        }
                                    }
                                }
                                //delete edges that do not appear in ford fulkerson
                                Vector<Edge> toDelete = new Vector<>();
                                for (Edge e1 : edges) {
                                    boolean exists = false;
                                    for (Edge e2 : fEdges) {
                                        if (e1.getStart().getID() == e2.getStart().getID() && e1.getEnd().getID() == e2.getEnd().getID()) {
                                            exists = true;
                                            break;
                                        }
                                    }
                                    if (!exists) {
                                        toDelete.add(e1);
                                    }
                                }
                                for (Edge e1 : toDelete) {
                                    edges.remove(e1);
                                }
                                repaint();
                            }
                        }

                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                pointStart = e.getPoint();
                isDragging = true;
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                Vector<Vector<Integer>>adjencyMatrix=new Vector<Vector<Integer>>();
                pointEnd = e.getPoint();
                if(pointStart.equals(pointEnd)) {
                    Node newNode = new Node(pointStart, node_diam, nodeNr);
                    for (Node n : nodes) {
                        if (n.isInside(newNode.center, node_diam)) {
                            return;
                        }
                    }
                    nodes.add(newNode);
                    adjencyMatrix=AdjencyMatrix();



                    FilePrint filePrint=new FilePrint("src/AdjencyMatrix.txt");
                    filePrint.fileWriteM(adjencyMatrix);
                    nodeNr++;
                    isDragging = false;
                    repaint();
                    return;
                }
                isDragging = false;
                Node n1 = null;
                Node n2 = null;
                for (Node n : nodes) {
                    if (n.isInside(pointStart)) {
                        n1 = n;
                    }
                    if (n.isInside(pointEnd)) {
                        n2 = n;
                    }
                }
                if (n1 != null && n2 != null) {
                    Edge newEdge = new Edge(n1, n2);
                    boolean alreadyExists = false;
                    for (Edge e1 : edges) {
                            if ((e1.getStart().equals(newEdge.getStart()) && e1.getEnd().equals(newEdge.getEnd()))) {
                                alreadyExists = true;
                                System.out.println("Edge already exists");
                                break;
                            }
                    }
                    if(!alreadyExists) {
                       //set in matrix 1 at n1 and n2 with set
                        edges.add(newEdge);
                        adjencyMatrix=AdjencyMatrix();
                        //show dialog box for weight
                        String weight=JOptionPane.showInputDialog("Introduceti capacitatea muchiei");
                        newEdge.setWeight(Integer.parseInt(weight));



                        FilePrint filePrint=new FilePrint("src/AdjencyMatrix.txt");
                        filePrint.fileWriteM(adjencyMatrix);
                        filePrint.fileWriteE(edges);
                    }
                }
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                pointEnd = e.getPoint();
                repaint();
            }
        });
        //drag node with it edges attached when mouse is right clicked
        this.addMouseMotionListener(new MouseMotionAdapter() {
            Node draggedNode = null;
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (draggedNode == null) {
                        for (Node n : nodes) {
                            if (n.isInside(e.getPoint())) {
                                draggedNode = n;
                                break;
                            }
                        }
                    } else {
                        for(Node n:nodes){
                            if(n!=draggedNode && n.isInside(e.getPoint(),n.diameter)){
                                return;
                            }
                        }
                        draggedNode.setCenter(e.getPoint());
                        repaint();
                    }
                }
            }


            @Override
            public void mouseMoved(MouseEvent e) {
                draggedNode = null;
            }
        });

        //initializare culori
        colors=new Vector<Color>();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Edge e : edges) {
            e.drawEdge(g);
        }
        for (Node n : nodes) {
                n.drawNode(g);
        }
        if (isDragging) {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
        }
        painted++;
        System.out.println(painted);
    }
    int painted=0;
    static Vector<Vector<Integer>>AdjencyMatrix(){
        Vector<Vector<Integer>>adjencyMatrix=new Vector<Vector<Integer>>();
        for(int i=0;i<nodes.size();i++){
            Vector<Integer>line=new Vector<Integer>();
            for(int j=0;j<nodes.size();j++){
                line.add(0);
            }
            adjencyMatrix.add(line);
        }
        for(Edge e:edges){
            adjencyMatrix.get(e.getStart().getID()-1).set(e.getEnd().getID()-1,1);
            adjencyMatrix.get(e.getEnd().getID()-1).set(e.getStart().getID()-1,1);
        }
        return adjencyMatrix;
    }

    void undo(){
        if(edges.size()>0){
            edges.remove(edges.size()-1);
            Vector<Vector<Integer>>adjencyMatrix=new Vector<Vector<Integer>>();
            adjencyMatrix=AdjencyMatrix();
            FilePrint filePrint=new FilePrint("src/AdjencyMatrix.txt");
            filePrint.fileWriteM(adjencyMatrix);
            filePrint.fileWriteE(edges);
            repaint();
        }
    }


    public static void main(String[] args) {
        //read from file and create nodes and edges without FileRead
        File file = new File("src/edges.txt");
        nodes = new Vector<Node>();
        edges = new Vector<Edge>();
        try {
            java.util.Scanner scanner = new java.util.Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] numbers = line.split(" ");
                int startID = Integer.parseInt(numbers[0]);
                int endID = Integer.parseInt(numbers[1]);
                int weight = Integer.parseInt(numbers[2]);
                Node startNode = new Node(startID);
                Node endNode = new Node(endID);
                Edge edge = new Edge(startNode, endNode, weight);
                edges.add(edge);
                boolean exists = false;
                for(Node n: nodes)
                {
                    if(n.getID()==startID || n.getID()==endID)
                    {
                        exists=true;
                        break;
                    }
                }
                if(!exists)
                {
                    nodes.add(startNode);
                    nodes.add(endNode);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Frame.startNode = nodes.get(0);
        Frame.endNode = nodes.get(nodes.size() - 1);

        Vector<Edge>fEdges=FordFulkerson.fordFulkerson(edges,Frame.startNode,Frame.endNode,nodes.size());
        //print edges
        for(Edge e:fEdges){
            System.out.println(e.getStart().getID()+" - "+e.getEnd().getID()+": "+e.getWeight());
        }
    }
}

