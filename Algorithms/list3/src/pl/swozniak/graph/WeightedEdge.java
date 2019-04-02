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

    @Override
    public String toString() {
        return "(" + u + " --{" + w +
                "}-- " + v + ')';
    }
}
