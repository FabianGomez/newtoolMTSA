import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapParser {

    public static Map parse(String FILENAME) {
        BufferedReader br = null;
        FileReader fr = null;
        Map map = new Map();
        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String currentLine;
            Integer currentRow = 0;
            Integer currentColumn;
            while ((currentLine = br.readLine()) != null) {
                String[] splitedCurrentLine = currentLine.split("\\t");

                if(splitedCurrentLine.length == 0)
                    continue;

                if(splitedCurrentLine.length == 1) {
                    splitedCurrentLine = currentLine.split(",");

                    if(splitedCurrentLine.length == 0)
                        continue;
                }

                if(map.getColumns() != 0 && map.getColumns() != splitedCurrentLine.length)
                    throw new Exception("PARSE ERROR: there are columns with different size");

                map.setColumns(splitedCurrentLine.length);

                for (currentColumn = 0; currentColumn < splitedCurrentLine.length; currentColumn++) {
                    if(tryParseInitial(splitedCurrentLine[currentColumn])){
                        InitialCell cell = new InitialCell(currentRow, currentColumn);
                        map.setInitialCell(cell);
                        //System.out.println("INITIAL on:" + currentRow + "," + currentColumn);
                    }
                    if(tryParseGoal(splitedCurrentLine[currentColumn])){
                        Integer goalIndex = Integer.parseInt(splitedCurrentLine[currentColumn]);
                        GoalCell goalCell = new GoalCell(goalIndex, currentRow,currentColumn);
                        map.addGoal(goalCell);
                        //System.out.println("GOAL " + goalIndex + " on:" + currentRow + "," + currentColumn);
                    }
                    if(tryParseDangerCell(splitedCurrentLine[currentColumn])){
                        DangerCell cell = new DangerCell(currentRow, currentColumn);
                        map.addDangerCell(cell);
                        //System.out.println("DANGER on:" + currentRow + "," + currentColumn);
                    }
                    if(tryParseWallCell(splitedCurrentLine[currentColumn])){
                        WallCell cell = new WallCell(currentRow, currentColumn);
                        map.addWallCell(cell);
                        //System.out.println("WALL on:" + currentRow + "," + currentColumn);
                    }
                    if(tryParseDoorCell(splitedCurrentLine[currentColumn])){
                        DoorCell cell = new DoorCell(currentRow, currentColumn,map.getDoorCells().size());
                        map.addDoorCell(cell);
                        //System.out.println("DOOR on:" + currentRow + "," + currentColumn);
                    }
                }
                currentRow++;
            }
            map.setRows(currentRow);

            return  map;

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
        return null;
    }

    public static List<String> getFile(Map map){
        List<String> file = new ArrayList<String>();

        List<List<String>> gridParsed = new LinkedList<List<String>>();

        for (int row=0; row < map.getRows();row++) {
            List<String> line = new ArrayList<String>();
            for (int col = 0; col < map.getColumns(); col++) {
                line.add("0");
            }
            gridParsed.add(line);
        }
        for(Integer goalId :  map.getGoalCells().keySet()) {
            List<GoalCell> goalCells = map.getGoalCells().get(goalId);
            for (GoalCell cell : goalCells) {
                gridParsed.get(cell.getRow()).remove(cell.getColumn());
                gridParsed.get(cell.getRow()).add(cell.getColumn(), cell.getValue().toString());
            }
        }
        for(DangerCell cell : map.getDangerCells()) {
            gridParsed.get(cell.getRow()).remove(cell.getColumn());
            gridParsed.get(cell.getRow()).add(cell.getColumn(),cell.getValue());
        }

        for(WallCell cell : map.getWallCells()) {
            gridParsed.get(cell.getRow()).remove(cell.getColumn());
            gridParsed.get(cell.getRow()).add(cell.getColumn(),cell.getValue());
        }

        for(DoorCell cell : map.getDoorCells()) {
            gridParsed.get(cell.getRow()).remove(cell.getColumn());
            gridParsed.get(cell.getRow()).add(cell.getColumn(),cell.getValue());
        }

        gridParsed.get(map.getInitialCell().getRow()).remove(map.getInitialCell().getColumn());
        gridParsed.get(map.getInitialCell().getRow()).add(map.getInitialCell().getColumn(),map.getInitialCell().getValue());

        for (List<String> line : gridParsed) {
            String lineString = "";
            for (String character : line) {
                lineString += character + ",";
            }
            lineString += "-";
            lineString = lineString.replace(",-","");
            file.add(lineString);
        }

        return file;
    }


    private static boolean tryParseGoal(String value) {
        try{
            Integer goalIndex = Integer.parseInt(value);
            return goalIndex != 0;
        }catch (Exception e){
        }
        return false;
    }
    private static boolean tryParseWallCell(String value) {
        return value.equals(WallCell.CONSTVALUE());
    }
    private static boolean tryParseDoorCell(String value) {
        return value.equals(DoorCell.CONSTVALUE());
    }
    private static boolean tryParseDangerCell(String value) {
        return value.equals(DangerCell.CONSTVALUE());
    }
    private static boolean tryParseInitial(String value) {
        return value.equals(InitialCell.CONSTVALUE());
    }

}