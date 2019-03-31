package pl.swozniak.graph;

public class DirectedWeightedEdge {
    private int u;
    private int v;
    private int w;

    public DirectedWeightedEdge(int u, int v, int w) {
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

    public int getW() {
        return w;
    }

    @Override
    public String toString() {
        return "(" + u + " --{" + w +
                "}--> " + v + ')';
    }
}
