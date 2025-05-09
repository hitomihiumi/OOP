import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ElectricityRecord {
    private final List<MonthlyData> data = new ArrayList<>();

    public void add(MonthlyData d) {
        data.add(d);
    }

    public List<MonthlyData> getAll() {
        return data;
    }

    public double getTotalUsed() {
        return data.stream().mapToDouble(MonthlyData::getUsed).sum();
    }

    public double getTotalCost() {
        return data.stream().mapToDouble(MonthlyData::getCost).sum();
    }

    public List<MonthlyData> getFiltered(MonthEnum fromMonth, int fromYear, MonthEnum toMonth, int toYear) {
        return data.stream().filter(d -> {
            int ym = d.getYear() * 100 + d.getMonth().ordinal();
            int fromYM = fromYear * 100 + fromMonth.ordinal();
            int toYM = toYear * 100 + toMonth.ordinal();
            return ym >= fromYM && ym <= toYM;
        }).collect(Collectors.toList());
    }

    public void save(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (MonthlyData d : data) {
                pw.println(String.join(";", d.toCSVRow()));
            }
        }
    }

    public void load(File file) throws IOException {
        data.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                data.add(MonthlyData.from(parts));
            }
        }
    }
}