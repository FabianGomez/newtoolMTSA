import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GridEnviromentSimulationUI extends JFrame {
    private JPanel gridPanel;
    private JFrame frame;
    Grid grid;

    CellPane actual;
    CellPane startingToMove;

    private static final Color STARTINGCOLOR =  Color.orange;
    private static final Color ACTUALCOLOR =  Color.orange;


    public GridEnviromentSimulationUI(){

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }
                frame = new JFrame("MTSA TOOL");
                frame.setLayout(new BorderLayout());
                Map map = MapParser.parse(Map.TEMPPATH);
                grid = new Grid(map,false, null);
                actual = grid.getCell(map.getInitialCell().getRow(), map.getInitialCell().getColumn());
                startingToMove(0,0);
                finishingToMove(0,0);
                gridPanel = grid;
                frame.add(gridPanel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        });

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 300);
        setLayout(new BorderLayout(5,5));
        add(gridPanel, BorderLayout.NORTH);

        repaint();
    }

    public void startingToMove(int vertical, int horizontal){
        actual.endMomentarilyPaint();
        startingToMove = grid.getCell(  actual.getCell().getRow() + vertical, actual.getCell().getColumn() + horizontal);
        startingToMove.startMomentarilyPaint(STARTINGCOLOR);
    }

    public void finishingToMove(int vertical, int horizontal){
        startingToMove.endMomentarilyPaint();
        actual = grid.getCell(startingToMove.getCell().getRow()+ vertical, startingToMove.getCell().getColumn()+ horizontal);
        actual.startMomentarilyPaint(ACTUALCOLOR);
    }

}
