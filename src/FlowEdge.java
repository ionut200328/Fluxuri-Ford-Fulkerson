public class FlowEdge {
    private Node start;
    private Node end;
    private int capacity;
    private int flow;

    public FlowEdge(Node start, Node end, int capacity) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
        this.flow = 0;
    }
    public FlowEdge() {
        this.start = null;
        this.end = null;
        this.capacity = 0;
        this.flow = 0;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }
// Getter and setter methods
}
