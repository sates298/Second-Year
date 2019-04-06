package pl.swozniak.graph;

public class WeightedEdge {
    private int u;
    private int v;
    private double w;

    public WeightedEdge(int u, int v, double w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public double getW() {
        return w;
    }

    public void setU(int u) {
        this.u = u;
    }

    public void setV(int v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "(" + u + " --{" + w +
                "}-- " + v + ')';
    }
}
