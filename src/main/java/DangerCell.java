import java.awt.*;

public class DangerCell extends Cell{
    public DangerCell(int row, int column){
        super(CONSTVALUE(),row,column);
    }
    @Override
    public String getValue() {
        return DangerCell.CONSTVALUE();
    }
    @Override
    public String getLabelValue() {
        return CONSTLABELVALUE();
    }
    @Override
    public Color getColor() {
        return Color.RED;
    }

    public static final String CONSTLABELVALUE() { return ""; }
    public static final String CONSTVALUE() { return "!"; }

}
