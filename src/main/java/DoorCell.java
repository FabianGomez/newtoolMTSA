import java.awt.*;

public class DoorCell extends Cell{

    public DoorCell(int row, int column){
        super(CONSTVALUE(),row,column);
    }
    @Override
    public String getValue() {
        return DoorCell.CONSTVALUE();
    }
    @Override
    public String getLabelValue() {
        return DoorCell.CONSTVALUE();
    }
    @Override
    public Color getColor() {
        return Color.BLUE;
    }


    public static final String CONSTVALUE() { return "#"; }

}