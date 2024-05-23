import java.sql.*;

class User {
    //Fields declarations
    private int user_id;
    private String first_name;
    private String last_name;
    private Timestamp user_since;

    //Constructors
    public User(int user_id) {
        this.user_id = user_id;
        fillDataFromSQL(user_id);
    }
    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        fillDataFromSQL(first_name, last_name);
    }

    public User(String first_name, String last_name, Timestamp user_since) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_since = user_since;
    }

    //This method is used to get the remaining information from sql
    //based on those passed to the constructor. It is overloaded
    //to accommodate all the constructors
    private void fillDataFromSQL(int user_id) {
        String query = """
                SELECT * FROM users
                WHERE user_id =""" + user_id;
        try {
            Connection connection = createLocalConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                first_name = resultSet.getString("first_name");
                last_name = resultSet.getString("last_name");
                user_since = resultSet.getTimestamp("user_since");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println(user_id + "\n" + first_name + "\n" + last_name + "\n" + user_since);
    }

    private void fillDataFromSQL(String first_name, String last_name) {
        String query = """
                SELECT * FROM users
                WHERE first_name =\"""" + first_name +
                "\"&& last_name =\"" + last_name +
                "\";";
        try {
            Connection connection = createLocalConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                user_id = resultSet.getInt("user_id");
                user_since = resultSet.getTimestamp("user_since");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println(user_id + "\n" + first_name + "\n" + last_name + "\n" + user_since);
    }

    //This method creates a connection used internally in this class
    private Connection createLocalConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "adam";
        String password = "password";

        Connection connection = DriverManager.getConnection(url, username, password);

        return connection;
    }

}