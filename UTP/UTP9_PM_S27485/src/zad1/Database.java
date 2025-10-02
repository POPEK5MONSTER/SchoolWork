package zad1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Database {
    private String url;
    private TravelData travelData;

    public Database(String url, TravelData travelData) {
        this.url = url;
        this.travelData = travelData;
    }

    public void create() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String uid = "root";
        String pwd = "M@teusz55";


        try  {
            Class.forName(driverName);
            Connection connection = DriverManager.getConnection(url, uid, pwd);

            if (connection != null) {
                createTable(connection);
                insertData(connection);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS offers (" +
                "location VARCHAR(10), " +
                "country VARCHAR(255), " +
                "departure_date DATE, " +
                "return_date DATE, " +
                "place VARCHAR(20), " +
                "price DOUBLE, " +
                "currency VARCHAR(3)" +
                ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.execute();
        }
    }
    private void insertData(Connection connection) {
        List<String> descriptions = travelData.getOffersDescriptionsList("en_US", "yyyy-MM-dd");
        String insertSQL = "INSERT INTO offers VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            for (String description : descriptions) {
                String[] parts = description.split("\t");
                if (parts.length >= 7) { // Dodaj sprawdzenie, czy tablica parts ma co najmniej 7 elementów
                    preparedStatement.setString(1, parts[0]);
                    preparedStatement.setString(2, parts[1]);

                    // Poprawiona linia - używamy Timestamp zamiast sql.Date
                    preparedStatement.setTimestamp(3, new Timestamp(parseDate(parts[2]).getTime()));
                    preparedStatement.setTimestamp(4, new Timestamp(parseDate(parts[3]).getTime()));

                    preparedStatement.setString(5, parts[4]);
                    preparedStatement.setDouble(6, Double.parseDouble(parts[5]));
                    preparedStatement.setString(7, parts[6]);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return new Date(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void showGui() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Travel Offers");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JTable table = new JTable();
            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"pl_PL", "en_GB"});
            JComboBox<String> dateFormatComboBox = new JComboBox<>(new String[]{"yyyy-MM-dd"});

            JButton refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(e -> {
                String selectedLanguage = (String) languageComboBox.getSelectedItem();
                String selectedDateFormat = (String) dateFormatComboBox.getSelectedItem();

                List<String> offersList = travelData.getOffersDescriptionsList(selectedLanguage, selectedDateFormat);

                model.setColumnCount(0);
                model.setRowCount(0);

                model.addColumn("Country");
                model.addColumn("Departure Date");
                model.addColumn("Return Date");
                model.addColumn("Place");
                model.addColumn("Price");
                model.addColumn("Currency");

                for (String offer : offersList) {
                    String[] parts = offer.split(" ");
                    int count = 0;
                    if (parts.length > 6){
                        for (int i = 0; i < parts.length - 6; i++) {
                            parts[0] += " " + parts[i + 1];
                            count++;
                        }
                        for (int i = count ; i < parts.length - 1; i++) {
                            if (count == 2)
                                parts[i - 1] = parts[i + 1];
                            else
                                parts[i] = parts[i + 1];
                        }
                    }
                    model.addRow(parts);
                }
            });

            JPanel panel = new JPanel();
            panel.add(new JLabel("Language:"));
            panel.add(languageComboBox);
            panel.add(new JLabel("Date Format:"));
            panel.add(dateFormatComboBox);
            panel.add(refreshButton);

            frame.getContentPane().add(BorderLayout.NORTH, panel);
            frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(table));

            frame.setSize(800, 400);
            frame.setVisible(true);
        });
    }
}
