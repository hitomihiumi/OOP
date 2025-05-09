import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ElectricityUI ui = new ElectricityUI();
            ui.loadInitialData();
            ui.createAndShowGUI();
        });
    }
}
