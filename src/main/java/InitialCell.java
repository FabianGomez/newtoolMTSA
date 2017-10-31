import java.awt.*;

public class InitialCell extends Cell{
    public InitialCell(int row, int column){
        super(CONSTVALUE(),row,column);
    }
    @Override
    public String getValue() {
        return InitialCell.CONSTVALUE();
    }

    @Override
    public String getLabelValue() {
        return CONSTLABELVALUE();
    }
    @Override
    public Color getColor() {
        return Color.GRAY;
    }

    private static final String CONSTLABELVALUE() {
        return CONSTVALUE();
    }
    private static final String CONSTVALUE() {
        return "@";
    }
}
