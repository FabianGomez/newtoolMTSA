import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Model {

    private List<String> lines;

    public Model(String FILENAME, Map map){
        lines = new LinkedList<String>();
        lines.addAll(Size(map));
        lines.addAll(StartingPosition(map));
        lines.addAll(GoalsPosition(map));
        lines.addAll(WallDefinition(map));
        lines.addAll(DoorDefinition(map));
        List<String> linesTemplate = parseTemplate(FILENAME);
        int indexToAdd = -1;
        int index = 0;
        for(String line: linesTemplate){
            if(line.contains("controllerSpec")){
                indexToAdd = index;
                break;
            }

            index++;
        }
        if(indexToAdd != -1) {
            linesTemplate.addAll(indexToAdd + 1, ControllerSpecLines(map));
            linesTemplate.addAll(indexToAdd - 1, DoorProperty(map));
            linesTemplate.addAll(indexToAdd - 1, Fluents(map));
            linesTemplate.addAll(indexToAdd - 1, GoalsAsserts(map));
        }

        getLines().addAll(linesTemplate);

        if(indexToAdd == -1) {
            index = 0;
            for(String line: getLines()){
                if(line.contains("controller")){
                    indexToAdd = index;
                    break;
                }

                index++;
            }
            getLines().addAll(indexToAdd - 1,ControllerSpec(map));
            getLines().addAll(indexToAdd - 1,DoorProperty(map));
            getLines().addAll(indexToAdd - 1,Fluents(map));
            getLines().addAll(indexToAdd - 1,GoalsAsserts(map));

        }

    }

    private static List<String> parseTemplate(String FILENAME)     {
        List<String> lines = new LinkedList<String>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                lines.add(currentLine);
            }
            return  lines;

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
        return new LinkedList<String>();
    }
    private static List<String> Size(Map map)    {
        List<String> definition = new LinkedList<String>();
        definition.add("const SizeCol = " + (map.getColumns() - 1));
        definition.add("const SizeRow = " + (map.getRows() - 1));
        definition.add("range RCol = 0..SizeCol");
        definition.add("range RRow = 0..SizeRow");
        return definition;
    }
    private static List<String> StartingPosition(Map map)    {
        List<String> definition = new LinkedList<String>();
        definition.add("const StartingRow = " + map.getInitialCell().getRow());
        definition.add("const StartingColumn = " + map.getInitialCell().getColumn());
        return definition;
    }
    private static List<String> GoalsPosition(Map map)    {
        List<String> definition = new LinkedList<String>();
        for(Integer goalId :  map.getGoalCells().keySet()){
            List<GoalCell> goalCells =  map.getGoalCells().get(goalId);
            int goalRelativeIndex = 1;
            for(GoalCell goalCell : goalCells)
            {
                definition.add("const GoalRow_" + goalId + "_" + goalRelativeIndex + " = " + goalCell.getRow());
                definition.add("const GoalColumn_" + goalId + "_" + goalRelativeIndex + " = " + goalCell.getColumn());
                goalRelativeIndex++;
            }
        }
        return definition;
    }
    private static List<String> WallDefinition(Map map)    {
        List<String> definition = new LinkedList<String>();
        if(map.getWallCells().size() == 0)
            return definition;
        
        String lineDefinition = "def Wall(row,col) = ";
        for(WallCell cell: map.getWallCells())
            lineDefinition += " row == " + cell.getRow() + " && col == " + cell.getColumn() + " || " ;
        lineDefinition += "!";
        lineDefinition = lineDefinition.replace("|| !","");

        definition.add(lineDefinition);

        return definition;

    }
    private static List<String> DoorDefinition(Map map)    {
        List<String> definition = new LinkedList<String>();
        if(map.getDoorCell() == null)
            return definition;


        String lineDefinition = "def DoorAndClosed(row,col,door) = ";
        lineDefinition += " row == " + map.getDoorCell().getRow() + " && col == " + map.getDoorCell().getColumn() + " && door == 1  " ;


        definition.add(lineDefinition);

        return definition;

    }
    private static List<String> Fluents(Map map)    {
        List<String> definition = new LinkedList<String>();

        if(map.getDangerCells().size() == 0)
            return  definition;

        String fluentOn = "";
        for(DangerCell cell: map.getDangerCells())
            fluentOn += "arrive["+ cell.getRow() +"]["+ cell.getColumn() +"] ,";
        fluentOn += "!";
        fluentOn = fluentOn.replace(",!","");

        //definition.add("fluent G[row:RRow][col:RCol] = <{arrive[row][col]}, Alphabet\\{arrive[row][col]}>");
        definition.add("fluent DangerZone = <{");
        definition.add(fluentOn);
        definition.add("}, Alphabet\\{");
        definition.add(fluentOn);
        definition.add("}>");
        return definition;
    }
    private static List<String> DoorProperty(Map map)    {
        List<String> definition = new LinkedList<String>();
        if(map.getDoorCell() == null)
            return  definition;

        definition.add("ltl_property DoorCorrectActionOpen = [] (open -> X((!open) W close))");
        definition.add("ltl_property DoorCorrectActionClose =   [] (close -> X((!close) W open))");
        definition.add("ltl_property DoorCorrectActionOrder = ((!close) W open)");

        return definition;
    }
    private static List<String> GoalsAsserts(Map map)    {
        List<String> definition = new LinkedList<String>();
        for(Integer goalId :  map.getGoalCells().keySet()){
            List<GoalCell> goalCells =  map.getGoalCells().get(goalId);
            int goalRelativeIndex = 1;
            String goalDefinition = "assert G" + goalId + " = (";
            for(GoalCell goalCell : goalCells)
            {
                goalDefinition += " G[GoalRow_" + goalId + "_" + goalRelativeIndex + "][GoalColumn_" + goalId + "_" + goalRelativeIndex + "] || " ;
                goalRelativeIndex++;
            }
            goalDefinition += "x";
            goalDefinition = goalDefinition.replace("|| x",")");
            definition.add(goalDefinition);
        }
        return definition;
    }
    private static List<String> ControllerSpecLines(Map map)    {
        List<String> definition = new LinkedList<String>();
        if(map.getDangerCells().size() > 0)
            definition.add("safety = {Safe}");

        definition.add("controllable = {ControllableActions}");

        String liveness = "liveness = {";

        List<Integer> goalIds = new LinkedList<Integer>();

        goalIds.addAll( map.getGoalCells().keySet());

        Collections.sort(goalIds);

        for(Integer goalId : goalIds)
        {
            List<GoalCell> goalCells =  map.getGoalCells().get(goalId);
            liveness += "G" + goalId + ",";
        }
        liveness += "}";
        liveness = liveness.replace(",}","}");
        definition.add(liveness);

        return definition;
    }
    private static List<String> ControllerSpec(Map map)    {
        List<String> definition = new LinkedList<String>();
        definition.add("controllerSpec SPEC = {");
        definition.addAll(ControllerSpecLines(map));
        definition.add("}");

        return definition;
    }
    public List<String> getLines() {
        return lines;
    }

}