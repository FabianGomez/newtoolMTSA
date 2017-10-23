import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainForm {

    private JPanel panel1;
    private JButton buttonOpen;
    private JButton buttonNew;
    private JButton buttonGenerate;
    private JPanel grid;
    private JButton buttonSave;
    private JFrame frame;
    public static void main(String[] args) {
        new MainForm();
    }


    public MainForm(){

        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                frame = new JFrame("MTSA TOOL");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(panel1);
                frame.pack();
                frame.setLocationRelativeTo(null);
                //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });

        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser dialog = new JFileChooser();
                int returnVal = dialog.showOpenDialog(buttonOpen);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = dialog.getSelectedFile();
                    //This is where a real application would open the file.
                    panel1.setVisible(false);
                    Map map = MapParser.parse(file.getAbsolutePath());
                    grid = new Grid(map, true);
                    panel1.add(grid);
                    panel1.setVisible(true);
                    ((JFrame)panel1.getTopLevelAncestor()).pack();
                    ((JFrame)panel1.getTopLevelAncestor()).setLocationRelativeTo(null);
                }
            }
        });

        buttonGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(grid.getClass()!= Grid.class) {
                    JOptionPane.showMessageDialog(null, "There is not map to generate", "Error", 1, null);
                    return;
                }
                Map map = ((Grid)grid).getMap();
                JFileChooser dialog = new JFileChooser();
                int returnVal = dialog.showOpenDialog(buttonOpen);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = dialog.getSelectedFile();
                    ModelForm form = new ModelForm(map, file.getAbsolutePath());
                }
            }
        });

        buttonNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NumberFormat format = NumberFormat.getInstance();
                NumberFormatter formatter = new NumberFormatter(format);
                formatter.setValueClass(Integer.class);
                formatter.setMinimum(1);
                formatter.setMaximum(Integer.MAX_VALUE);
                formatter.setAllowsInvalid(false);
                formatter.setCommitsOnValidEdit(true);
                JFormattedTextField field = new JFormattedTextField(formatter);
                field.createToolTip();
                field.setToolTipText("Fill the map's rows size");
                JOptionPane.showMessageDialog(null, field, "Rows", 3, null);
                if(field.getValue() != null) {
                    int valueRow = (Integer) field.getValue();
                    field.setValue(null);
                    field.setToolTipText("Fill the maps's column size");
                    JOptionPane.showMessageDialog(null, field, "Columns", 3, null);
                    if(field.getValue() != null) {
                        int valueColumn = (Integer) field.getValue();

                        Map map = new Map();
                        map.setRows(valueRow);
                        map.setColumns(valueColumn);
                        grid = new Grid(map,true);
                        panel1.add(grid);
                        panel1.setVisible(true);
                        ((JFrame)panel1.getTopLevelAncestor()).pack();
                        ((JFrame)panel1.getTopLevelAncestor()).setLocationRelativeTo(null);
                    }
                }

            }
        });
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(grid.getClass()!= Grid.class) {
                        JOptionPane.showMessageDialog(null, "There is not map to saveTemp", "Error", 1, null);
                        return;
                    }

                    JFileChooser dialog = new JFileChooser();
                    int returnVal = dialog.showOpenDialog(buttonOpen);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = dialog.getSelectedFile();
                        Map map = ((Grid)grid).getMap();
                        map.save(file.getAbsolutePath());
                    }
                }catch (Exception ex){}
            }
        });
    }


}
