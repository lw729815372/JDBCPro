package com.sdu.java.jdbcutils;

import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTest {
    @Test
    public void transactionTest(){
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql1 = "update account set balance = balance - 100 where id = ?";
            String sql2 = "update account set balance = balance + 100 where id = ?";
            connection.setAutoCommit(false);
            JDBCUtils.update(connection,sql1,1);

            JDBCUtils.update(connection,sql2,2);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(connection,null,null);
        }
    }
}
