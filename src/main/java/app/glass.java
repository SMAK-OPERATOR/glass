package app;

import app.classes.Mod;
import app.classes.ModToOut;
import app.classes.Stats;
import app.forms.StartForm;
import app.util.BaseForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class glass {
    private static glass instance;

    public static void main(String[] args) throws FileNotFoundException {
        new glass();
    }

    private glass() throws FileNotFoundException {
        instance = this;

        this.initUi();


        new StartForm();
    }

    public void initUi() {
        try {
            URL url = glass.class.getClassLoader().getResource("GlassblowersBauble — копия.png");
            if (url != null)
                BaseForm.setBaseApplicationIcon(ImageIO.read(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static glass getInstance() {
        return instance;
    }

    public static List<List<ModToOut>> parseMods(String baseGroup, String base, String firstInf, String secondInf, int ilvl) throws FileNotFoundException {
        Gson gson = new Gson();
        Type mymodtype = new TypeToken<List<Mod>>() {
        }.getType();
        if (firstInf.equals("None"))
            firstInf = "";
        else
            firstInf = firstInf.toLowerCase();
        if (secondInf.equals("None"))
            secondInf = "";
        else
            secondInf = secondInf.toLowerCase();
        List<Mod> endMods1 = gson.fromJson(sUPperAsER(new File("data\\" + baseGroup + "\\" + base + ".json")), mymodtype);
        List<ModToOut> prefixes = new ArrayList<>();
        List<ModToOut> suffixes = new ArrayList<>();
        Type modListType = new TypeToken<List<trans>>() {
        }.getType();
        List<trans> mods = gson.fromJson(sUPperAsER(new File("stat_translations.min.json")), modListType);
        String str;
        String buffer = "";
        for (int i = 0; i != endMods1.size(); i++) {
            if ((endMods1.get(i).getInfluence().equals(firstInf) || endMods1.get(i).getInfluence().equals(secondInf) || endMods1.get(i).getInfluence().equals("")) && endMods1.get(i).getiLvl() <= ilvl) {
//                System.out.println(endMods1.get(i).getInfluence() + "  " + firstInf + "  " + secondInf);

//            System.out.print(endMods1.get(i).getStats());
                for (int l = 0; l != endMods1.get(i).getStats().size(); l++) {
                    for (int j = 0; j != mods.size(); j++) {
                        if (endMods1.get(i).getStats().get(l).getId().equals(mods.get(j).ids.get(0))) {
                            str = mods.get(j).English.get(0).string;
                            for (int f = 0; f != mods.get(j).English.get(0).format.size(); f++) {
                                if (endMods1.get(i).getStats().get(l).getMin() == endMods1.get(i).getStats().get(l).getMax())
                                    str = (str.replace("{" + f + "}", "" + endMods1.get(i).getStats().get(l).getMin()));
                                else
                                    str = (str.replace("{" + f + "}", endMods1.get(i).getStats().get(l).getMin() + "-" + endMods1.get(i).getStats().get(l).getMax()));
                            }
//                        System.out.println(endMods1.get(i).getStats().size());
                            if (endMods1.get(i).getStats().size() > 1 && l == 0) {
                                buffer = str;
                            } else if (endMods1.get(i).getStats().size() > 1 && l == 1) {
                                str = "<html> " + str + ",<br>" + buffer + "</html>";
                                String influence = endMods1.get(i).getInfluence();
//                            System.out.println(influence);
                                if (influence.equals(""))
                                    influence = "none";
                                if (endMods1.get(i).getGeneration_type().equals("prefix")) {
                                    prefixes.add(new ModToOut(str, endMods1.get(i).getiLvl(), endMods1.get(i).getWeight(), influence, endMods1.get(i).getGeneration_type(), endMods1.get(i).getGroup(), endMods1.get(i).getTags()));
                                } else
                                    suffixes.add(new ModToOut(str, endMods1.get(i).getiLvl(), endMods1.get(i).getWeight(), influence, endMods1.get(i).getGeneration_type(), endMods1.get(i).getGroup(), endMods1.get(i).getTags()));
                            } else if (endMods1.get(i).getStats().size() == 1) {
                                String influence = endMods1.get(i).getInfluence();
//                            System.out.println(influence);
                                if (influence.equals(""))
                                    influence = "none";
                                if (endMods1.get(i).getGeneration_type().equals("prefix")) {
                                    prefixes.add(new ModToOut(str, endMods1.get(i).getiLvl(), endMods1.get(i).getWeight(), influence, endMods1.get(i).getGeneration_type(), endMods1.get(i).getGroup(), endMods1.get(i).getTags()));
                                } else
                                    suffixes.add(new ModToOut(str, endMods1.get(i).getiLvl(), endMods1.get(i).getWeight(), influence, endMods1.get(i).getGeneration_type(), endMods1.get(i).getGroup(), endMods1.get(i).getTags()));
                            }
                        }
                    }
                }
            }
        }
//        System.out.println(suffixes);
//        System.out.println(prefixes);
        List<List<ModToOut>> modstoout = new ArrayList<>();
        modstoout.add(prefixes);
        modstoout.add(suffixes);
//        System.out.println(modstoout);
        return (modstoout);
    }

    public static String sUPperAsER(File file) throws FileNotFoundException {
        String json = "";
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        try {
            String s;
            while ((s = br.readLine()) != null) {
                json = json.concat(s);
            }
        } catch (Exception a) {
            a.printStackTrace();
        }
        return json;
    }

    public static int getAllWeight(List<List<ModToOut>> mods) {
        int weight = 0;
        for (int j = 0; j != mods.size(); j++) {
            for (int i = 0; i != mods.get(j).size(); i++) {
                weight += mods.get(j).get(i).getWeight();
            }
        }
        return weight;
    }

    public static int getAffixesWeight(List<ModToOut> mods) {
        int weight = 0;
        for (int i = 0; i != mods.size(); i++) {
            weight += mods.get(i).getWeight();
        }

        return weight;
    }


    class trans {
        List<english> English;
        List<String> ids;

        @Override
        public String toString() {
            return "trans{" +
                    "english=" + English +
                    ", ids=" + ids +
                    '}';
        }

        public trans(List<english> english, List<String> ids) {
            this.English = english;
            this.ids = ids;
        }
    }


    class english {
        List<Map<String, Integer>> condition;
        List<String> format;
        List<List<String>> index_handlers;
        String string;

        public english(List<Map<String, Integer>> condition, List<String> format, List<List<String>> index_handlers, String string) {
            this.condition = condition;
            this.format = format;
            this.index_handlers = index_handlers;
            this.string = string;
        }


        @Override
        public String toString() {
            return "english{" +
                    "condition=" + condition +
                    ", format=" + format +
                    ", index_handlers=" + index_handlers +
                    ", string='" + string + '\'' +
                    '}';
        }
    }


}


