import javax.swing.*;
import java.awt.*;

public class GridEnviromentSimulationUI extends JFrame{
    private JPanel gridPanel;
    private JFrame frame;
    Grid grid;

    CellPane actual;
    CellPane startingToMove;
    CellPane endingToMove;

    private static final Color STARTINGCOLOR =  Color.getHSBColor(102,25,61);
    private static final Color ACTUALCOLOR =  Color.getHSBColor(99,44,44);
    private static final Color ENDINGCOLOR =  Color.getHSBColor(102,25,61);


    public GridEnviromentSimulationUI(){

        Map map = MapParser.parse(Map.TEMPPATH);
        grid = new Grid(map,false);

        gridPanel = grid;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(800, 300);
        setLayout(new BorderLayout(5,5));
        add(gridPanel, BorderLayout.NORTH);
        repaint();
    }

    public void startingToMove(int vertical, int horizontal){
        startingToMove = grid.getCell(vertical,horizontal);
        startingToMove.startMomentarilyPaint(STARTINGCOLOR);
    }
    public void finishingToMove(int vertical, int horizontal){
        endingToMove = grid.getCell(vertical,horizontal);
        startingToMove.endMomentarilyPaint();
        endingToMove.startMomentarilyPaint(ENDINGCOLOR);
    }
    public void endingToMove(){
        actual = endingToMove;
        actual.startMomentarilyPaint(ACTUALCOLOR);
    }
}
