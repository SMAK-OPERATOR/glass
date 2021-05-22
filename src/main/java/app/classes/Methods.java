package app.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Methods {

    public static double alt(List<ModToOut> WantedMods, float SuffixWeight, float PrefixWeight) {
        if (WantedMods.size() == 2) {
            return WantedMods.get(0).getPrefixChance() / 100 * WantedMods.get(1).getPrefixChance() / 100 * 0.5;
        } else if (WantedMods.size() == 1) {
            if (WantedMods.get(0).getAffixType().equals("prefix"))
                return (WantedMods.get(0).getPrefixChance() / 100 * 0.5) + (WantedMods.get(0).getPrefixChance() / 100 * 0.5 * PrefixWeight);
            else
                return (WantedMods.get(0).getPrefixChance() / 100 * 0.5) + (WantedMods.get(0).getPrefixChance() / 100 * 0.5 * SuffixWeight);
        }
        return -1;
    }

    public static double altaug(List<ModToOut> WantedMods, float SuffixWeight, float PrefixWeight) {

        if (WantedMods.size() == 2) {
            return WantedMods.get(0).getPrefixChance() / 100 * WantedMods.get(1).getPrefixChance() / 100;
        } else if (WantedMods.size() == 1) {
            float s1 = (float) (WantedMods.get(0).getPrefixChance() / 100 * 0.5 * PrefixWeight);
            double s2 = WantedMods.get(0).getPrefixChance() / 100 * 0.5 * SuffixWeight;
            double s3 = WantedMods.get(0).getPrefixChance() / 100 * 0.5;
            return s1 + s2 + s3;
        }
        return -1;

    }

    public static double chaos(List<ModToOut> WantedMods, List<Map<String, Integer>> Groups, float SuffixWeight, float PrefixWeight) {
        List<ModToOut> wantedSuffixes = new ArrayList<>();
        List<ModToOut> wantedPrefixes = new ArrayList<>();
        int prefCount = 0;
        int sufCount = 0;
        double chance = 0;
        for (int i = 0; i != WantedMods.size(); i++) {
            if (WantedMods.get(i).getAffixType().equals("prefix")) {
                wantedPrefixes.add(WantedMods.get(i));
                prefCount++;
            } else {
                wantedSuffixes.add(WantedMods.get(i));
                sufCount++;
            }
        }
        List<String> validCombs = new ArrayList<>();
        validCombs.add("3/3");
        if (!(prefCount == 3 && sufCount == 3)) {
            if (prefCount == 3) {
                if (sufCount == 2)
                    validCombs.add("3/2");
                else if (sufCount == 1 || sufCount == 0) {
                    validCombs.add("3/2");
                    validCombs.add("3/1");
                }

            } else if (prefCount == 2) {
                if (sufCount == 3)
                    validCombs.add("2/3");
                else if (sufCount == 2) {
                    validCombs.add("3/2");
                    validCombs.add("2/2");
                    validCombs.add("2/3");
                } else if (sufCount == 1 || sufCount == 0) {
                    validCombs.add("3/2");
                    validCombs.add("3/1");
                    validCombs.add("2/2");
                    validCombs.add("2/3");
                }
            } else if (prefCount == 1 || prefCount == 0) {
                if (sufCount == 3) {
                    validCombs.add("1/3");
                    validCombs.add("2/3");
                } else if (sufCount == 2) {
                    validCombs.add("1/3");
                    validCombs.add("2/2");
                    validCombs.add("2/3");
                    validCombs.add("3/2");
                } else if (sufCount == 1 || sufCount == 0) {
                    validCombs.add("1/3");
                    validCombs.add("2/3");
                    validCombs.add("2/2");
                    validCombs.add("3/2");
                    validCombs.add("3/1");
                }
            }
        }
//        System.out.println(validCombs);

//        if(Groups.get(0).size()==2 && wantedPrefixes.size() == 2){
//            chance += wantedPrefixes.get(0).getWeight()/PrefixWeight * WantedMods.get(1).getWeight()/(PrefixWeight - Groups.get(0).get(wantedPrefixes.get(0).getGroup()));
//            chance += wantedPrefixes.get(1).getWeight()/PrefixWeight * WantedMods.get(0).getWeight()/(PrefixWeight - Groups.get(0).get(wantedPrefixes.get(1).getGroup()));
//
//        }else

        for (int i = 0; i != validCombs.size(); i++) {
            int prockol = 0;
//            System.out.println(validCombs.get(i));
            char[] chars = validCombs.get(i).toCharArray();
            if (Integer.parseInt(String.valueOf(chars[0])) > Groups.get(0).size()) {
                prockol = 1;
            }
            double prefChance = 0;
            double sufChance = 0;
            if(wantedPrefixes.size() == 1 && Groups.get(0).size() == 1){
                prefChance = wantedPrefixes.get(0).getWeight()/PrefixWeight;
            }
            else if (prefCount != 0) {
//                System.out.println(Integer.parseInt(String.valueOf(chars[0])));
//                System.out.println(wantedPrefixes.size());
                prefChance += affixCalc(Integer.parseInt(String.valueOf(chars[0]))-prockol, wantedPrefixes, Groups.get(0), PrefixWeight);
            } else prefChance = 1;

            if (sufCount != 0) {
                sufChance += affixCalc(Integer.parseInt(String.valueOf(chars[2])), wantedSuffixes, Groups.get(1), SuffixWeight);

            } else sufChance = 1;
//            System.out.println("PrefChance = " + prefChance);
//            System.out.println("SuffixChance  = " + sufChance);
//            System.out.println(spr(SuffixWeight, PrefixWeight, Integer.parseInt(String.valueOf(chars[0])), Integer.parseInt(String.valueOf(chars[2])),Groups.get(0)));

            chance += prefChance * sufChance * spr(SuffixWeight, PrefixWeight, Integer.parseInt(String.valueOf(chars[0])), Integer.parseInt(String.valueOf(chars[2])),Groups.get(0));

//            System.out.println(chance);
        }
        return chance;

    }


    public static double affixCalc(int aAf, List<ModToOut> WantedMods, Map<String, Integer> Groups, float AffixesWeight) {
        double chance = 0;

        if (aAf == 1) {
            return WantedMods.get(0).getWeight() / AffixesWeight;
        }
        if (aAf == 2 && WantedMods.size() == 2) {
            chance += WantedMods.get(0).getWeight() / AffixesWeight * (WantedMods.get(1).getWeight() / (AffixesWeight - Groups.get(WantedMods.get(0).getGroup())));
            chance += WantedMods.get(1).getWeight() / AffixesWeight * (WantedMods.get(0).getWeight() / (AffixesWeight - Groups.get(WantedMods.get(1).getGroup())));
            return chance;
        }
        List<String> keys = new ArrayList<>(Groups.keySet());
        if (aAf == 2 && WantedMods.size() == 1) {
            for (int i = 0; i != keys.size(); i++) {
                if (!(keys.get(i).equals(WantedMods.get(0).getGroup()))) {
                    chance += WantedMods.get(0).getWeight() / AffixesWeight * (Groups.get(keys.get(i)) / (AffixesWeight - Groups.get(WantedMods.get(0).getGroup())));
                    chance += Groups.get(keys.get(i)) / AffixesWeight * (WantedMods.get(0).getWeight() / (AffixesWeight - Groups.get(keys.get(i))));
                }
            }
            return chance;
        }
        if (aAf == 3 && WantedMods.size() == 1) {
            int modWeight = WantedMods.get(0).getWeight();
            int counter = 0;
            for (int i = 0; i != keys.size(); i++) {
                int iWeight = Groups.get(keys.get(i));

                for (int j = i; j != keys.size(); j++) {
                    if (!(keys.get(i).equals(WantedMods.get(0).getGroup())) && !(keys.get(j).equals(WantedMods.get(0).getGroup())) && !(keys.get(i).equals(keys.get(j)))) {
                        counter++;
                        int jWeight = Groups.get(keys.get(j));
                        chance += triChance(modWeight, iWeight, jWeight, AffixesWeight, Groups.get(WantedMods.get(0).getGroup()), iWeight, jWeight);
                    }
                }
            }
//            System.out.println(counter);
            return chance;
        }
        if (aAf == 3 && WantedMods.size() == 2) {
            int modWeight1 = WantedMods.get(0).getWeight();
            int modWeight2 = WantedMods.get(1).getWeight();
            for (int i = 0; i != keys.size(); i++) {
                int iWeight = Groups.get(keys.get(i));
                if (!(keys.get(i).equals(WantedMods.get(0).getGroup())) && !(keys.get(i).equals(WantedMods.get(1).getGroup()))) {
                    chance += triChance(modWeight1, modWeight2, iWeight, AffixesWeight, Groups.get(WantedMods.get(0).getGroup()), Groups.get(WantedMods.get(1).getGroup()), iWeight);
                }
            }
            return chance;
        }
        if (aAf == 3 && WantedMods.size() == 3) {
            int modWeight1 = WantedMods.get(0).getWeight();
            int modWeight2 = WantedMods.get(1).getWeight();
            int modWeight3 = WantedMods.get(2).getWeight();
            return triChance(modWeight1, modWeight2, modWeight3, AffixesWeight, Groups.get(WantedMods.get(0).getGroup()), Groups.get(WantedMods.get(1).getGroup()), Groups.get(WantedMods.get(2).getGroup()));
        }
        return -1;
    }

    public static double triChance(int modWeight1, int modWeight2, int modWeight3, float AffixesWeight, int group1, int group2, int group3) {
        double chance = 0;
//        System.out.println(modWeight1 + "  " +  modWeight2 + "  " + modWeight3 + "  " + group1 + "  " + group2 + "  " + AffixesWeight);
//        System.out.println(chanceC(modWeight1, modWeight2, modWeight3, AffixesWeight,group1,group2));
//        System.out.println(modWeight1/AffixesWeight);
//        System.out.println(modWeight2 / (AffixesWeight - group1));
//        System.out.println(modWeight3 / (AffixesWeight - group1 - group2));
        chance += chanceC(modWeight1, modWeight3, modWeight2, AffixesWeight, group1, group3);
        chance += chanceC(modWeight1, modWeight2, modWeight3, AffixesWeight, group1, group2);
        chance += chanceC(modWeight2, modWeight3, modWeight1, AffixesWeight, group2, group3);
        chance += chanceC(modWeight2, modWeight1, modWeight3, AffixesWeight, group2, group1);
        chance += chanceC(modWeight3, modWeight1, modWeight2, AffixesWeight, group3, group1);
        chance += chanceC(modWeight3, modWeight2, modWeight1, AffixesWeight, group3, group2);

//        System.out.println(chance);
        return chance;
    }

    public static double chanceC(int modWeight1, int modWeight2, int modWeight3, float AffixesWeight, int group1, int group2) {
        return ((modWeight1 / AffixesWeight) * (modWeight2 / (AffixesWeight - group1)) * (modWeight3 / (AffixesWeight - group1 - group2)));
    }

    public static double spr(float SuffixWeight, float PrefixWeight, int prefAm, int sufAm,Map<String, Integer> Groups) {
        if(Groups.size()>3){
            if (prefAm + sufAm == 6)
                return 0.08333333333333333;
            else {

                double chance = (getFactorial(prefAm + sufAm - 2) / (getFactorial(prefAm - 1) * (getFactorial(prefAm + sufAm - 2 - prefAm + 1))) * Math.pow(PrefixWeight / (PrefixWeight + SuffixWeight), prefAm - 1) * Math.pow(SuffixWeight / (PrefixWeight + SuffixWeight), sufAm - 1));
//            System.out.println(getFactorial(prefAm + sufAm - 2));
//            System.out.println(getFactorial(prefAm - 1) * (getFactorial(prefAm + sufAm - 2 - prefAm - 1)));
//            System.out.println(getFactorial(prefAm + sufAm - 2) / (getFactorial(prefAm - 1) * (getFactorial(prefAm + sufAm - 2 - prefAm - 1))));
//            System.out.println(Math.pow(PrefixWeight / (PrefixWeight + SuffixWeight), prefAm - 1));
//            System.out.println(Math.pow(SuffixWeight / (PrefixWeight + SuffixWeight), sufAm - 1));
//            System.out.println(chance);
                if (prefAm + sufAm == 5)
                    return chance * 0.25;
                else if (prefAm + sufAm == 4)
                    return chance * 0.6666666;
            }
        }else if (Groups.size() == 1){
            if((prefAm > 1  && (prefAm + sufAm == 4)) || (prefAm == 3  && (prefAm + sufAm == 5)))
                return 0;
            else
                return 0.66666666;
        } else if(Groups.size() == 2){
            if((prefAm > 1  && (prefAm + sufAm == 4)))
                return 0;
            else if((prefAm == 2 || prefAm == 3) && sufAm == 3 )
                return 0.25;
            else if((prefAm == 2 || prefAm == 3) && sufAm == 2)
                return PrefixWeight/(PrefixWeight+SuffixWeight)*SuffixWeight/(PrefixWeight+SuffixWeight)*(0.75);
            else return  0.666 - PrefixWeight/(PrefixWeight+SuffixWeight)*SuffixWeight/(PrefixWeight+SuffixWeight)*(0.75);
        }

        return -1;
    }

    public static int getFactorial(int f) {
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }
}



