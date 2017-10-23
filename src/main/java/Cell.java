import java.awt.*;

public abstract  class Cell {
    protected Object value;
    private int row;
    private int column;

    public Cell(Object value, int row, int column){
        this.value = value;
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    public abstract Object getValue();
    public abstract Color getColor();
    public abstract String getLabelValue();
}
