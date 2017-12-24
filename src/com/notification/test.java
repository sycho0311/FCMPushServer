package com.notification;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
 
@WebServlet("/test")
public class test extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
	Connection conn = null;
	PreparedStatement pstmt = null;

	// 데이터베이스 연결관련정보를 문자열로 선언
	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost:3306/fcm";
	
	try{
		// JDBC 드라이버 로드
		Class.forName(jdbc_driver);

		// 데이터베이스 연결정보를 이용해 Connection 인스턴스 확보
		conn = DriverManager.getConnection(jdbc_url,"root","csedbadmin");

		// Connection 클래스의 인스턴스로 부터 SQL  문 작성을 위한 Statement 준비
		String sql = "insert into users values(?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,request.getParameter("id"));
		pstmt.setString(2,request.getParameter("token"));

		// username 값을 입력한 경우 sql 문장을 수행.
		if(request.getParameter("id") != null) {
			pstmt.executeUpdate();
		}
	}
	catch(Exception e) {
		System.out.println("DB연결 실패");
		System.out.println(e);
	}
	
    }
}
