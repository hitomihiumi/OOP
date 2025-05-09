import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.io.File;


class ElectricityUI {
    private final ElectricityRecord record = new ElectricityRecord();
    private final ElectricityTableModel tableModel = new ElectricityTableModel();
    private final JTable table = new JTable(tableModel);
    private final JFrame frame = new JFrame("Облік електроенергії");
    private final Database db = new Database();

    public void createAndShowGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        JPanel buttons = new JPanel();

        JButton add = new JButton("Додати дані");
        JButton filter = new JButton("Фільтр"), showAll = new JButton("Показати все");
        JButton saveFile = new JButton("Зберегти"), loadFile = new JButton("Завантажити");
        JButton saveDB = new JButton("Зберегти в БД"), loadDB = new JButton("Завантажити з БД");

        add.addActionListener(e -> showAddDialog());
        filter.addActionListener(e -> showFilterDialog());
        showAll.addActionListener(e -> tableModel.setData(record.getAll()));

        saveFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV and TXT files", "csv", "txt"));
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    record.save(fc.getSelectedFile());
                } catch (IOException ex) {
                    showError("Помилка збереження");
                }
            }
        });

        loadFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV and TXT files", "csv", "txt"));
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    record.load(fc.getSelectedFile());
                    tableModel.setData(record.getAll());
                } catch (IOException ex) {
                    showError("Помилка завантаження");
                }
            }
        });

        saveDB.addActionListener(e -> {
            try {
                db.saveAll(record.getAll());
                JOptionPane.showMessageDialog(frame, "Дані збережено в БД");
            } catch (Exception ex) {
                showError("Помилка збереження в БД");
            }
        });

        loadDB.addActionListener(e -> {
            try {
                tableModel.setData(db.loadAll());
                JOptionPane.showMessageDialog(frame, "Дані завантажено з БД");
            } catch (Exception ex) {
                showError("Помилка завантаження з БД");
            }
        });

        buttons.add(add);
        buttons.add(filter);
        buttons.add(showAll);
        buttons.add(saveFile);
        buttons.add(loadFile);
        buttons.add(saveDB);
        buttons.add(loadDB);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void loadInitialData() {
        try {
            File csv = new File("data.csv.csv");
            File txt = new File("data.csv.txt");

            tableModel.setData(db.loadAll());

            if (tableModel.getRowCount() == 0) {
                if (csv.exists()) {
                    record.load(csv);
                    tableModel.setData(record.getAll());
                } else if (txt.exists()) {
                    record.load(txt);
                    tableModel.setData(record.getAll());
                }
            }
        } catch (IOException e) {
            System.err.println("Не вдалося завантажити початкові дані: " + e.getMessage());
        }
    }

    private void showAddDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JComboBox<MonthEnum> monthBox = new JComboBox<>(MonthEnum.values());
        JTextField yearField = new JTextField();
        JTextField prevField = new JTextField();
        JTextField currField = new JTextField();
        JTextField priceField = new JTextField();

        panel.add(new JLabel("Місяць:")); panel.add(monthBox);
        panel.add(new JLabel("Рік:")); panel.add(yearField);
        panel.add(new JLabel("Попередній показник:")); panel.add(prevField);
        panel.add(new JLabel("Поточний показник:")); panel.add(currField);
        panel.add(new JLabel("Ціна за кВт·год:")); panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Додавання запису",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                MonthEnum month = (MonthEnum) monthBox.getSelectedItem();
                int year = Integer.parseInt(yearField.getText());
                double prev = Double.parseDouble(prevField.getText());
                double curr = Double.parseDouble(currField.getText());
                double price = Double.parseDouble(priceField.getText());

                MonthlyData d = new MonthlyData(month, year);
                d.setPrevious(prev);
                d.setCurrent(curr);
                d.setPrice(price);
                record.add(d);
                tableModel.setData(record.getAll());
            } catch (NumberFormatException e) {
                showError("Некоректні дані!");
            }
        }
    }

    private void showFilterDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JComboBox<MonthEnum> fromMonth = new JComboBox<>(MonthEnum.values());
        JComboBox<MonthEnum> toMonth = new JComboBox<>(MonthEnum.values());
        JTextField fromYear = new JTextField();
        JTextField toYear = new JTextField();

        panel.add(new JLabel("Від місяця:")); panel.add(fromMonth);
        panel.add(new JLabel("Рік від:")); panel.add(fromYear);
        panel.add(new JLabel("До місяця:")); panel.add(toMonth);
        panel.add(new JLabel("Рік до:")); panel.add(toYear);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Фільтр періоду",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                MonthEnum m1 = (MonthEnum) fromMonth.getSelectedItem();
                int y1 = Integer.parseInt(fromYear.getText());
                MonthEnum m2 = (MonthEnum) toMonth.getSelectedItem();
                int y2 = Integer.parseInt(toYear.getText());
                List<MonthlyData> filtered = record.getFiltered(m1, y1, m2, y2);
                tableModel.setData(filtered);
            } catch (Exception ex) {
                showError("Помилка фільтрації");
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }
}