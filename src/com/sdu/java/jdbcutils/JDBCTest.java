package com.sdu.java.jdbcutils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sdu.java.bean.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

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

    @Test
    public void testTimeZone(){
        Connection connection = null;
        try {
            String sql = "update user set birth = ? where id = 13";
            connection = JDBCUtils.getConnection();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
            java.util.Date parse = simpleDateFormat.parse("1997-07-23");
            Date date1 = new Date(parse.getTime());
            Date date = new Date(System.currentTimeMillis());

            JDBCUtils.update(connection,sql,date1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,null,null);
        }
    }

    @Test
    public void druidTest() throws Exception {
        Properties properties = new Properties();
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        properties.load(resourceAsStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testQueryRunner(){
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection();
            String sql = "insert into user(name,age,birth) values(?,?,?)";
            int i = queryRunner.update(connection, sql, "国产007", 30, "1997-01-01");
            System.out.println("共"+i +"条数据插入");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Test
    public void queryTest(){
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection();
            String sql = "select name,age,birth from user where id <= ?";
            List<User> user = queryRunner.query(connection, sql, new BeanListHandler<>(User.class), 16);
            user.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 聚合函数查询测试
     */
    @Test
    public  void test(){
        Connection connection = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            connection = JDBCUtils.getConnection();
            String sql = "select count(*) from user";
            long i = (long) queryRunner.query(connection, sql, new ScalarHandler<>());
            System.out.println(i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
