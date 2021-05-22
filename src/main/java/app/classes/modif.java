package app.classes;

public class modif {
    private String tag;
    private double weight;

    public modif(String tag, double weight) {
        this.tag = tag;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "modif{" +
                "tag='" + tag + '\'' +
                ", weight=" + weight +
                '}';
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
