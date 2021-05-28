package com.revature.webapp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

//import static com.revature.assigments.p1.MyCustomORMDriver.NUMOFCONNECTIONS;

public class ConnectionFactory {

    private static ConnectionFactory connectionFactory; // Lazy Singleton
    //private Properties props = new Properties();

    static{
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConnectionFactory(){

        /*try{

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream input = loader.getResourceAsStream("application.properties");
            props.load(input);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static ConnectionFactory getInstance() {

        if (connectionFactory == null){
            connectionFactory = new ConnectionFactory();
        }

        return connectionFactory;
    }

    public Queue<Connection> getConnections(String connections, String hostUrl, String dbUsername , String dbPassword){
        Queue<Connection> conns = new LinkedList<>();
        Connection conn=null;
        try{
            for (int i = 0; i < Integer.parseInt(connections); i++) {
                conn= DriverManager.getConnection(hostUrl,dbUsername, dbPassword);
                conns.add(conn);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conns;
    }


}

