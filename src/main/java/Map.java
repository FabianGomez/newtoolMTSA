import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Map {

    public final static String TEMPPATH = Map.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "mapTEMP.txt";

    private int rows;
    private int columns;
    private InitialCell initialCell;
    private HashMap<Integer,List<GoalCell>> goalCells;
    private List<DangerCell> dangerCells;
    private List<WallCell> wallCells;

    public Map(){
        this.goalCells = new HashMap<Integer,List<GoalCell>>();
        this.dangerCells = new LinkedList<DangerCell>();
        this.wallCells = new LinkedList<WallCell>();
        this.columns = 0;
        this.rows = 0;
    }

    public void setInitialCell(InitialCell initialCell)    {
        this.initialCell = initialCell;
    }
    public void addGoal(GoalCell goalCell) {
        if(getGoalCells().containsKey(goalCell.getValue()))
            getGoalCells().get(goalCell.getValue()).add(goalCell);
        else{
            List<GoalCell> goals = new LinkedList<GoalCell>();
            goals.add(goalCell);
            getGoalCells().put(goalCell.getValue(),goals);
        }
    }
    public void addDangerCell(DangerCell cell) {
        dangerCells.add(cell);
    }

    public void addWallCell(WallCell cell) {
        wallCells.add(cell);
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

    public HashMap<Integer,List<GoalCell>> getGoalCells() {
        return goalCells;
    }

    public List<DangerCell> getDangerCells() {
        return dangerCells;
    }

    public List<WallCell> getWallCells() {
        return wallCells;
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
        for(Integer index : goalCells.keySet())
            if(index > max)
                max = index;

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
