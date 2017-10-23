import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class CellPane extends JPanel {

    private Color defaultBackground;
    private Cell cell;

    public CellPane(boolean modified) {
        if(modified) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    defaultBackground = getBackground();
                    setBackground(Color.BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(defaultBackground);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    DialogCellAction dialog = new DialogCellAction();
                    dialog.setSize(500, 100);
                    dialog.setLocationRelativeTo(null);
                    dialog.show();
                    int response = dialog.dialogResponse;
                    int column = getCell().getColumn();
                    int row = getCell().getRow();
                    switch (response) {
                        case DialogCellAction.CONSTCLEAR:
                            setCell(new EmptyCell(row, column));
                            break;
                        case DialogCellAction.CONSTSTART:
                            setCell(new InitialCell(row, column));
                            break;
                        case DialogCellAction.CONSTGOAL:
                            NumberFormat format = NumberFormat.getInstance();
                            NumberFormatter formatter = new NumberFormatter(format);
                            formatter.setValueClass(Integer.class);
                            formatter.setMinimum(1);
                            formatter.setMaximum(Integer.MAX_VALUE);
                            formatter.setAllowsInvalid(false);
                            formatter.setCommitsOnValidEdit(true);
                            JFormattedTextField field = new JFormattedTextField(formatter);
                            field.createToolTip();
                            field.setToolTipText("Fill the goal's index");
                            JOptionPane.showMessageDialog(null, field, "Goal", 3, null);
                            if (field.getValue() != null)
                                setCell(new GoalCell((Integer) field.getValue(), row, column));
                            break;
                        case DialogCellAction.CONSTDANGER:
                            setCell(new DangerCell(row, column));
                            break;
                        default:
                            break;
                    }

                    paint();
                }
            });
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(25, 25);
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void paint(){
        this.setVisible(false);
        try{this.remove(0);}catch (Exception e){}
        this.setBackground(cell.getColor());
        defaultBackground = cell.getColor();
        JLabel component = new JLabel();
        component.setText(cell.getLabelValue());
        this.add(component);
        this.setVisible(true);
    }

    public void startMomentarilyPaint(Color color){
        this.setVisible(false);
        defaultBackground = getBackground();
        setBackground(color);
        this.setVisible(true);
    }
    public void endMomentarilyPaint(){
        this.setVisible(false);
        setBackground(defaultBackground );
        this.setVisible(true);
    }
}