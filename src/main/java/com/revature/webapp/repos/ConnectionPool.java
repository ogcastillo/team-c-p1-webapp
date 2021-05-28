package com.revature.webapp.repos;

import com.revature.webapp.util.ConnectionFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

public class ConnectionPool{

    private Queue<Connection> connectionPool = new LinkedList<>();
    private Properties props = new Properties();

    //private static final ConnectionPool connectionPoolInstance = new ConnectionPool(System.getenv("num_conns"));
    //private static final ConnectionPool connectionPoolInstance = new ConnectionPool("3");

    private static final ConnectionPool connectionPoolInstance = new ConnectionPool();

    private ConnectionPool() {

        try{

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("application.properties");
            props.load(input);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.connectionPool = ConnectionFactory.getInstance().getConnections(props.getProperty("connections"),
                                                                             props.getProperty("host-url"),
                                                                             props.getProperty("username"),
                                                                             props.getProperty("password"));
    };

    public static ConnectionPool getConnectionPool(){
        return connectionPoolInstance;
    }

    public Connection pollFromConnectionPool() throws SQLException {
        Connection conn=null;

        if(connectionPool.peek()!=null){
            conn = connectionPool.poll();
        }else{
            throw new SQLException("No connection available!!!");
        }

        return conn;
    }

    public boolean addToConnectionPool(Connection conn){
        return connectionPool.add(conn);
    }

    public void closeConnections(){
        connectionPool.forEach( conn -> {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public int connectionsAvailable(){
        return connectionPool.stream().toArray().length;
    }

}

