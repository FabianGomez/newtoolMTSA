import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class CellPane extends JPanel {

    private Color defaultBackground;
    private Cell cell;
    private MainForm mainForm;
    public CellPane(boolean modified, final MainForm mainForm) {
        this.mainForm = mainForm;
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
                    mainForm.listenerChangeCell(CellPane.this);
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