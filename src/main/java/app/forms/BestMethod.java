package app.forms;

import app.classes.MethodToOut;
import app.util.BaseForm;
import app.util.CustomTableModel;

import javax.swing.*;
import java.util.List;

public class BestMethod extends BaseForm {
    private JPanel mainPanel;
    private JTable CostTable;
    private JTable TriesTable;
    private JButton okButton;
    private CustomTableModel<MethodToOut> triesModel;
    private CustomTableModel<MethodToOut> costModel;

    public BestMethod(List<MethodToOut> TriesMethods,List<MethodToOut> CostMethods){
        setContentPane(mainPanel);
        TriesTable.getTableHeader().setReorderingAllowed(false);
        CostTable.getTableHeader().setReorderingAllowed(false);

        triesModel = new CustomTableModel<MethodToOut>(
                MethodToOut.class,
                new String[]{
                        "Name", "Tries", "Cost"
                },
                TriesMethods
        );
        TriesTable.setModel(triesModel);

        costModel = new CustomTableModel<MethodToOut>(
                MethodToOut.class,
                new String[]{
                        "Name", "Tries", "Cost"
                },
                CostMethods
        );
        CostTable.setModel(costModel);

        okButton.addActionListener(e -> {
            dispose();
        });
        CostTable.getColumn("Name").setPreferredWidth(350);
        TriesTable.getColumn("Name").setPreferredWidth(350);
        TriesTable.getColumn("Tries").setPreferredWidth(30);
        CostTable.getColumn("Tries").setPreferredWidth(30);
        TriesTable.getColumn("Cost").setPreferredWidth(30);
        CostTable.getColumn("Cost").setPreferredWidth(30);


        setVisible(true);
    }

    @Override
    public int getFormWidth() {
        return 1100;
    }

    @Override
    public int getFormHeight() {
        return 300;
    }
}
