package app.classes;

public class MethodToOut implements Cloneable{
    private String name;
    private Integer Tries;
    private float cost;

    @Override
    public MethodToOut clone() throws CloneNotSupportedException {
        return (MethodToOut) super.clone();
    }

    @Override
    public String toString() {
        return "MethodToOut{" +
                "name='" + name + '\'' +
                ", Tries=" + Tries +
                ", cost=" + cost +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTries() {
        return Tries;
    }

    public void setTries(int tries) {
        Tries = tries;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public MethodToOut(String name, int tries, float cost) {
        this.name = name;
        Tries = tries;
        this.cost = cost;
    }

//    @Override
//    public int compareTo(MethodToOut o) {
//        int result = this.Tries.compareTo(o.Tries);
//        return result;
//    }
}
