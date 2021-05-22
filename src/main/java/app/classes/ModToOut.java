package app.classes;

import java.math.BigDecimal;
import java.util.List;

public class ModToOut implements Cloneable{
    private String Name;
    private int iLvl;
    private int Weight;
    private float prefixChance;
    private float weightChance;
    private String influence;
    private final String AffixType;
    private final String group;
    private final List<String> tags;

    public ModToOut(String name, int iLvl, int weight, String influence, String affixType, String group, List<String> tags) {
        Name = name;
        this.iLvl = iLvl;
        Weight = weight;
        this.prefixChance = prefixChance;
        this.weightChance = weightChance;
        this.influence = influence;
        AffixType = affixType;
        this.group = group;
        this.tags = tags;
    }

    public ModToOut(ModToOut other){
        this.Name = other.getName();
        this.Weight = other.getWeight();
        this.tags = other.getTags();
        this.AffixType = other.getAffixType();
        this.iLvl = other.getiLvl();
        this.prefixChance = other.prefixChance;
        this.weightChance = other.getWeightChance();
        this.influence = other.getInfluence();
        this.group = other.getGroup();
    }

    @Override
    public ModToOut clone() throws CloneNotSupportedException {
        return (ModToOut) super.clone();
    }

    public String getGroup() {
        return group;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getAffixType() {
        return AffixType;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getiLvl() {
        return iLvl;
    }

    public void setiLvl(int iLvl) {
        this.iLvl = iLvl;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public float getPrefixChance() {
        return prefixChance;
    }

    public void setPrefixChance(float prefixChance) {
        this.prefixChance = prefixChance;
    }

    public float getWeightChance() {
        return weightChance;
    }

    public void setWeightChance(float weightChance) {
        this.weightChance = weightChance;
    }

    @Override
    public String toString() {
        return "ModToOut{" +
                "Name='" + Name + '\'' +
                ", iLvl=" + iLvl +
                ", Weight=" + Weight +
                ", prefixChance=" + prefixChance +
                ", weightChance=" + weightChance +
                ", influence='" + influence + '\'' +
                ", AffixType='" + AffixType + '\'' +
                ", group='" + group + '\'' +
                ", tags=" + tags +
                '}';
    }
}

