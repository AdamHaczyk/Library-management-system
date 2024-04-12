import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
    private ResultSet getResultset(Connection connection) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM books;");

        return resultSet;
    }


    //This method is used to display the book database.
    //In the future it might get another String type argument, that will allow to
    //search for particular kinds of books only (like available only). I might
    //have a separate method for that though.
    public void browseBooks(Connection connection) throws SQLException {

            Statement statement = connection.createStatement();
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

    }
    //Below method allows to check books in and out depending on the arguments,
    //that are passed to it. It simply changes the availability column
    //in the db.
    public void checkInCheckOutBook(Connection connection, boolean isCheckOut) throws SQLException {
        connection.setAutoCommit(false);

        String checkOutQuery ="""
                    UPDATE books
                    SET is_available = FALSE
                    WHERE book_id = """
                    + book_id + ";";

        String checkInQuery ="""
                    UPDATE books
                    SET is_available = TRUE
                    WHERE book_id = """
                    + book_id + ";";

            try (Statement statement = connection.createStatement()) {

                if(isCheckOut) {
                    statement.executeUpdate(checkOutQuery);
                }
                if(!isCheckOut) {
                    statement.executeUpdate(checkInQuery);
                }

                connection.commit();

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }

    }
}

