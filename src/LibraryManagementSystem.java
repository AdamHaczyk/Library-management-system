import java.sql.*;

//LibraryManagementSystem has no functionality as of now.
class LibraryManagementSystem {

    public LibraryManagementSystem() {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "adam";
        String password = "password";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();


            String query = "SELECT * FROM books";
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Available books:");

            while (resultSet.next()) {
                int book_id = resultSet.getInt("book_id");
                String author = resultSet.getString("author");
                String title = resultSet.getString("title");
                String ISBN = resultSet.getString("ISBN");
                System.out.println("id: " + book_id + " author: " + author + " title: " + title);
            }


        }   catch (SQLException e) {
                System.out.println("Failed to connect to database");
                e.printStackTrace();
            }

    }

}



