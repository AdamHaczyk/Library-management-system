import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int optionSelection;

        //Basic interface for user interaction. In future this will
        //be the first step of interacting with the whole program
        System.out.println ("""
                    1 = manage books
                    2 = manage user account
                    3 = idk""");

        Scanner scanner = new Scanner(System.in);
        optionSelection = scanner.nextInt();

        //Switch statement will be expanded later
        switch (optionSelection) {
            case 1:
                try {
                    bookManagement();
                } catch (SQLException ex) {
                    System.err.println("SQLException occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
        }

        scanner.close();
    }

    //Below method allows interaction between classes Main and Book
    public static void bookManagement() throws SQLException {

        int optionSelection;
        boolean isCheckout;

        String url = "jdbc:mysql://localhost:3306/books";
        String username = "adam";
        String password = "password";

        int book_id;
        String author;
        String title;
        String ISBN;
        Book book = new Book();


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
            case 1:
                isCheckout = false;

                book.browseBooks(connection);

                System.out.print("Type the id of the book you want to return: ");
                book_id = scanner.nextInt();
                Book returnBook = new Book(book_id);

                try {
                    returnBook.checkInCheckOutBook(connection, isCheckout);

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            case 2:
                isCheckout = true;

                book.browseBooks(connection);

                System.out.print("Type the id of the book you want to borrow: ");
                book_id = scanner.nextInt();
                Book borrowBook = new Book(book_id);

                try {
                    borrowBook.checkInCheckOutBook(connection, isCheckout);

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            case 3:
                try {
                    book.browseBooks(connection);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;

        }

    }
}