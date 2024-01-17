import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class FilePrint {
    private File file;
    public void fileWriteM(Vector<Vector<Integer>> adjencyMatrix) {
        file = new File("src/adjencyMatrix.txt");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (int i = 0; i < adjencyMatrix.size(); i++) {
                for (int j = 0; j < adjencyMatrix.size(); j++) {
                    printWriter.print(adjencyMatrix.get(i).get(j) + " ");
                }
                printWriter.println();
            }
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public void fileWriteE(Vector<Edge> edges) {
        file = new File("src/edges.txt");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (int i = 0; i < edges.size(); i++) {
                printWriter.println(edges.get(i).getStart().getID() + " " + edges.get(i).getEnd().getID() + " " + edges.get(i).getWeight());
            }
            printWriter.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public FilePrint(String path) {
        file = new File(path);
    }
    public FilePrint() {

    }

    public Vector<Vector<Integer>> FileReadMatrix() {
        file = new File("src/adjencyMatrix.txt");
        Vector<Vector<Integer>> adjencyMatrix = new Vector<Vector<Integer>>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] numbers = line.split(" ");
                Vector<Integer> lineVector = new Vector<Integer>();
                for (int i = 0; i < numbers.length; i++) {
                    lineVector.add(Integer.parseInt(numbers[i]));
                }
                adjencyMatrix.add(lineVector);
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return adjencyMatrix;
    }

    public Vector<Edge> FileReadEdges() {
        file = new File("src/edges.txt");
        Vector<Edge> edges = new Vector<Edge>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] numbers = line.split(" ");
                edges.add(new Edge(new Node(Integer.parseInt(numbers[0])), new Node(Integer.parseInt(numbers[1])), Integer.parseInt(numbers[2])));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return edges;
    }

}
