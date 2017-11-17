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
        return CONSTLABELVALUE();
    }
    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    private static final String CONSTLABELVALUE() { return DoorCell.CONSTVALUE(); }
    public static final String CONSTVALUE() { return "#"; }

}