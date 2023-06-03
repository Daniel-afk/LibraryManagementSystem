import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementSystem {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USERNAME = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public static void main(String[] args) {
        // Connect to the MySQL database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            System.out.println("Connected to the database");

            // Retrieve the list of available books
            List<Book> availableBooks = getAvailableBooks(connection);

            // Display the table of available books
            displayBooks(availableBooks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Book> getAvailableBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM books WHERE availability = true";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publicationDate = resultSet.getString("publication_date");
                boolean availability = resultSet.getBoolean("availability");

                Book book = new Book(title, author, publicationDate, availability);
                books.add(book);
            }
            return books;
        }
    }

    private static void displayBooks(List<Book> books) {
        System.out.println("Available Books:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-20s %-20s %-15s %s\n", "Title", "Author", "Publication Date", "Availability");
        System.out.println("--------------------------------------------------");
        for (Book book : books) {
            System.out.printf("%-20s %-20s %-15s %s\n", book.getTitle(), book.getAuthor(), book.getPublicationDate(), book.isAvailability());
        }
    }

    private static boolean validateUser(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // User found if there is a matching record


            }

        }
    }
}