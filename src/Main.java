import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {
        int optionSelection;

        //Basic interface for user interaction. In future this will
        //be the first step of interacting with the whole program
        System.out.println ("""
                    1 = manage books\n
                    2 = manage user account\n
                    3 = idk""");

        Scanner scanner = new Scanner(System.in);
        optionSelection = scanner.nextInt();

        //Switch statement will be expanded later
        switch (optionSelection) {
            case 1:
                bookManagement();
                break;
        }

        scanner.close();
    }

    //Below method allows interaction between classes Main and Book
    public static void bookManagement() throws SQLException {

        int optionSelection;

        String url = "jdbc:mysql://localhost:3306/books";
        String username = "adam";
        String password = "password";

        int book_id;
        String author;
        String title;
        String ISBN;

        //Connection set up (object connection is passed on as an argument
        //methods of the Book class) and a basic interface to allow
        //selection of options provided by the Book class
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("""
                1 = Check in
                2 = Check out
                3 = Browse""");
        Scanner scanner = new Scanner(System.in);
        optionSelection = scanner.nextInt();

        //Switch statement to be expanded with more options later
        switch (optionSelection) {
            case 2:
                Book browserObject = new Book();
                browserObject.browseBooks(connection);

                System.out.print("Type id of the book you want to borrow: ");
                book_id = scanner.nextInt();
                Book borrowBookObject = new Book(book_id);
                borrowBookObject.checkOutBook(connection);
                break;

            case 3:
                Book book = new Book();
                book.browseBooks(connection);
                break;

        }

    }
}

//LibraryManagementSystem has no functionality as of yet
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

class Book {
    //Fields declarations
    private int book_id;
    private String title;
    private String author;
    private String ISBN;

    //Constructors
    public Book() {/*empty constructor*/}

    public Book(int book_id) {
        this.book_id = book_id;
    }
    public Book(String authorOrTitle) {
        this.author = authorOrTitle;
        this.title = authorOrTitle;
    }

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }
    public Book(int book_id, String title, String author, String ISBN) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
    }

    //Getter methods
    public int getBook_id() {return book_id;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getISBN() {return ISBN;}


    public void browseBooks(Connection connection) {

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books;");
            int book_id;
            String author;
            String title;
            String ISBN;
            boolean isAvailable;

            System.out.println("Book database:");
            while (resultSet.next()) {
                book_id = resultSet.getInt("book_id");
                author = resultSet.getString("author");
                title = resultSet.getString("title");
                ISBN = resultSet.getString("ISBN");
                isAvailable = resultSet.getBoolean("is_available");

                System.out.print("book id: " + book_id + "  ||  ");
                System.out.print("author: " + author + "  ||  ");
                System.out.print("title: " + title + "  ||  ");
                System.out.print("ISBN: " + ISBN + "  ||  ");
                System.out.println("Available: " + isAvailable);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    public void checkOutBook(Connection connection) throws SQLException {

        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String query =
                    "UPDATE books " +
                            "SET is_available = FALSE " +
                            "WHERE book_id = " + book_id;

            statement.executeUpdate(query);
            connection.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}

class User {

}