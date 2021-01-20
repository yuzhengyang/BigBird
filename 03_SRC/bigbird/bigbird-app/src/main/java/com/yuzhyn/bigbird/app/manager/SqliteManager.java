package com.yuzhyn.bigbird.app.manager;

import java.sql.*;

public class SqliteManager {
    public static void main(String[] arg) throws ClassNotFoundException, SQLException {
        System.out.println("database");
        Connection conn = null;
        ResultSet rs = null;
        Statement statement;
        Class.forName("org.sqlite.JDBC");//sqlite database name.
        conn = DriverManager.getConnection("jdbc:sqlite::resource:db/bigbird_main_db.sqlite3");
        statement = conn.createStatement();
        rs = statement.executeQuery("SELECT * FROM demo"); //this is name of database list
        while (rs.next()){
            System.out.println("--------------------");
            System.out.print("id:"+rs.getString("id"));
            System.out.print("    name:"+rs.getString("name"));
            System.out.println("    age:"+rs.getString("age"));
        }

    }
}
