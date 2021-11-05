package com.moduo.seckill.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moduo.seckill.pojo.User;
import com.moduo.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
* 生成用户工具类
* @author Wu Zicong
* @create 2021-10-21 16:14
*/
@Slf4j
public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
        User user = new User();
        user.setId(13000000000L + i);
        user.setNickname("user" + i);
        user.setSlat("1ab2c3d4");
        user.setPassword(MD5Util.inputPassToDBPass("36bae1a6e93543f2299cbb4db7cdf730", user.getSlat()));
        users.add(user);
        }
        log.info("create user start");
        //插入数据库
        Connection conn = getConn();
        conn.setAutoCommit(false);
        String sql = "insert into t_user(id, nickname, password, slat, head, register_date,last_login_date,login_count)values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int j = 0; j < 20 ;j++){
            for (int i = 250*j; i < 250+250*j; i++) {
                User user = users.get(i);
                pstmt.setLong(1, user.getId());
                pstmt.setString(2, user.getNickname());
                pstmt.setString(3, user.getPassword());
                pstmt.setString(4, user.getSlat());;
                pstmt.setString(5, null);
                pstmt.setDate(6,new Date(new java.util.Date().getTime()));
                pstmt.setDate(7,new Date(new java.util.Date().getTime()));
                pstmt.setInt(8, 0);
                pstmt.addBatch();
                }
            pstmt.executeBatch();
            conn.commit();
            }
        pstmt.close();
        conn.close();
        log.info("insert into db");
        //登录，生成token
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\92349\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
        User user = users.get(i);
        URL url = new URL(urlString);
        HttpURLConnection co = (HttpURLConnection) url.openConnection();
        co.setRequestMethod("POST");
        co.setDoOutput(true);
        OutputStream out = co.getOutputStream();
        String params = "mobile=" + user.getId() + "&password=" +
                MD5Util.inputPassToFromPass("123456");
        out.write(params.getBytes());
        out.flush();
        InputStream inputStream = co.getInputStream();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte buff[] = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buff)) >= 0) {
        bout.write(buff, 0, len);
        }
        inputStream.close();

        bout.close();
        String response = new String(bout.toByteArray());
        ObjectMapper mapper = new ObjectMapper();
        RespBean respBean = mapper.readValue(response, RespBean.class);
      System.out.println(respBean);
        String userTicket = ((String) respBean.getObj());
        log.info("create userTicket : " , user.getId());
        String row = user.getId() + "," + userTicket;
        raf.seek(raf.length());
        raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
        System.out.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("over");
        }
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
}
    public static void main(String[] args) throws Exception {
           createUser(5000);
//        String s = MD5Util.inputPassToFromPass("123456");
//        System.out.println(s);
    }
}