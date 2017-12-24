package com.notification;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/RegisterKey")
public class RegisterKey extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public static Connection con = null;
    public static Statement stmt = null;
    String sql = null;
    String token = null;
    String requestMethod = null;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String jdbc_driver = "com.mysql.jdbc.Driver";
    	
    	request.setCharacterEncoding("utf-8");
        
        try{
        	Class.forName(jdbc_driver);
        	
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fcm","root","csedbadmin");
            stmt = con.createStatement();
            System.out.println("DB���� ����");
            
            System.out.println(request.getContentLength());
            token = request.getParameter("Token");
            
            if(token == null) {
                System.out.println("��ū���� ���� ���� �ʾҽ��ϴ�.");
            }else{
                // ��ū�� ���޽� ������ �Է��Ұ���
                sql = "INSERT INTO USERS(Token) VALUES('"+ token +"')";
                stmt.executeUpdate(sql);
            }
        }catch(Exception e){
            System.out.println("DB���� ����");
            e.printStackTrace();
        }
    }
 
}
