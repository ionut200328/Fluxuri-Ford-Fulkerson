import java.awt.*;

public class Edge extends FlowEdge{
    private Node start;
    public Node getStart() {
        return start;
    }
    private Node end;
    public Node getEnd() {
        return end;
    }

    private int weight;
    public int getWeight() {
        return weight;
    }
    public void drawEdge(Graphics g) {
        g.setColor(Color.BLACK);
        if(this.start.center.x==this.end.center.x && this.start.center.y==this.end.center.y)
            g.drawOval(start.center.x - Frame.getNode_diam(), start.center.y - Frame.getNode_diam()+3, Frame.getNode_diam()*2, Frame.getNode_diam());
        else {
            //draw line of tickness 3
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(start.center.x, start.center.y, end.center.x, end.center.y);
            g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));

            //draw string in a middle of the line inside a rectangle
            FontMetrics fontMetrics = g2.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(String.valueOf(weight));
            int stringHeight = fontMetrics.getHeight();
            int x = (start.center.x + end.center.x) / 2 - stringWidth / 2;
            int y = (start.center.y + end.center.y) / 2 + stringHeight / 4; // Adjusted for better vertical centering
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y - stringHeight, stringWidth, stringHeight);
            g2.setColor(Color.RED);
            g2.drawString(String.valueOf(weight), x, y);

            g2.dispose();
        }
        double angle = Math.atan2(this.end.center.y - this.start.center.y, this.end.center.x - this.start.center.x);
        int arrowLength = start.getDiameter()/3;  // You can adjust this value

        // Adjust position of arrowhead based on node radius
        int adjustedEndX = this.end.center.x - (int)(start.getDiameter()/2 * Math.cos(angle));
        int adjustedEndY = this.end.center.y - (int)(start.getDiameter()/2 * Math.sin(angle));

        int xArrow[] = {adjustedEndX, adjustedEndX - (int)(arrowLength * Math.cos(angle + Math.PI/6)), adjustedEndX - (int)(arrowLength * Math.cos(angle - Math.PI/6))};
        int yArrow[] = {adjustedEndY, adjustedEndY - (int)(arrowLength * Math.sin(angle + Math.PI/6)), adjustedEndY - (int)(arrowLength * Math.sin(angle - Math.PI/6))};
        g.fillPolygon(xArrow, yArrow, 3);
    }
    public Edge(Node start, Node end) {
        this.start = start;
        this.end = end;
    }

    public Edge(Node start, Node end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public void setStart(Node node) {
        start = node;
    }

    public void setEnd(Node node) {
        end = node;
    }

    public void setWeight(int i) {
        weight = i;
    }
}
