public class ConnectionException extends Exception {
    ConnectionException() {
        super("Connection Instruction failed");
    }

    ConnectionException(String message) {
        super(message);
    }

}

public abstract class Database implements DatabaseIO {
    private String url;
    private String username;
    private String password;

    public String getUrl() {
        return this.url;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void executeQuery(String query) throws ExecutionException {
        if (this.getUsername() != "admin" || this.getPassword() != "admin")
            throw new ExecutionException("Invalid credentials");

        System.out.println("Executing query: " + query);
    }

    @Override
    public String toString() {
        return "Database{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}

interface DatabaseIO {
    // If you omit the public keyword java defaults to public
    void createConnection() throws ConnectionException;

    void executeQuery(String query) throws ExecutionException;

    void closeConnection() throws ConnectionException;
}

public class ExecutionException extends Exception {

    ExecutionException() {
        this("Execution of query failed");
    }

    ExecutionException(String message) {
        super(message);
    }
}

public class MongoDb extends Database {
    public MongoDb(String url, String username, String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    @Override
    public void createConnection() throws ConnectionException {
        if (Math.random() < 0.5)
            throw new ConnectionException("Connection failed");

        System.out.println("Connecting to MongoDB...");
    }

    @Override
    public void closeConnection() throws ConnectionException {
        if (Math.random() < 0.5)
            throw new ConnectionException("Connection failed");
        System.out.println("Closing MongoDB connection...");
    }

}

public class MYSQL extends Database {
    public MYSQL(String url, String username, String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    @Override
    public void createConnection() throws ConnectionException {
        if (Math.random() < 0.2)
            throw new ConnectionException("Connection failed");

        System.out.println("Connecting to MYSQL...");
    }

    @Override
    public void closeConnection() throws ConnectionException {
        if (Math.random() < 0.2)
            throw new ConnectionException("Connection failed");
        System.out.println("Closing MYSQL connection...");
    }
}

public class SQLServer extends Database {
    public SQLServer(String url, String username, String password) {
        setUrl(url);
        setUsername(username);
        setPassword(password);
    }

    @Override
    public void createConnection() throws ConnectionException {
        if (Math.random() < 0.7)
            throw new ConnectionException("Connection failed");

        System.out.println("Connecting to SQLServer...");
    }

    @Override
    public void closeConnection() throws ConnectionException {
        if (Math.random() < 0.7)
            throw new ConnectionException("Connection failed");
        System.out.println("Closing SQLServer connection...");
    }

}

public class TestDB {
    public static void main(String[] args) {
        Database db = new MongoDb("jdbc:mysql://localhost:3306/test", "admin", "admin");
        try {
            db.createConnection();
            db.executeQuery("SELECT * FROM users");
            db.closeConnection();
        } catch (ConnectionException | ExecutionException e) {
            e.printStackTrace();
        }

        Database db1 = new MYSQL("jdbc:mysql://localhost:3306/test", "admin", "admin");
        try {
            db1.createConnection();
            db1.executeQuery("SELECT * FROM users");
            db1.closeConnection();
        } catch (ConnectionException | ExecutionException e) {
            e.printStackTrace();
        }

        Database db2 = new SQLServer("jdbc:mysql://localhost:3306/test", "admin", "admin");
        try {
            db2.createConnection();
            db2.executeQuery("SELECT * FROM users");
            db2.closeConnection();
        } catch (ConnectionException | ExecutionException e) {
            e.printStackTrace();
        }

        Database db3 = new MYSQL("jdbc:mysql://localhost:3306/test", "root", "root");
        try {
            db3.createConnection();
            db3.executeQuery("SELECT * FROM users");
            db3.closeConnection();
        } catch (ConnectionException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}



