import java.awt.*;

public class DoorCell extends Cell{

    public int index;

    public DoorCell(int row, int column, int index){
        super(CONSTVALUE(),row,column);
        this.index = index;
    }
    @Override
    public String getValue() {
        return DoorCell.CONSTVALUE();
    }
    @Override
    public String getLabelValue() {
        return "" + getIndex();
    }
    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    public int getIndex(){return index;}

    public static final String CONSTVALUE() { return "#"; }

}