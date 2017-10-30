import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Map {

    public final static String TEMPPATH = Map.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "mapTEMP.txt";

    private int rows;
    private int columns;
    private InitialCell initialCell;
    private List<GoalCell> goalCells;
    private List<DangerCell> dangerCells;

    public Map(){
        this.goalCells = new LinkedList<GoalCell>();
        this.dangerCells = new LinkedList<DangerCell>();
        this.columns = 0;
        this.rows = 0;
    }

    public void setInitialCell(InitialCell initialCell)    {
        this.initialCell = initialCell;
    }
    public void addGoal(GoalCell goalCell) {
        getGoalCells().add(goalCell);
    }
    public void addDangerCell(DangerCell cell) {
        dangerCells.add(cell);
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows)    {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns)    {
        this.columns = columns;
    }

    public InitialCell getInitialCell() {
        return initialCell;
    }

    public List<GoalCell> getGoalCells() {
        return goalCells;
    }

    public List<DangerCell> getDangerCells() {
        return dangerCells;
    }

    public void saveTemp(){
        save(TEMPPATH);
    }
    public void save(String path){
        try {
            PrintWriter writer = new PrintWriter(path.replace(".txt", "") + ".txt", "UTF-8");

            List<String> mapParsed = MapParser.getFile(this);
            for (String line : mapParsed)
                writer.println(line);
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public int goalMaxIndex(){
        int max = 0;
        for(GoalCell goal : goalCells)
            if(goal.getValue() > max)
                max = goal.getValue();

        return max;
    }
    /*DEPRECATED
    *
    *
    public List<DangerZone> dangerZones(){

        List<DangerZone> zones = new LinkedList<DangerZone>();
        for(Cell actualCell : dangerCells){
            boolean added = false;
            for(DangerZone actualZone : zones){
                if(actualZone.firstRow == actualCell.getRow() + 1 && actualZone.firstColumn == actualCell.getColumn()){
                    actualZone.firstRow--;
                    added = true;
                    break;
                }
                if(actualZone.lastRow == actualCell.getRow() - 1 && actualZone.firstColumn == actualCell.getColumn()){
                    actualZone.lastRow++;
                    added = true;
                    break;
                }
            }
            if(!added){
                DangerZone zone = new DangerZone(actualCell.getColumn(), actualCell.getRow(), actualCell.getRow(), actualCell.getColumn());
                zones.add(zone);
            }
        }
        List<DangerZone> finalZones = new LinkedList<DangerZone>();
        for(DangerZone actualZone : zones){
            boolean added = false;
            for(DangerZone actualFinalZone : finalZones){
                if(actualZone.firstColumn == actualFinalZone.lastColumn + 1 && actualZone.firstRow == actualFinalZone.firstRow && actualZone.lastRow == actualFinalZone.lastRow){
                    actualFinalZone.lastColumn = actualZone.lastColumn;
                    added = true;
                    break;
                }
                if(actualZone.lastColumn == actualFinalZone.firstColumn - 1 && actualZone.firstRow == actualFinalZone.firstRow && actualZone.lastRow == actualFinalZone.lastRow){
                    actualFinalZone.firstColumn = actualZone.firstColumn;
                    added = true;
                    break;
                }
            }
            if(!added){
                finalZones.add(actualZone);
            }
        }

        return finalZones;
    }
    *
    *
    * */
}
