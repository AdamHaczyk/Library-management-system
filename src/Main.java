public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

class LibraryManagementSystem {

}

class Book {
    //Fields declarations
    private String title;
    private String author;
    private String ISBN;

    //constructor

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
    }

    //getter methods
    public String getTitle() {return title;}

    public String getAuthor() {return author;}

    public String getISBN() {return ISBN;}
}

class User {

}