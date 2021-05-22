package app.classes;

public class Stats {
    String id;
    int max;
    int min;

    public Stats(String id, int max, int min) {
        this.id = id;
        this.max = max;
        this.min = min;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "id='" + id + '\'' +
                ", max=" + max +
                ", min=" + min +
                '}';
    }
}
