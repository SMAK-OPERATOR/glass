package app.classes;

import java.util.List;


public class Mod {
    private final int weight;
    private final String name;
    private final List<Stats> stats;
    private final List<String> tags;
    private final String generation_type;
    private final String group;
    private final int iLvl;
    private final String influence;

    public Mod(int weight, String name, List<Stats> stats, List<String> tags, String generation_type, String group, int iLvl, String influence) {
        this.weight = weight;
        this.name = name;
        this.stats = stats;
        this.tags = tags;
        this.generation_type = generation_type;
        this.group = group;
        this.iLvl = iLvl;
        this.influence = influence;
    }

    @Override
    public String toString() {
        return "Mod{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                ", stats=" + stats +
                ", tags=" + tags +
                ", generation_type='" + generation_type + '\'' +
                ", group='" + group + '\'' +
                ", iLvl=" + iLvl +
                ", influence='" + influence + '\'' +
                '}';
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getGeneration_type() {
        return generation_type;
    }

    public String getGroup() {
        return group;
    }

    public int getiLvl() {
        return iLvl;
    }

    public String getInfluence() {
        return influence;
    }

}



