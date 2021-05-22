package app.classes;

import java.util.List;

public class Fossil {
    private List<String> blocked_descriptions;
    private List<String> descriptions;
    private String name;
    private List<modif> negative_mod_weights;
    private List<modif> positive_mod_weights;
    private float cost;
    private String imagePath;


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Fossil{" +
                "blocked_descriptions=" + blocked_descriptions +
                ", descriptions=" + descriptions +
                ", name='" + name + '\'' +
                ", negative_mod_weights=" + negative_mod_weights +
                ", positive_mod_weights=" + positive_mod_weights +
                ", cost=" + cost +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    public List<String> getBlocked_descriptions() {
        return blocked_descriptions;
    }

    public void setBlocked_descriptions(List<String> blocked_descriptions) {
        this.blocked_descriptions = blocked_descriptions;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<modif> getNegative_mod_weights() {
        return negative_mod_weights;
    }

    public void setNegative_mod_weights(List<modif> negative_mod_weights) {
        this.negative_mod_weights = negative_mod_weights;
    }

    public List<modif> getPositive_mod_weights() {
        return positive_mod_weights;
    }

    public void setPositive_mod_weights(List<modif> positive_mod_weights) {
        this.positive_mod_weights = positive_mod_weights;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Fossil(List<String> blocked_descriptions, List<String> descriptions, String name, List<modif> negative_mod_weights, List<modif> positive_mod_weights, float cost, String imagePath) {
        this.blocked_descriptions = blocked_descriptions;
        this.descriptions = descriptions;
        this.name = name;
        this.negative_mod_weights = negative_mod_weights;
        this.positive_mod_weights = positive_mod_weights;
        this.cost = cost;
        this.imagePath = imagePath;
    }
}


