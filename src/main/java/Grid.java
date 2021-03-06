import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class Grid extends JPanel {

    List<List<CellPane>> cells;

    public Grid(Map map, boolean modified, MainForm mainForm) {
        GridBagLayout grid = new GridBagLayout();
        setLayout(grid);
        cells = new ArrayList<List<CellPane>>();
        GridBagConstraints gbc = new GridBagConstraints();
        for (int row = 0; row < map.getRows(); row++) {
            List<CellPane> aux = new ArrayList<CellPane>();
            for (int col = 0; col < map.getColumns(); col++) {
                gbc.gridx = col;
                gbc.gridy = row;

                CellPane cellPane = new CellPane(modified, mainForm);
                cellPane.setCell(new EmptyCell(row,col));
                Border border = new MatteBorder(1, 1, 1, 1, Color.BLACK);

                cellPane.setBorder(border);
                add(cellPane, gbc);
                aux.add(cellPane);
            }
            cells.add(aux);
        }

        setCells(map);
        paintCells();

    }

    public void setCells(Map map){
        for(DangerCell cell : map.getDangerCells()){
            CellPane cellPane = (cells.get(cell.getRow())).get(cell.getColumn());
            cellPane.setCell(cell);
        }
        for(WallCell cell : map.getWallCells()){
            CellPane cellPane = (cells.get(cell.getRow())).get(cell.getColumn());
            cellPane.setCell(cell);
        }
        if(map.getDoorCell() != null){
            CellPane cellPane = (cells.get(map.getDoorCell().getRow())).get(map.getDoorCell().getColumn());
            cellPane.setCell(map.getDoorCell());
        }
        for(Integer goalId :  map.getGoalCells().keySet()) {
            List<GoalCell> goalCells = map.getGoalCells().get(goalId);
            for (GoalCell cell : goalCells) {
                CellPane cellPane = (cells.get(cell.getRow())).get(cell.getColumn());
                cellPane.setCell(cell);
            }
        }

        if(map.getInitialCell()!= null) {
            CellPane cellPane = (cells.get(map.getInitialCell().getRow())).get(map.getInitialCell().getColumn());
            cellPane.setCell(map.getInitialCell());
        }

    }

    public CellPane getCell(int row, int column){
        return cells.get(row).get(column);
    }

    public void paintCells() {
        for (List<CellPane> cellRows : cells)
            for (CellPane cellPane : cellRows)
                cellPane.paint();
    }

    public Map getMap(){
        Map map = new Map();
        map.setRows(cells.size());
        map.setColumns(cells.get(0).size());
        for (List<CellPane> cellRows : cells)
            for (CellPane cellPane : cellRows)
            {
                if(cellPane.getCell().getClass().equals(GoalCell.class))
                    map.addGoal((GoalCell) cellPane.getCell());
                if(cellPane.getCell().getClass().equals(DangerCell.class))
                    map.addDangerCell((DangerCell) cellPane.getCell());
                if(cellPane.getCell().getClass().equals(WallCell.class))
                    map.addWallCell((WallCell) cellPane.getCell());
                if(cellPane.getCell().getClass().equals(DoorCell.class))
                    map.setDoorCell((DoorCell) cellPane.getCell());
                if(cellPane.getCell().getClass().equals(InitialCell.class))
                    map.setInitialCell((InitialCell) cellPane.getCell());
            }

        return map;
    }

}
