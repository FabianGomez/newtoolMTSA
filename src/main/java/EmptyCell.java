import java.awt.*;

public class EmptyCell extends Cell{
    public EmptyCell(int row, int column){
        super(CONSTVALUE(),row,column);
    }
    @Override
    public String getValue() {
        return EmptyCell.CONSTVALUE();
    }
    @Override
    public String getLabelValue() {
        return EmptyCell.CONSTLABELVALUE();
    }
    @Override
    public Color getColor() {
        return Color.LIGHT_GRAY;
    }

    public static final String CONSTVALUE() {
        return "0";
    }
    public static final String CONSTLABELVALUE() {
        return "";
    }
}
