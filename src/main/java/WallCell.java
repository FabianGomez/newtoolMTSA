import java.awt.*;

public class WallCell  extends Cell{

    public WallCell(int row, int column){
        super(CONSTVALUE(),row,column);
    }
    @Override
    public String getValue() {
        return WallCell.CONSTVALUE();
    }
    @Override
    public String getLabelValue() {
        return CONSTLABELVALUE();
    }
    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    private static final String CONSTLABELVALUE() { return ""; }
    public static final String CONSTVALUE() { return "x"; }

}