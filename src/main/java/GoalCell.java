import java.awt.*;

public class GoalCell extends Cell{

    public GoalCell(Integer value, int row, int column){
        super(value,row,column);
    }

    @Override
    public Integer getValue() {
        return Integer.parseInt(value.toString());
    }
    @Override
    public String getLabelValue() {
        return getValue().toString();
    }
    @Override
    public Color getColor() {
        return Color.GREEN;
    }


}
