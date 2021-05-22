package app.forms;

import app.classes.*;
import app.classes.Currency;
import app.glass;
import app.util.BaseForm;
import app.util.CustomTableModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StartForm extends BaseForm {
    private JPanel mainPanel;
    private JTable SuffixTable;
    private JTable PrefixTable;
    private JComboBox BaseGroupComboBox;
    private JComboBox BaseComboBox;
    private JComboBox FirstInfluenceComboBox;
    private JComboBox SecondInfluenceComboBox;
    private JLabel BaseJLabel;
    private JLabel FirstInfluenceJlabel;
    private JLabel SecondInfluenceJLabel;
    private JScrollPane Scroll;
    private JComboBox MethodComboBox;
    private JLabel MethodJLabel;
    private JTextField ChanceTG;
    private JTextField AvgTriesText;
    private JTable curTable;
    private JTable WantedTable;
    private JButton OKButton;
    private JTextField ilvlField;
    private JTextField Confidence;
    private JButton calculateButton;
    private JTextField triesField;
    private JTextField TotalCostField;
    private JButton recalcuteButton;
    private JComboBox FossilBox1;
    private JComboBox FossilBox2;
    private JComboBox FossilBox3;
    private JComboBox FossilBox4;
    private JLabel FossilLabel1;
    private JLabel FossilLabel2;
    private JLabel FossilLabel3;
    private JLabel FossilLabel4;
    private JButton buttonBest;
    private CustomTableModel<ModToOut> prefixModel;
    private CustomTableModel<ModToOut> suffixModel;
    private CustomTableModel<ModToOut> wantedModel;
    private CustomTableModel<Currency> curModel;

    final List<ModToOut> WantedMods = new ArrayList<>();
    int prefixCounter = 0;
    int suffixCounter = 0;

    List<Map<String, Integer>> groups = new ArrayList<>();
    Map<String, Fossil> fossils;
    Map<String, Currency> currencies;

    public StartForm() throws FileNotFoundException {
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fossils = iniFossils();
        currencies = initPoeNinja();
//        System.out.println(currencies);
        groups.add(new HashMap<>()); // pref
        groups.add(new HashMap<>()); // suf
        initButtons();

        setVisible(true);
    }


    public Map<String, Currency> initPoeNinja() throws FileNotFoundException {
        Formatter f = new Formatter();
        f.format("https://poe.ninja/api/data/currencyoverview?league=Ultimatum&type=Currency");
        java.lang.reflect.Type modListType = new TypeToken<List<Currency>>() {
        }.getType();
        List<Currency> listCurs = new Gson().fromJson(glass.sUPperAsER(new File("data\\Currency.json")), modListType);
        Map<String, Currency> currencyMap = new HashMap<>();
        try {
            URL url = new URL(f.toString());
            InputStreamReader inpStream = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new Gson().fromJson(inpStream, JsonObject.class);
            JsonArray array = jsonObject.get("lines").getAsJsonArray();
            for (int i = 0; i != listCurs.size(); i++) {
                for (int j = array.size() - 1; j != 0; j--) {
                    JsonObject object = array.get(j).getAsJsonObject();
                    JsonElement name = object.get("currencyTypeName");
                    if (("\"" + listCurs.get(i).getName() + "\"").equals(name.toString())) {
                        JsonElement price = object.get("chaosEquivalent");
                        listCurs.get(i).setValue(Float.parseFloat(String.valueOf(price)));
                        break;
                    }
                }
                currencyMap.put(listCurs.get(i).getName(), listCurs.get(i));
            }
            return currencyMap;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return currencyMap;
    }

    public void initButtons() throws FileNotFoundException {
        BaseGroupComboBox.addItem("Base Group");
        BaseComboBox.addItem("Base");
        FirstInfluenceComboBox.addItem("None");
        SecondInfluenceComboBox.addItem("None");
        Gson gson = new Gson();
        java.lang.reflect.Type baseType = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        Map<String, List<String>> bases = gson.fromJson(glass.sUPperAsER(new File("data\\Bases.json")), baseType);
        List<String> keys = new ArrayList<>(bases.keySet());
        for (int i = 0; i != keys.size(); i++) {
            BaseGroupComboBox.addItem(keys.get(i));
        }
        BaseGroupComboBox.addActionListener(e -> {
            if (BaseGroupComboBox.getSelectedItem() != "Base Group") {
                BaseJLabel.setEnabled(true);
                BaseComboBox.setEnabled(true);
                BaseComboBox.removeAllItems();
                BaseComboBox.addItem("Base");
                for (int j = 0; j != bases.get(BaseGroupComboBox.getSelectedItem()).size(); j++) {
                    BaseComboBox.addItem(bases.get(BaseGroupComboBox.getSelectedItem()).get(j));
                }
            } else {
                BaseJLabel.setEnabled(false);
                BaseComboBox.setEnabled(false);
                BaseComboBox.removeAllItems();
                BaseComboBox.addItem("Base");
            }
        });
        BaseComboBox.addActionListener(e -> {
            if (BaseComboBox.getSelectedItem() != "Base") {
                OKButton.setEnabled(true);
            }
            if (BaseComboBox.getSelectedItem() == "Base")
                OKButton.setEnabled(false);

        });
        List<String> influences = new ArrayList<>();
        influences.add("Elder");
        influences.add("Shaper");
        influences.add("Crusader");
        influences.add("Hunter");
        influences.add("Warlord");
        influences.add("Redeemer");
        for (int k = 0; k != 5; k++) {
            FirstInfluenceComboBox.addItem(influences.get(k));
        }
        for (int k = 0; k != 5; k++) {
            SecondInfluenceComboBox.addItem(influences.get(k));
        }
        FirstInfluenceComboBox.addActionListener(e -> {
            if (SecondInfluenceComboBox.getSelectedItem() == FirstInfluenceComboBox.getSelectedItem() && !(FirstInfluenceComboBox.getSelectedItem() == ("None"))) {
                showError("You cannot add two of the same influences");
                FirstInfluenceComboBox.setSelectedItem("None");
            }
        });
        SecondInfluenceComboBox.addActionListener(e -> {
            if (FirstInfluenceComboBox.getSelectedItem() == SecondInfluenceComboBox.getSelectedItem() && !(SecondInfluenceComboBox.getSelectedItem() == ("None"))) {
                showError("You cannot add two of the same influences");
                SecondInfluenceComboBox.setSelectedItem("None");
            }
        });

        OKButton.addActionListener(e -> {
            if (BaseComboBox.isEnabled() && !((BaseComboBox.getSelectedItem()) == "Base")) {
                try {
                    if ((Integer.parseInt(ilvlField.getText())) > 0 && (Integer.parseInt(ilvlField.getText())) <= 100) {
                        try {
                            if (!(wantedModel == null) && !(wantedModel.getValues().isEmpty())) {
                                wantedModel.removeValues();
                                wantedModel.fireTableDataChanged();
                                buttonBest.setEnabled(false);
                            }
                            if (!(curModel == null) && !(curModel.getValues().isEmpty())) {
                                curModel.removeValues();
                                curModel.fireTableDataChanged();
                                triesField.setEnabled(false);
                                triesField.setText("");
                                TotalCostField.setText("");
                                Confidence.setText("");
                                ChanceTG.setText("");
                                AvgTriesText.setText("");
                                recalcuteButton.setEnabled(false);
                            }
                            initTables((String) BaseGroupComboBox.getSelectedItem(), (String) BaseComboBox.getSelectedItem(), (String) FirstInfluenceComboBox.getSelectedItem(), (String) SecondInfluenceComboBox.getSelectedItem(), Integer.parseInt(ilvlField.getText()));
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                    } else showError("Incorrect Item level");
                } catch (NumberFormatException numberFormatException) {
                    showError("Incorrect Item level");
                }

            }
        });
        MethodComboBox.addItem("Method");
        MethodComboBox.addItem("Alt+Aug");
        MethodComboBox.addItem("Alt");
        MethodComboBox.addItem("Chaos Orb");
        MethodComboBox.addItem("Alch+Scouring");
        MethodComboBox.addItem("Binding+Scouring");
        MethodComboBox.addItem("Fossils");
        List<String> fossilKeys = new ArrayList<>(fossils.keySet());
        FossilBox1.addItem("None");
        FossilBox2.addItem("None");
        FossilBox3.addItem("None");
        FossilBox4.addItem("None");
        for (int i = 0; i != fossilKeys.size(); i++) {
            FossilBox1.addItem(fossilKeys.get(i));
            FossilBox2.addItem(fossilKeys.get(i));
            FossilBox3.addItem(fossilKeys.get(i));
            FossilBox4.addItem(fossilKeys.get(i));
        }

        MethodComboBox.addItemListener(e -> {
            triesField.setEditable(false);
            recalcuteButton.setEnabled(false);
            if (MethodComboBox.getSelectedItem().equals("Fossils")) {
                FossilLabel1.setVisible(true);
                FossilBox1.setVisible(true);
                FossilLabel2.setVisible(true);
                FossilBox2.setVisible(true);
                FossilLabel3.setVisible(true);
                FossilBox3.setVisible(true);
                FossilLabel4.setVisible(true);
                FossilBox4.setVisible(true);
            } else {
                FossilLabel1.setVisible(false);
                FossilBox1.setVisible(false);
                FossilLabel2.setVisible(false);
                FossilBox2.setVisible(false);
                FossilLabel3.setVisible(false);
                FossilBox3.setVisible(false);
                FossilLabel4.setVisible(false);
                FossilBox4.setVisible(false);
            }
        });

        FossilBox1.addActionListener(e -> {
            triesField.setEditable(false);
            recalcuteButton.setEnabled(false);
        });
        FossilBox2.addActionListener(e -> {
            triesField.setEditable(false);
            recalcuteButton.setEnabled(false);
        });
        FossilBox3.addActionListener(e -> {
            triesField.setEditable(false);
            recalcuteButton.setEnabled(false);
        });
        FossilBox4.addActionListener(e -> {
            triesField.setEditable(false);
            recalcuteButton.setEnabled(false);
        });


        recalcuteButton.addActionListener(e -> {

                try {
                    if(Integer.parseInt(triesField.getText()) > 0){
                    if (MethodComboBox.getSelectedItem().equals("Alt+Aug") || MethodComboBox.getSelectedItem().equals("Alt")) {
                        altAugs(true, Integer.parseInt(triesField.getText()), (String) MethodComboBox.getSelectedItem(), false);
                    } else if (MethodComboBox.getSelectedItem().equals("Chaos Orb"))
                        chaosOrb(true, Integer.parseInt(triesField.getText()), false);
                    else if (MethodComboBox.getSelectedItem().equals("Alch+Scouring") || MethodComboBox.getSelectedItem().equals("Binding+Scouring")) {
                        AlchBind(true, Integer.parseInt(triesField.getText()), false, true);
                    } else if (MethodComboBox.getSelectedItem().equals("Fossils")) {
                        List<String> fos = new ArrayList<>();
                        if (!(FossilBox1.getSelectedItem().equals("None")))
                            fos.add((String) FossilBox1.getSelectedItem());
                        if (!(FossilBox2.getSelectedItem().equals("None")))
                            fos.add((String) FossilBox2.getSelectedItem());
                        if (!(FossilBox3.getSelectedItem().equals("None")))
                            fos.add((String) FossilBox3.getSelectedItem());
                        if (!(FossilBox4.getSelectedItem().equals("None")))
                            fos.add((String) FossilBox4.getSelectedItem());
                        FossilsCraft(fos, true, Integer.parseInt(triesField.getText()), false);
                    }
                    } else showError("Wrong Tries input");
                }catch (NumberFormatException numberFormatException){
                    showError("Wrong Tries input");
                }
        });

        AtomicReference<String> method = new AtomicReference<>("");
        calculateButton.addActionListener(e -> {
            if (!WantedMods.isEmpty()) {
                if (MethodComboBox.getSelectedItem().equals("Alt+Aug") || MethodComboBox.getSelectedItem().equals("Alt")) {
                    if (WantedMods.size() <= 2 && suffixCounter < 2 && prefixCounter < 2)
                        altAugs(false, 0, (String) MethodComboBox.getSelectedItem(), false);
                    else showError("You have incorrect amount of affixes");
                } else if (MethodComboBox.getSelectedItem().equals("Chaos Orb")) {
                    chaosOrb(false, 0, false);
                } else if (MethodComboBox.getSelectedItem().equals("Alch+Scouring") || MethodComboBox.getSelectedItem().equals("Binding+Scouring")) {
                    AlchBind(false, 0, false, false);
                } else if (MethodComboBox.getSelectedItem().equals("Fossils")) {
                    List<String> fos = new ArrayList<>();
                    boolean error = false;
                    if (!(FossilBox1.getSelectedItem().equals("None"))) {
                        if (fos.contains((String) FossilBox1.getSelectedItem())) {
                            error = true;
                        } else fos.add((String) FossilBox1.getSelectedItem());
                    }
                    if (!(FossilBox2.getSelectedItem().equals("None"))) {
                        if (fos.contains((String) FossilBox2.getSelectedItem())) {
                            error = true;
                        } else fos.add((String) FossilBox2.getSelectedItem());
                    }
                    if (!(FossilBox3.getSelectedItem().equals("None"))) {
                        if (fos.contains((String) FossilBox3.getSelectedItem())) {
                            error = true;
                        } else fos.add((String) FossilBox3.getSelectedItem());
                    }
                    if (!(FossilBox4.getSelectedItem().equals("None"))) {
                        if (fos.contains((String) FossilBox4.getSelectedItem())) {
                            error = true;
                        } else fos.add((String) FossilBox4.getSelectedItem());
                    }
                    if (fos.size() == 0) showError("You did not select any fossils");
                    else if (error) showError("You have identical fossils selected");
                    else FossilsCraft(fos, false, 0, false);

                }
            } else showError("You did not chose the wanted affixes");
        });

        PrefixTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = PrefixTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    ModToOut modToOut = prefixModel.getValues().get(row);
                    if (prefixCounter < 3) {
                        if (checkGroups(modToOut)) {
                            prefixCounter++;
                            initWantedTable(modToOut, true);
                        } else
                            showError("You already have a prefix of the same group");
                    } else
                        showError("You have maximum of prefixes");
                }
            }
        });
        SuffixTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = SuffixTable.rowAtPoint(e.getPoint());
                if (row != -1 && e.getClickCount() == 2) {
                    ModToOut modToOut = suffixModel.getValues().get(row);
                    if (suffixCounter < 3) {
                        if (checkGroups(modToOut)) {
                            suffixCounter++;
                            initWantedTable(modToOut, true);
                        } else
                            showError("You already have a suffix of the same group");
                    } else
                        showError("You have maximum of suffixes");
                }
            }
        });

        WantedTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = WantedTable.getSelectedRow();
                if (row != -1 && e.getClickCount() == 2) {
                    ModToOut modToOut = wantedModel.getValues().get(row);
                    initWantedTable(modToOut, false);
                }
            }
        });


        buttonBest.addActionListener(e -> {
            if (WantedMods.size() != 0) {
                try {
                    BestMethod();
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                }
            } else showError("You did not select wanted mods");
        });

    }

    public void BestMethod() throws CloneNotSupportedException {
        List<MethodToOut> bestTries = new ArrayList<>(5);
        List<MethodToOut> bestCost = new ArrayList<>(5);
        MethodToOut method;
        MethodToOut bufferMethod;
        if (WantedMods.size() <= 2 && suffixCounter < 2 && prefixCounter < 2) {
            method = altAugs(false, 0, "Alt+Aug", true);
            bestTries.add(method.clone());
            bestCost.add(method.clone());
            method = altAugs(false, 0, "Alt", true);
            bestTries.add(method.clone());
            bestCost.add(method.clone());
        }
        method = AlchBind(false, 0, true, true);
        bestTries.add(method.clone());
        bestCost.add(method.clone());
        method = AlchBind(false, 0, true, false);
        bestTries.add(method.clone());
        bestCost.add(method.clone());
        method = chaosOrb(false, 0, true);
        bestTries.add(method.clone());
        bestCost.add(method.clone());
        sortBest(bestTries, bestCost);
        List<String> keys = new ArrayList<>(fossils.keySet());
        for (int f1 = 0; f1 != fossils.size(); f1++) {
            List<String> fossilss = new ArrayList<>();
            fossilss.add(keys.get(f1));
            method = FossilsCraft(fossilss, false, 0, true);
            if (bestCost.size() != 5) {
                bestCost.add(method);
                bestTries.add(method);
            } else {
                if (bestCost.get(4).getCost() > method.getCost() && method.getCost() != 0) {
                    bestCost.remove(4);
                    bestCost.add(method);
                }
                if (bestTries.get(4).getTries() > method.getTries() && method.getTries() != 0) {
                    bestTries.remove(4);
                    bestTries.add(method);
                }
            }
            sortBest(bestTries, bestCost);
        }
        System.out.println(fossils.size());
        System.out.println(fossils);
        for (int f1 = 0; f1 != fossils.size(); f1++) {
            for (int f2 = f1 + 1; f2 != fossils.size(); f2++) {
                List<String> fossilss = new ArrayList<>();
                fossilss.add(keys.get(f1));
                fossilss.add(keys.get(f2));
                method = FossilsCraft(fossilss, false, 0, true);

                if (bestCost.get(4).getCost() > method.getCost() && method.getCost() != 0) {
                    bestCost.remove(4);
                    bestCost.add(method);
                }
                if (bestTries.get(4).getTries() > method.getTries() && method.getTries() != 0) {
                    bestTries.remove(4);
                    bestTries.add(method);
                }
                sortBest(bestTries, bestCost);

            }
        }
        for (int f1 = 0; f1 != fossils.size(); f1++) {
            for (int f2 = f1 + 1; f2 != fossils.size(); f2++) {
                for (int f3 = f2 + 1; f3 != fossils.size(); f3++) {
                    List<String> fossilss = new ArrayList<>();
//                    System.out.println(f1 + " " + f2 + " " + f3);
                    fossilss.add(keys.get(f1));
                    fossilss.add(keys.get(f2));
                    fossilss.add(keys.get(f3));
                    method = FossilsCraft(fossilss, false, 0, true);

                    if (bestCost.get(4).getCost() > method.getCost() && method.getCost() != 0) {
                        bestCost.remove(4);
                        bestCost.add(method);
                    }
                    if (bestTries.get(4).getTries() > method.getTries() && method.getTries() != 0) {
                        bestTries.remove(4);
                        bestTries.add(method);
                    }
                    sortBest(bestTries, bestCost);
                }
            }
        }

        for (int f1 = 0; f1 != fossils.size(); f1++) {
            for (int f2 = f1 + 1; f2 != fossils.size(); f2++) {
                for (int f3 = f2 + 1; f3 != fossils.size(); f3++) {
                    for (int f4 = f3 + 1; f4 != fossils.size(); f4++) {
                        List<String> fossilss = new ArrayList<>();
                        System.out.println(f1 + " " + f2 + " " + f3 + " " + f4);
                        fossilss.add(keys.get(f1));
                        fossilss.add(keys.get(f2));
                        fossilss.add(keys.get(f3));
                        fossilss.add(keys.get(f4));
                        method = FossilsCraft(fossilss, false, 0, true);

                        if (bestCost.get(4).getCost() > method.getCost() && method.getCost() != 0) {
                            bestCost.remove(4);
                            bestCost.add(method);
                        }
                        if (bestTries.get(4).getTries() > method.getTries() && method.getTries() != 0) {
                            bestTries.remove(4);
                            bestTries.add(method);
                        }
                        sortBest(bestTries, bestCost);
                    }
                }
            }
        }
        sortBest(bestTries, bestCost);
        new BestMethod(bestTries, bestCost);
    }


    public void sortBest(List<MethodToOut> bestTries, List<MethodToOut> bestCost) {
        bestTries.sort(new Comparator<MethodToOut>() {
            @Override
            public int compare(MethodToOut rhs, MethodToOut lhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return Integer.compare(rhs.getTries(), lhs.getTries());
            }
        });
        bestCost.sort(new Comparator<MethodToOut>() {
            @Override
            public int compare(MethodToOut rhs, MethodToOut lhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return Float.compare(rhs.getCost(), lhs.getCost());
            }
        });
    }

    public MethodToOut FossilsCraft(List<String> fossilsTC, boolean re, int tries, boolean bm) {
        List<List<ModToOut>> prefs = new ArrayList<>();
        List<ModToOut> prefxs = prefixModel.getValues().stream().map(x -> {
            try {
                return x.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return x;
        }).collect(Collectors.toList());
        prefs.add(prefxs);
        List<ModToOut> suffx = suffixModel.getValues().stream().map(x -> {
            try {
                return x.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return x;
        }).collect(Collectors.toList());
        prefs.add(suffx);
        List<ModToOut> wanted = WantedMods.stream().map(x -> {
            try {
                return x.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return x;
        }).collect(Collectors.toList());

        List<Map<String, Integer>> group = new ArrayList<>();
        group.add(new HashMap<>());
        group.add(new HashMap<>());
        for (int i = 0; i != fossilsTC.size(); i++) {
            Fossil currentFossil = fossils.get(fossilsTC.get(i));
            for (int a = 0; a != prefs.size(); a++) {
                for (int j = 0; j != prefs.get(a).size(); j++) {
                    for (int k = 0; k != currentFossil.getNegative_mod_weights().size(); k++) {
                        if (prefs.get(a).get(j).getTags().contains(currentFossil.getNegative_mod_weights().get(k).getTag())) {
                            prefs.get(a).get(j).setWeight((int) (prefs.get(a).get(j).getWeight() * currentFossil.getNegative_mod_weights().get(k).getWeight()));
                            break;
                        }
                    }
                    for (int k = 0; k != currentFossil.getPositive_mod_weights().size(); k++) {
                        if (prefs.get(a).get(j).getTags().contains(currentFossil.getPositive_mod_weights().get(k).getTag())) {
                            prefs.get(a).get(j).setWeight((int) (prefs.get(a).get(j).getWeight() * currentFossil.getPositive_mod_weights().get(k).getWeight()));
                            break;
                        }
                    }
                }
            }
        }
        for (int a = 0; a != prefs.size(); a++) {
            for (int j = 0; j != prefs.get(a).size(); j++) {
                if (group.get(a).get(prefs.get(a).get(j).getGroup()) != null)
                    group.get(a).put(prefs.get(a).get(j).getGroup(), group.get(a).get(prefs.get(a).get(j).getGroup()) + prefs.get(a).get(j).getWeight());
                else if (prefs.get(a).get(j).getWeight() != 0)
                    group.get(a).put(prefs.get(a).get(j).getGroup(), prefs.get(a).get(j).getWeight());
            }
        }

        for (int i = 0; i != wanted.size(); i++) {
            List<ModToOut> affs;
            if (wanted.get(i).getAffixType().equals("prefix"))
                affs = prefs.get(0);
            else affs = prefs.get(1);
            for (int j = 0; j != affs.size(); j++) {
                if (affs.get(j).getName().equals(wanted.get(i).getName())) {
                    wanted.get(i).setWeight(affs.get(j).getWeight());
                    break;
                }
            }
        }
//        System.out.println(wanted);
//        System.out.println(group);
//        System.out.println(glass.getAffixesWeight(prefs.get(1)));
//        System.out.println(glass.getAffixesWeight(prefs.get(0)));

//        System.out.println(WantedMods);
        int prikol = 1;
        for (int i = 0; i != wanted.size(); i++)
            prikol = prikol * wanted.get(i).getWeight();
        double chance;
        if (prikol != 0)
            chance = chaos(wanted, group, glass.getAffixesWeight(prefs.get(1)), glass.getAffixesWeight(prefs.get(0)));
        else chance = 0;
        if (!bm) {
            if (chance <= 0) {
                chance = 0;
                tries = 0;
                AvgTriesText.setText(String.valueOf(tries));
                triesField.setText(String.valueOf(tries));
                ChanceTG.setText("0 %");
                Confidence.setText("0 %");
            } else {
                if (!re) {
                    tries = (int) (1 / chance) + 1;
                    AvgTriesText.setText(String.valueOf(tries));
                    triesField.setText(String.valueOf(tries));
                }
                double confidence = 1 - Math.pow(1 - chance, tries);
                confidence = round(confidence);
                ChanceTG.setText(String.valueOf(round(chance * 100) + " %"));
                Confidence.setText(String.valueOf(round(confidence * 100)) + " %");
                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i != fossilsTC.size(); i++) {
                    map.put(fossilsTC.get(i), tries);
                }
                try {
                    initCurTable(map, false, true);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                triesField.setEditable(true);
                recalcuteButton.setEnabled(true);
            }
        } else {
            MethodToOut method;
            String s = "";
            for (int i = 0; i != fossilsTC.size(); i++) {
                s = s.concat(fossilsTC.get(i));
                s = s.concat(" ");
            }
            s = s.replace("Fossil", "");

            if (chance <= 0) {
                method = new MethodToOut(s, 0, 0);
                return method;
            } else {
                tries = (int) (1 / chance) + 1;
                float cost = 0;
                for (int i = 0; i != fossilsTC.size(); i++) {
                    cost += fossils.get(fossilsTC.get(i)).getCost() * tries;
                }
                switch (fossilsTC.size()) {
                    case 1 -> {
                        cost += currencies.get("Primitive Chaotic Resonator").getValue();
                    }
                    case 2 -> {
                        cost += currencies.get("Potent Chaotic Resonator").getValue();
                    }
                    case 3 -> {
                        cost += currencies.get("Powerful Chaotic Resonator").getValue();
                    }
                    case 4 -> {
                        cost += currencies.get("Prime Chaotic Resonator").getValue();
                    }
                }

                method = new MethodToOut(s, tries, round(cost));
                return method;
            }
        }
        return null;
    }

    public MethodToOut AlchBind(Boolean re, int tries, boolean bm, boolean alch) {
        double chance = chaos(WantedMods, groups, glass.getAffixesWeight(suffixModel.getValues()), glass.getAffixesWeight(prefixModel.getValues()));
        if (!bm) {
            if (!re) {
                tries = (int) (1 / chance) + 1;
                AvgTriesText.setText(String.valueOf(tries));
                triesField.setText(String.valueOf(tries));
            }
            double confidence = 1 - Math.pow(1 - chance, tries);
            confidence = round(confidence);
            ChanceTG.setText(String.valueOf(round(chance * 100) + " %"));
            Confidence.setText(String.valueOf(round(confidence * 100)) + " %");
            Map<String, Integer> map = new HashMap<>();
            if (MethodComboBox.getSelectedItem().equals("Alch+Scouring"))
                map.put("Orb of Alchemy", tries);
            else
                map.put("Orb of Binding", tries);
            map.put("Orb of Scouring", tries - 1);

            try {
                initCurTable(map, false, false);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

            triesField.setEditable(true);
            recalcuteButton.setEnabled(true);
        } else {
            MethodToOut method;
            float cost = 0;
            tries = (int) (1 / chance) + 1;
            String s;
            if (alch) {
                cost += currencies.get("Orb of Alchemy").getValue() * tries;
                s = "Alch+Scouring";
            } else {
                cost += currencies.get("Orb of Binding").getValue() * tries;
                s = "Binding+Scouring";
            }
            cost += currencies.get("Orb of Scouring").getValue() * tries;
            method = new MethodToOut(s, tries, round(cost));
            return method;
        }

        return null;
    }

    public MethodToOut chaosOrb(Boolean re, int tries, boolean bm) {
        double chance = chaos(WantedMods, groups, glass.getAffixesWeight(suffixModel.getValues()), glass.getAffixesWeight(prefixModel.getValues()));
        if (!bm) {
            if (!re) {
                tries = (int) (1 / chance) + 1;
                AvgTriesText.setText(String.valueOf(tries));
                triesField.setText(String.valueOf(tries));
            }
            double confidence = 1 - Math.pow(1 - chance, tries);
            confidence = round(confidence);
            ChanceTG.setText(String.valueOf(round(chance * 100) + " %"));
            Confidence.setText(String.valueOf(round(confidence * 100)) + " %");
            Map<String, Integer> map = new HashMap<>();
            map.put("Chaos Orb", tries);
            try {
                initCurTable(map, true, false);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

            triesField.setEditable(true);
            recalcuteButton.setEnabled(true);
        } else {
            MethodToOut method = new MethodToOut("Chaos orb", (int) (1 / chance) + 1, (int) (1 / chance) + 1);
            return method;
        }
        return null;
    }

    public MethodToOut altAugs(Boolean re, int tries, String method, boolean bm) {
        float allWeight = glass.getAffixesWeight(suffixModel.getValues()) + glass.getAffixesWeight(prefixModel.getValues());
        double chance;
        if ((method.equals("Alt+Aug")))
            chance = altAug(allWeight);
        else
            chance = alt(allWeight);
        if (!bm) {
            if (!re) {
                tries = (int) (1 / chance) + 1;
                AvgTriesText.setText(String.valueOf(tries));
                triesField.setText(String.valueOf(tries));
            }
            double confidence = 1 - Math.pow(1 - chance, tries);
            confidence = round(confidence);
            ChanceTG.setText(String.valueOf(round(chance * 100) + " %"));
            Confidence.setText(String.valueOf(round(confidence * 100)) + " %");
            Map<String, Integer> map = new HashMap<>();
            map.put("Orb of Alteration", tries);
            if (MethodComboBox.getSelectedItem().equals("Alt+Aug"))
                map.put("Orb of Augmentation", tries / 4);
            try {
                initCurTable(map, false, false);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

            triesField.setEditable(true);
            recalcuteButton.setEnabled(true);
        } else {
            MethodToOut methodd;
            float cost = 0;
            tries = (int) (1 / chance) + 1;
            String s;
            if (method.equals("Alt+Aug")) {
                cost += currencies.get("Orb of Augmentation").getValue() * tries / 4;
                s = "Alt+Aug";
            } else {
                s = "Alt";
            }
            cost += currencies.get("Orb of Alteration").getValue() * tries;
            methodd = new MethodToOut(s, tries, round(cost));
            return methodd;
        }

        return null;
    }

    public double chaos(List<ModToOut> wantedMods, List<Map<String, Integer>> grops, float suffixWeight, float prefixWeight) {
        return Methods.chaos(wantedMods, grops, suffixWeight, prefixWeight);
    }

    public double altAug(float allWeight) {
        return Methods.altaug(WantedMods, glass.getAffixesWeight(suffixModel.getValues()) / allWeight, glass.getAffixesWeight(prefixModel.getValues()) / allWeight);
    }

    public double alt(float allWeight) {
        return Methods.alt(WantedMods, glass.getAffixesWeight(suffixModel.getValues()) / allWeight, glass.getAffixesWeight(prefixModel.getValues()) / allWeight);
    }

    public Map<String, Fossil> iniFossils() throws FileNotFoundException {
        java.lang.reflect.Type modListType = new TypeToken<Map<String, Fossil>>() {
        }.getType();
        Formatter f = new Formatter();
        f.format("https://poe.ninja/api/data/itemoverview?league=Ultimatum&type=Fossil");
        Map<String, Fossil> listCurs = new Gson().fromJson(glass.sUPperAsER(new File("data\\fossils.json")), modListType);
        List<String> keys = new ArrayList<>(listCurs.keySet());
        try {
            URL url = new URL(f.toString());
            InputStreamReader inpStream = new InputStreamReader(url.openStream());
            JsonObject jsonObject = new Gson().fromJson(inpStream, JsonObject.class);
            JsonArray array = jsonObject.get("lines").getAsJsonArray();
            for (int i = 0; i != listCurs.size(); i++) {
                for (int j = array.size() - 1; j != 0; j--) {
                    JsonObject object = array.get(j).getAsJsonObject();
                    JsonElement name = object.get("name");
                    if (("\"" + listCurs.get(keys.get(i)).getName() + "\"").equals(name.toString())) {
                        JsonElement price = object.get("chaosValue");
                        listCurs.get(keys.get(i)).setCost(Float.parseFloat(String.valueOf(price)));
                        break;
                    }
                }
            }
            return listCurs;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCurs;
    }


    public void initCurTable(Map<String, Integer> curs, boolean chaos, boolean fossil) throws FileNotFoundException {
        List<Currency> curTo = new ArrayList<>();
        float total = 0;
        if (!chaos && !fossil) {
            Gson gson = new Gson();
            java.lang.reflect.Type modListType = new TypeToken<List<Currency>>() {
            }.getType();
            List<String> keys = new ArrayList<>(curs.keySet());
            for (int i = 0; i != keys.size(); i++) {
                curTo.add(new Currency(keys.get(i), currencies.get(keys.get(i)).getImagepath(), currencies.get(keys.get(i)).getValue(), curs.get(keys.get(i))));
                curTo.get(i).setTotal();
                curTo.get(i).setImage();
                total += curTo.get(i).getTotal();

            }
        } else if (chaos) {
            curTo.add(new Currency("Chaos Orb", "Currency/ChaosOrb.png", 1, -1));
            curTo.get(0).setAmount(curs.get("Chaos Orb"));
            curTo.get(0).setTotal();
            curTo.get(0).setImage();
            total += curTo.get(0).getTotal();
        } else {
            List<String> keys = new ArrayList<>(curs.keySet());
            for (int i = 0; i != keys.size(); i++) {
                curTo.add(new Currency(keys.get(i), fossils.get(keys.get(i)).getImagePath(), fossils.get(keys.get(i)).getCost(), curs.get(keys.get(i))));
                curTo.get(i).setTotal();
                curTo.get(i).setImage();
                total += curTo.get(i).getTotal();

            }
            switch (curTo.size()) {
                case 1 -> {
                    curTo.add(addRes("Primitive Chaotic Resonator", total, curs, keys));
//                    curTo.add(new Currency("Primitive Chaotic Resonator", currencies.get("Primitive Chaotic Resonator").getImagepath(), currencies.get("Primitive Chaotic Resonator").getValue(), curs.get(keys.get(0))));
//                    curTo.get(curTo.size() - 1).setTotal();
//                    curTo.get(curTo.size() - 1).setImage();
//                    total += curTo.get(curTo.size() - 1).getTotal();
                    total += curTo.get(curTo.size() - 1).getTotal();
                }
                case 2 -> {
                    curTo.add(addRes("Potent Chaotic Resonator", total, curs, keys));
                }
                case 3 -> {
                    curTo.add(addRes("Powerful Chaotic Resonator", total, curs, keys));
                }
                case 4 -> {
                    curTo.add(addRes("Prime Chaotic Resonator", total, curs, keys));
                }
            }
        }
        if (!(curModel == null) && !(curModel.getValues().isEmpty())) {
            curModel.removeValues();
            curModel.setValues(curTo);
            curModel.fireTableDataChanged();
        } else {
            //        System.out.println(curTo);
            curTable.getTableHeader().setReorderingAllowed(false);
            curModel = new CustomTableModel<Currency>(
                    Currency.class,
                    new String[]{
                            "Name", "Image path", "Image", "Value", "Amount", "Total"
                    },
                    curTo
            );
            curTable.setModel(curModel);
            curTable.getColumn("Image path").setPreferredWidth(0);
            curTable.getColumn("Image path").setMinWidth(0);
            curTable.getColumn("Image path").setMaxWidth(0);
            curTable.getColumn("Name").setPreferredWidth(170);
            curTable.setRowHeight(40);
        }
        TotalCostField.setText(String.valueOf(round(total)));


    }

    public Currency addRes(String res, float total, Map<String, Integer> curs, List<String> keys) {

        Currency curTo = (new Currency(res, currencies.get(res).getImagepath(), currencies.get(res).getValue(), curs.get(keys.get(0))));
        curTo.setTotal();
        curTo.setImage();
        return curTo;
    }

    public void initTables(String baseGroup, String base, String firstInf, String secInf, int ilvl) throws FileNotFoundException {
        List<List<ModToOut>> mods = glass.parseMods(baseGroup, base, firstInf, secInf, ilvl);
        float allWeight = glass.getAllWeight(mods);
        float prefixWeight = glass.getAffixesWeight(mods.get(0));
        float suffixWeight = glass.getAffixesWeight(mods.get(1));
        for (int j = 0; j != mods.size(); j++) {
            for (int i = 0; i != mods.get(j).size(); i++) {
                mods.get(j).get(i).setWeightChance(round(mods.get(j).get(i).getWeight() / allWeight * 100));
                if (groups.get(j).get(mods.get(j).get(i).getGroup()) != null)
                    groups.get(j).put(mods.get(j).get(i).getGroup(), groups.get(j).get(mods.get(j).get(i).getGroup()) + mods.get(j).get(i).getWeight());
                else
                    groups.get(j).put(mods.get(j).get(i).getGroup(), mods.get(j).get(i).getWeight());
                if (j == 0)
                    mods.get(j).get(i).setPrefixChance(round(mods.get(j).get(i).getWeight() / prefixWeight * 100));
                else
                    mods.get(j).get(i).setPrefixChance(round(mods.get(j).get(i).getWeight() / suffixWeight * 100));
            }
        }

        if (!(prefixModel == null) && !(prefixModel.getValues().isEmpty())) {
            prefixModel.removeValues();
            prefixModel.setValues(mods.get(0));
            prefixModel.fireTableDataChanged();
        }
        if (!(suffixModel == null) && !(suffixModel.getValues().isEmpty())) {
            suffixModel.removeValues();
            suffixModel.setValues(mods.get(1));
            suffixModel.fireTableDataChanged();
        } else {


            try {
                PrefixTable.getTableHeader().setReorderingAllowed(false);
                prefixModel = new CustomTableModel<ModToOut>(
                        ModToOut.class,
                        new String[]{
                                "Name", "iLvl", "Weight", "Prefix %", "Weight %", "Influence", "Affix Type", "Group", "Tags"
                        },
                        mods.get(0)
                );
                PrefixTable.setModel(prefixModel);
                prefixModel.fireTableDataChanged();
                setsize(PrefixTable, true);
                SuffixTable.getTableHeader().setReorderingAllowed(false);
                suffixModel = new CustomTableModel<ModToOut>(
                        ModToOut.class,
                        new String[]{
                                "Name", "iLvl", "Weight", "Prefix %", "Weight %", "Influence", "Affix Type", "Group", "Tags"
                        },
                        mods.get(1)
                );
                SuffixTable.setModel(suffixModel);
                suffixModel.fireTableDataChanged();
                setsize(SuffixTable, true);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void initWantedTable(ModToOut modToOut, boolean b) {
        buttonBest.setEnabled(true);
        if (b) {
            WantedMods.add(modToOut);
            if (!(wantedModel == null) && !(wantedModel.getValues().isEmpty()))
                wantedModel.fireTableDataChanged();
            else {
                try {
                    wantedModel = new CustomTableModel<ModToOut>(
                            ModToOut.class,
                            new String[]{
                                    "Name", "iLvl", "Weight", "Prefix %", "Weight %", "Influence", "Affix Type", "Group", "Tags"
                            },
                            WantedMods
                    );
                    WantedTable.setModel(wantedModel);
                    setsize(WantedTable, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i != WantedMods.size(); i++) {
                if (WantedMods.get(i).getName().equals(modToOut.getName())) {
                    if (modToOut.getAffixType().equals("prefix")) {
                        prefixCounter--;
                    } else {
                        suffixCounter--;
                    }
                    WantedMods.remove(i);
                    wantedModel.fireTableDataChanged();
                    break;
                }
            }
        }
    }

    public static void setsize(JTable table, boolean b) {
        table.getColumn("Name").setPreferredWidth(520);
        table.getColumn("iLvl").setPreferredWidth(40);
        table.getColumn("Weight").setPreferredWidth(65);
        table.getColumn("Prefix %").setPreferredWidth(80);
        table.getColumn("Weight %").setPreferredWidth(80);
        table.getColumn("Influence").setPreferredWidth(90);
        table.getColumn("Group").setPreferredWidth(0);
        table.getColumn("Group").setMinWidth(0);
        table.getColumn("Group").setMaxWidth(0);
        table.getColumn("Tags").setPreferredWidth(0);
        table.getColumn("Tags").setMinWidth(0);
        table.getColumn("Tags").setMaxWidth(0);
        if (b) {
            table.getColumn("Affix Type").setPreferredWidth(0);
            table.getColumn("Affix Type").setMinWidth(0);
            table.getColumn("Affix Type").setMaxWidth(0);
        } else {
            table.getColumn("Influence").setPreferredWidth(0);
            table.getColumn("Influence").setMinWidth(0);
            table.getColumn("Influence").setMaxWidth(0);
        }

    }

    public boolean checkGroups(ModToOut modToOut) {
        boolean gr = true;
        if (!WantedMods.isEmpty()) {
            for (int i = 0; i != WantedMods.size(); i++) {
                if (WantedMods.get(i).getGroup().equals(modToOut.getGroup())) {
                    gr = false;
                    break;
                }
            }
        }
        return gr;
    }

    public static float round(float value) {
        float scale = (float) Math.pow(10, 3);
        return (float) (Math.ceil(value * scale) / scale);
    }

    public static double round(double value) {
        double scale = Math.pow(10, 5);
        return (Math.ceil(value * scale) / scale);
    }

    public static void showError(String text) {
        JOptionPane.showMessageDialog(null, text, " Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public int getFormWidth() {
        return 1920;
    }

    @Override
    public int getFormHeight() {
        return 1080;
    }


}
