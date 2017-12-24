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

	// �����ͺ��̽� ������������� ���ڿ��� ����
	String jdbc_driver = "com.mysql.jdbc.Driver";
	String jdbc_url = "jdbc:mysql://localhost:3306/fcm";
	
	try{
		// JDBC ����̹� �ε�
		Class.forName(jdbc_driver);

		// �����ͺ��̽� ���������� �̿��� Connection �ν��Ͻ� Ȯ��
		conn = DriverManager.getConnection(jdbc_url,"root","csedbadmin");

		// Connection Ŭ������ �ν��Ͻ��� ���� SQL  �� �ۼ��� ���� Statement �غ�
		String sql = "insert into users values(?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,request.getParameter("id"));
		pstmt.setString(2,request.getParameter("token"));

		// username ���� �Է��� ��� sql ������ ����.
		if(request.getParameter("id") != null) {
			pstmt.executeUpdate();
		}
	}
	catch(Exception e) {
		System.out.println("DB���� ����");
		System.out.println(e);
	}
	
    }
}
