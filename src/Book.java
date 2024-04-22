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
    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
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

        return statement.executeQuery("SELECT * FROM books;");
    }

}

