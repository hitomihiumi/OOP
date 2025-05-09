import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:electricity.sqlite";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load SQLite JDBC driver");
        }
    }

    public Database() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String create = """
                CREATE TABLE IF NOT EXISTS electricity (
                    month TEXT NOT NULL,
                    year INTEGER NOT NULL,
                    previous REAL,
                    current REAL,
                    used REAL,
                    price REAL,
                    cost REAL
                )
            """;
            conn.createStatement().execute(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MonthlyData> loadAll() {
        List<MonthlyData> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM electricity");
            while (rs.next()) {
                MonthlyData d = new MonthlyData(MonthEnum.valueOf(rs.getString("month")), rs.getInt("year"));
                d.setPrevious(rs.getDouble("previous"));
                d.setCurrent(rs.getDouble("current"));
                d.setPrice(rs.getDouble("price"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveAll(List<MonthlyData> dataList) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.createStatement().execute("DELETE FROM electricity");
            String insert = "INSERT INTO electricity VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                for (MonthlyData d : dataList) {
                    ps.setString(1, d.getMonth().name());
                    ps.setInt(2, d.getYear());
                    ps.setDouble(3, d.getPrevious());
                    ps.setDouble(4, d.getCurrent());
                    ps.setDouble(5, d.getUsed());
                    ps.setDouble(6, d.getPrice());
                    ps.setDouble(7, d.getCost());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
