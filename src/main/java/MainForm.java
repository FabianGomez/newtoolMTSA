import com.sun.org.apache.bcel.internal.generic.NEW;

import java.text.NumberFormat;
import javax.management.loading.ClassLoaderRepository;
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
    private JButton startButton;
    private JButton goalButton;
    private JButton dangerButton;
    private JPanel buttonsPanel;
    private JPanel formPannel;
    private JButton clearButton;
    private JButton wallButton;
    private JButton doorButton;
    private JFrame frame;

    private static final int CLEAR = 0;
    private static final int ADDINGSTART = 1;
    private static final int ADDINGDANGER = 2;
    private static final int ADDINGGOAL = 3;
    private static final int ADDINGWALL = 4;
    private static final int ADDINGDOOR = 5;
    private int currentGoal;

    public static void main(String[] args) {
        new MainForm();
    }

    private int currentAction = -1;

    public MainForm() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                frame = new JFrame("MTSA TOOL");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(panel1);
                frame.pack();
                frame.setLocationRelativeTo(null);
                buttonsPanel.setVisible(false);
                grid.setVisible(false);
                frame.setVisible(true);
            }
        });

        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser dialog = new JFileChooser();
                int returnVal = dialog.showOpenDialog(buttonOpen);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = dialog.getSelectedFile();
                    panel1.setVisible(false);
                    Map map = MapParser.parse(file.getAbsolutePath());
                    panel1.remove(grid);
                    grid = new Grid(map, true, MainForm.this);
                    panel1.add(grid);
                    panel1.setVisible(true);
                    grid.setVisible(true);
                    buttonsPanel.setVisible(true);
                    ((JFrame) panel1.getTopLevelAncestor()).pack();
                    ((JFrame) panel1.getTopLevelAncestor()).setLocationRelativeTo(null);
                }
            }
        });

        buttonGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (grid.getClass() != Grid.class) {
                    JOptionPane.showMessageDialog(null, "There is not map to generate", "Error", 1, null);
                    return;
                }
                Map map = ((Grid) grid).getMap();
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
                NewMapForm form  = new NewMapForm();
                form.setLocationRelativeTo(null);
                form.pack();
                form.setVisible(true);

                if (form.columns != -1) {
                    Map map = new Map();
                    map.setRows(form.rows);
                    map.setColumns(form.columns);
                    panel1.remove(grid);
                    grid = new Grid(map, true,MainForm.this);
                    buttonsPanel.setVisible(true);
                    panel1.add(grid);
                    panel1.setVisible(true);
                    ((JFrame) panel1.getTopLevelAncestor()).pack();
                    ((JFrame) panel1.getTopLevelAncestor()).setLocationRelativeTo(null);
                }
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (grid.getClass() != Grid.class) {
                        JOptionPane.showMessageDialog(null, "There is not map to saveTemp", "Error", 1, null);
                        return;
                    }

                    JFileChooser dialog = new JFileChooser();
                    int returnVal = dialog.showOpenDialog(buttonOpen);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = dialog.getSelectedFile();
                        Map map = ((Grid) grid).getMap();
                        map.save(file.getAbsolutePath());
                    }
                } catch (Exception ex) {
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    startChangeMap(CLEAR, clearButton);
                } else {
                    clearButton.setBackground(startButton.getBackground());
                    finishChangeMap();
                }
            }
        });
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    startChangeMap(ADDINGSTART, startButton);
                } else {
                    startButton.setBackground(goalButton.getBackground());
                    finishChangeMap();
                }
            }
        });
        dangerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    startChangeMap(ADDINGDANGER, dangerButton);
                } else {
                    dangerButton.setBackground(goalButton.getBackground());
                    finishChangeMap();
                }
            }
        });
        goalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    currentGoal = ((Grid) grid).getMap().goalMaxIndex() + 1;
                    startChangeMap(ADDINGGOAL, goalButton);
                } else {
                    goalButton.setBackground(startButton.getBackground());
                    finishChangeMap();
                }
            }
        });
        wallButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    startChangeMap(ADDINGWALL, wallButton);
                } else {
                    wallButton.setBackground(startButton.getBackground());
                    finishChangeMap();
                }
            }
        });
        doorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentAction == -1) {
                    startChangeMap(ADDINGDOOR, doorButton);
                } else {
                    doorButton.setBackground(startButton.getBackground());
                    finishChangeMap();
                }
            }
        });

    }

    private void enableButton(boolean enabled) {
        clearButton.setEnabled(enabled);
        startButton.setEnabled(enabled);
        goalButton.setEnabled(enabled);
        wallButton.setEnabled(enabled);
        doorButton.setEnabled(enabled);
        dangerButton.setEnabled(enabled);
        buttonOpen.setEnabled(enabled);
        buttonGenerate.setEnabled(enabled);
        buttonNew.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
    }
    private void startChangeMap(int action, JButton button) {
        button.setBackground(Color.gray);
        currentAction = action;
        enableButton(false);
        button.setEnabled(true);
    }

    private void finishChangeMap() {
        currentAction = -1;
        enableButton(true);
    }

    private CellPane initialCellPane;

    public void listenerEndChangeCell(int x, int y){
        CellPane endCellPane = (CellPane) grid.findComponentAt(x - grid.getLocationOnScreen().x,y - grid.getLocationOnScreen().y);

        int column = endCellPane.getCell().getColumn();
        int row = endCellPane.getCell().getRow();

        int minRow = initialCellPane.getCell().getRow() < row ? initialCellPane.getCell().getRow() : row ;
        int minColumn = initialCellPane.getCell().getColumn() < column ? initialCellPane.getCell().getColumn() : column ;

        int maxRow = initialCellPane.getCell().getRow() >= row ? initialCellPane.getCell().getRow() : row ;
        int maxColumn = initialCellPane.getCell().getColumn() >= column ? initialCellPane.getCell().getColumn() : column ;

        for(int indexRow = minRow ;  indexRow <= maxRow ; indexRow++){
            for(int indexColumn = minColumn ;  indexColumn <= maxColumn ; indexColumn++) {
                CellPane actualCellPane = ((Grid) grid).getCell(indexRow, indexColumn);
                Map map = ((Grid) grid).getMap();
                switch (currentAction) {
                    case CLEAR:
                        actualCellPane.setCell(new EmptyCell(indexRow, indexColumn));
                        break;
                    case ADDINGSTART:
                        if(map.getInitialCell() == null)
                            actualCellPane.setCell(new InitialCell(indexRow, indexColumn));
                        else
                            JOptionPane.showMessageDialog(null, "There is an initial cell", "Error", 1, null);
                        break;
                    case ADDINGGOAL:
                        actualCellPane.setCell(new GoalCell(currentGoal, indexRow, indexColumn));
                        break;
                    case ADDINGDANGER:
                        actualCellPane.setCell(new DangerCell(indexRow, indexColumn));
                        break;
                    case ADDINGWALL:
                        actualCellPane.setCell(new WallCell(indexRow, indexColumn));
                        break;
                    case ADDINGDOOR:
                        if(map.getDoorCell() == null)
                            actualCellPane.setCell(new DoorCell(indexRow, indexColumn));
                        else
                            JOptionPane.showMessageDialog(null, "There is a door cell", "Error", 1, null);
                        break;
                }
                actualCellPane.paint();
                if(currentAction == ADDINGSTART)
                    break;
                if(currentAction == ADDINGDOOR)
                    break;
            }
        }

        initialCellPane = null;

    }

    public void listenerStartChangeCell(CellPane cellPane){
        initialCellPane = cellPane;
    }
}
