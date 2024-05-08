import java.sql.*;
import java.util.Scanner;

class Library {

    public void runLibrary() {
        int optionSelection;

        //Basic interface for user interaction. In future this will
        //be the first step of interacting with the whole program
        System.out.println ("""
                    1 = manage books
                    2 = manage user account
                    3 = idk""");

        Scanner scanner = new Scanner(System.in);
        optionSelection = scanner.nextInt();

        //Switch statement will be expanded later. Or removed.
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

    //Below method allows interaction between classes Library and Book
    public void bookManagement() throws SQLException {

        int optionSelection;
        boolean isCheckout;

        String url = "jdbc:mysql://localhost:3306/books";
        String username = "adam";
        String password = "password";

        int book_id;
        String author;
        String title;
        String ISBN;
        String inputCleaner;        //Used to consume the '\n' that gets left over by scanner.nextInt() method.


        //Connection set up (object connection is passed on as an argument
        //methods of the Book class) and a basic interface to allow
        //selection of options provided by the Book class
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("""
                1 = Check in
                2 = Check out
                3 = Browse
                4 = Add a book
                5 = Delete a book
                9 = TESTING""");
        Scanner scanner = new Scanner(System.in);
        optionSelection = scanner.nextInt();
        inputCleaner = scanner.nextLine();

        //Switch statement to be expanded with more options later
        switch (optionSelection) {
            case 1:
                isCheckout = false;

                browseBooks(connection);

                System.out.print("Type the id of the book you want to return: ");
                book_id = scanner.nextInt();
                Book returnBook = new Book(book_id);

                try {
                    checkInCheckOutBook(connection, isCheckout, returnBook);

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            case 2:
                isCheckout = true;

                browseBooks(connection);

                System.out.print("Type the id of the book you want to borrow: ");
                book_id = scanner.nextInt();
                Book borrowBook = new Book(book_id);

                try {
                    checkInCheckOutBook(connection, isCheckout, borrowBook);

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            case 3:
                try {
                    browseBooks(connection);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;

            case 4:
                System.out.println("Insert book details");
                System.out.print("Author: ");
                author = scanner.nextLine();
                author = author.toUpperCase();
                System.out.print("Title: ");
                title = scanner.nextLine();
                title = title.toUpperCase();
                System.out.print("ISBN: ");
                ISBN = scanner.nextLine();
                try {
                    Book book1 = new Book(title, author, ISBN);
                    addBook(connection, book1);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                }
                break;

            case 5:
                try {
                    browseBooks(connection);
                    System.out.println("Select the ID of a book you wish to delete from the database");
                    book_id = scanner.nextInt();
                    inputCleaner = scanner.nextLine();
                    Book bookToRemove = new Book(book_id);
                    removeBook(connection, bookToRemove);

                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    System.out.println("Book removed successfully");
                }
                break;

            case 9: //FOR TESTING
                storeUnusedId(connection, 4);
                break;

        }

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
    public void checkInCheckOutBook(Connection connection, boolean isCheckOut, Book book) throws SQLException {
       connection.setAutoCommit(false);

        String checkOutQuery ="""
                    UPDATE books
                    SET is_available = FALSE
                    WHERE book_id ="""
                + book.getBook_id() + ";";

        String checkInQuery ="""
                    UPDATE books
                    SET is_available = TRUE
                    WHERE book_id ="""
                + book.getBook_id() + ";";

        try (Statement statement = connection.createStatement()) {

            if(isCheckOut) {
                statement.executeUpdate(checkOutQuery);
            }
            if(!isCheckOut) {
                statement.executeUpdate(checkInQuery);
            }

            connection.commit();

        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }

    }

    public void addBook(Connection connection, Book book) throws SQLException {
        connection.setAutoCommit(false);
        int localBookID = getEmptyID(connection);

        String addQuery = """
                INSERT INTO books
                (author, title, ISBN, is_available)
                VALUES (\"""" +
                book.getAuthor() + "\", \"" +
                book.getTitle() + "\", \"" +
                book.getISBN() + "\", " +
                "true);";

        String addWithIDQuery = """
                INSERT INTO books
                (book_id, author, title, ISBN, is_available)
                VALUES (\"""" +
                localBookID + "\", \"" +
                book.getAuthor() + "\", \"" +
                book.getTitle() + "\", \"" +
                book.getISBN() + "\", " +
                "true);";

        try {
            Statement statement = connection.createStatement();

            if(localBookID != 0) statement.executeUpdate(addWithIDQuery);
            else statement.executeUpdate(addQuery);

        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //This method removes a book selected by the user. It also changes
    //the auto_increment value in books table if the position
    //with the highest book_id was removed and stores the id of removed
    //position to separate table free_ids, which will allow to use those
    //ids in the future.
    public void removeBook(Connection connection, Book book) throws SQLException {
        connection.setAutoCommit(false);

        String removeQuery = """
                DELETE FROM books
                WHERE book_id = """ + book.getBook_id();

        String auto_incrementQuery = """
            ALTER TABLE books
            AUTO_INCREMENT = """ + book.getBook_id();

        String getAuto_IncrementQuery = """
                SELECT `AUTO_INCREMENT`
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = 'books'
                AND TABLE_NAME = 'books';""";


        try {
            Statement statement = connection.createStatement();
            int localBookID = book.getBook_id();
            storeUnusedId(connection, localBookID);

            statement.executeUpdate(removeQuery);

            ResultSet resultSet = statement.executeQuery(getAuto_IncrementQuery);
            resultSet.next();
            System.out.println("AUTO_INCREMENT = " + resultSet.getInt("AUTO_INCREMENT"));


            if(resultSet.getInt("AUTO_INCREMENT") == (localBookID + 1)) {
                statement.executeUpdate(auto_incrementQuery);
                System.out.println("this works right here");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void storeUnusedId(Connection connection, int idToStore) throws SQLException {
        System.out.println("idToStore = " + idToStore);
        connection.setAutoCommit(false);
        String localQuery = """
                INSERT INTO free_ids
                VALUES (""" + idToStore + ");";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(localQuery);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    //This method gets the first (FIFO) free id from table free_ids,
    //returns its value and removes it from the table.
    public int getEmptyID(Connection connection) throws SQLException{
        int localBookID;
        String deleteQuery;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM free_ids;");

            if (!resultSet.next()) return 0;
            localBookID = resultSet.getInt("free_id");

            deleteQuery = """
                    DELETE FROM free_ids
                    WHERE free_id = """ + localBookID;


            connection.setAutoCommit(false);
            try {
                statement.executeUpdate(deleteQuery);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
            return localBookID;

    }

}



