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
 
@WebServlet("/SendMessage")
public class SendMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static Connection con = null;
    public static Statement stmt = null;
    ResultSet rs = null;
    
    ArrayList<String> token = new ArrayList<String>();              // token값을 ArrayList에 저장
    String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    // 메시지 고유 ID
    boolean SHOW_ON_IDLE = false;                                   // 앱 활성화 상태일때 보여줄것인지
    int LIVE_TIME = 1;                                              // 앱 비활성화 상태일때 FCM가 메시지를 유효화하는 시간
    int RETRY = 2;                                                  // 메시지 전송실패시 재시도 횟수
    
    String simpleApiKey = "AAAAFuSEfiQ:APA91bHylrJSSFN--RHK2TTl8s0oi4p3VPpgBU5NjqtyKA2diQeBPWDH3dFEr-kp3N5Xh1YwnVT1wkGlf2nabjT2jB5XILTRMZdaHZDAIJoR62tGA4HSqrQ1XUc0tfv27l-1ZySXLr_v"; //"AIzaSyBQepjtC55YCawR7oVyo6-5E_J2c6xcsBs";
    String gcmURL = "https://android.googleapis.com/fcm/send"; // "https://android.googleapis.com/fcm/send"; //https://fcm.googleapis.com/fcm/send
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	request.setCharacterEncoding("utf-8");
    	Connection conn = null;
    	PreparedStatement pstmt = null;

    	// 데이터베이스 연결관련정보를 문자열로 선언
    	String jdbc_driver = "com.mysql.jdbc.Driver";
    	String jdbc_url = "jdbc:mysql://localhost:3306/fcm";
    	
        String msg = request.getParameter("message");
        
        if(msg==null || msg.equals("")) msg="";
        
        // msg = new String(msg.getBytes("UTF-8"), "UTF-8");   // 메시지 한글깨짐 처리
         
        try {
        	Class.forName(jdbc_driver);
        	
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fcm","root","csedbadmin");
            
            stmt = con.createStatement();
            System.out.println("DB연결 성공");
            
            String sql = "select token from users";
            rs = stmt.executeQuery(sql);
            
            //모든 등록ID를 리스트로 묶음
            while(rs.next()){
                token.add(rs.getString("token"));
                System.out.println(rs.getString("token"));
            }
            con.close();
            
            Sender sender = new Sender(simpleApiKey);
            Message message = new Message.Builder()
            .collapseKey(MESSAGE_ID)
            .delayWhileIdle(SHOW_ON_IDLE)
            .timeToLive(LIVE_TIME)
            .addData("message",msg)
            .build();
            MulticastResult result1 = sender.send(message,token,RETRY);
            if (result1 != null) {
                List<Result> resultList = result1.getResults();
                for (Result result : resultList) {
                    System.out.println(result.getErrorCodeName()); 
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("DB연결 실패");
            e.printStackTrace();
        }
  
        PrintWriter out = response.getWriter();
        
        String htmlResponse = "<html>";
        htmlResponse += "<form action=/OurSubway/index_master.jsp method=post>";
        htmlResponse += "<input type=image src=/OurSubway/images/SuccessSendMessage.png name=Submit value=Submit align=absmiddle>";
        htmlResponse += "</form>";
        htmlResponse += "</html>";
        
        out.println(htmlResponse);
    }
 
}