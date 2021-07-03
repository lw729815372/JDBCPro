package com.sdu.java.jdbcutils;

import org.junit.Test;

import java.sql.Connection;
import java.util.function.DoubleToIntFunction;

import static org.junit.Assert.*;

public class JDBCUtilsTest {

    @Test
    public void getConnection() {
    }

    @Test
    public void update() {
    }

    @Test
    public void closeResource() {
    }

    @Test
    public void getDruidConnection(){
        Connection druidConnection = JDBCUtils.getDruidConnection();
        System.out.println(druidConnection);
    }
}