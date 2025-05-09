import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

class ElectricityTableModel extends AbstractTableModel {
    private List<MonthlyData> data = new ArrayList<>();
    private final String[] columns = {"Місяць", "Рік", "Попередній", "Поточний", "Використано", "Ціна", "Вартість"};

    public void setData(List<MonthlyData> data) {
        this.data = data;
        fireTableDataChanged();
    }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int c) { return columns[c]; }

    @Override public Object getValueAt(int row, int col) {
        MonthlyData d = data.get(row);
        return switch (col) {
            case 0 -> d.getMonth();
            case 1 -> d.getYear();
            case 2 -> d.getPrevious();
            case 3 -> d.getCurrent();
            case 4 -> d.getUsed();
            case 5 -> d.getPrice();
            case 6 -> d.getCost();
            default -> null;
        };
    }
}