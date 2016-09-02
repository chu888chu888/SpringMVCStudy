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
public class EncodingServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {
        request.setCharacterEncoding("UTF-8");
        String name=request.getParameter("nameGet");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out=response.getWriter();
        out.println(name);
    }
}
