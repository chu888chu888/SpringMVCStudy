package com.chu;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Enumeration;

/**
 * Created by chuguangming on 16/8/31.
 */
@MultipartConfig
@WebServlet(name = "Upload")
public class UploadServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

        /*
        Part part=request.getPart("photo");
        String filename=getFilename(part);
        writeTo(filename,part);
        System.out.println(filename);
        */
        request.setCharacterEncoding("UTF-8");
        Part part=request.getPart("photo");
        String filename=getFilename(part);
        part.write(filename);


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, java.io.IOException {

    }

    private String getFilename(Part part)
    {
        String header=part.getHeader("Content-Disposition");
        String filename=header.substring(header.indexOf("filename=\"")+10,header.lastIndexOf("\""));
        return filename;
    }

    private void writeTo(String filename,Part part) throws IOException,FileNotFoundException
    {
        InputStream in=part.getInputStream();
        OutputStream out=new FileOutputStream(filename);
        byte [] buffer =new byte[1024];
        int length=-1;
        while ((length=in.read(buffer))!=-1)
        {
            out.write(buffer,0,length);
        }
        in.close();
        out.close();
    }

}
