package com.chu;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by chuguangming on 16/8/31.
 */
@WebServlet(name = "GetHeaderInfo")
public class GetHeaderInfo extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out=response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>GetHeaderInfo</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>"+new java.util.Date()+"</h1>");
        //获取所有标头名称
        Enumeration<String> names=request.getHeaderNames();
        while(names.hasMoreElements())
        {
            String name=names.nextElement();
            out.println(name+":"+request.getHeader(name)+"<br>");
        }
        out.println("</body>");
        out.println("</html>");

    }
}
