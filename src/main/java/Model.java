import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.*;

public class Model {

    private List<String> lines;

    public Model(String FILENAME, Map map){
        lines = new LinkedList<String>();
        lines.addAll(Size(map));
        lines.addAll(StartingPosition(map));
        lines.addAll(GoalsPosition(map));
        //lines.addAll(DangerPosition(map));
        lines.addAll(Enviroment());
        lines.addAll(Fluents(map));
        lines.addAll(SafeProperty(map));
        lines.addAll(GoalsAsserts(map));

        List<String> linesTemplate = parseTemplate(FILENAME);
        int indexToAdd = -1;
        int index = 0;
        for(String line: linesTemplate){
            if(line.contains("SPEC"))
                indexToAdd = index;

            index++;
        }
        if(indexToAdd != -1)
            linesTemplate.addAll(indexToAdd + 1 ,ControllerSpecLines(map));

        getLines().addAll(linesTemplate);
        if(indexToAdd == -1)
            lines.addAll(ControllerSpec(map));

        lines.addAll(ControllerDefinition());

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
    private static List<String> Enviroment()    {
        List<String> definition = new LinkedList<String>();
        definition.add("set Gotos = {{go}.{s,n,e,w}}");
        definition.add("set Nodetour = {nodetour}");
        definition.add("set Arrivals = {{arrive}.[row:RRow][col:RCol]}");
        definition.add("set Detours = {{detour}.{s,n,e,w}}");
        definition.add("set ControllableActions = {Gotos,Arrivals}");
        definition.add("set UncontrollableActions = {Nodetour,Detours}");
        definition.add("set Alphabet = {ControllableActions, UncontrollableActions}");


        definition.add("GRID = POS[StartingRow][StartingColumn],");
        definition.add("POS[row:RRow][col:RCol] =");
        definition.add("    ( when (row<SizeRow) go.s-> GOING_TO[row+1][col]");
        definition.add("    | when (col<SizeCol) go.e-> GOING_TO[row][col+1]");
        definition.add("    | when (row>0) go.n -> GOING_TO[row-1][col]");
        definition.add("    | when (col>0) go.w -> GOING_TO[row][col-1]),");

        definition.add("GOING_TO[row:RRow][col:RCol] = ( nodetour -> arrive[row][col] -> POS[row][col]");
        definition.add("    | when (row>0) detour.n -> arrive[row-1][col] -> POS[row-1][col]");
        definition.add("    | when (row<SizeRow) detour.s -> arrive[row+1][col] -> POS[row+1][col]");
        definition.add("    | when (col>0) detour.w ->  arrive[row][col-1] ->POS[row][col-1]");
        definition.add("    | when (col<SizeCol) detour.e -> arrive[row][col+1] -> POS[row][col+1]");
        definition.add(")+{Gotos,Arrivals,Detours,Nodetour}.");

        definition.add("DRONE = ({Gotos}->USER),");
        definition.add("USER = ({Nodetour,Detours}->ARRIVE),");
        definition.add("ARRIVE = ({Arrivals}->DRONE).");
        definition.add("||ENV = (GRID || DRONE).");

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

        definition.add("fluent G[row:RRow][col:RCol] = <{arrive[row][col]}, Alphabet\\{arrive[row][col]}>");
        definition.add("fluent DangerZone = <{");
        definition.add(fluentOn);
        definition.add("}, Alphabet\\{");
        definition.add(fluentOn);
        definition.add("}>");
        return definition;
    }
    private static List<String> SafeProperty(Map map)    {
        List<String> definition = new LinkedList<String>();
        if(map.getDangerCells().size()==0)
            return  definition;

        definition.add("ltl_property Safe = []((!DangerZone))");

        return definition;
    }
    private static List<String> GoalsAsserts(Map map)    {
        List<String> definition = new LinkedList<String>();
        for(Integer goalId :  map.getGoalCells().keySet()){
            List<GoalCell> goalCells =  map.getGoalCells().get(goalId);
            int goalRelativeIndex = 1;
            for(GoalCell goalCell : goalCells)
            {
                definition.add("assert G" + goalId + "_" + goalRelativeIndex + " = G[GoalRow_" + goalId + "_" + goalRelativeIndex + "][GoalColumn_" + goalId + "_" + goalRelativeIndex + "]");
                goalRelativeIndex++;
            }
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
            int goalRelativeIndex = 1;
            for(GoalCell goalCell : goalCells) {
                liveness += "G" + goalId + "_" + goalRelativeIndex + ",";
                goalRelativeIndex++;
            }
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
    private static List<String> ControllerDefinition()    {
        List<String> definition = new LinkedList<String>();
        definition.add("controller ||C = (ENV)~{SPEC}.");
        definition.add("||ANIMAR = (C || ENV).");

        return definition;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

     /*DEPRECATED
    *
    *
    private static List<String> DangerPosition(Map map)    {
        List<String> definition = new LinkedList<String>();
        int index = 1;
        for(DangerZone danger: map.dangerZones())
        {
            definition.add("range DangerZoneRows_" + index + " = " + danger.firstRow +".."+ danger.lastRow);
            definition.add("range DangerZoneColumns_" + index + " = " + danger.firstColumn +".."+ danger.lastColumn);
            index++;
        }
        return definition;
    }
    *
    */
}