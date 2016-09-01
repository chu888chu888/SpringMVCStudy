import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by chuguangming on 16/8/31.
 */
@WebServlet(name = "SimpleServlet")
public class SimpleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out=response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>SimpleServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>"+new java.util.Date()+"</h1>");
        out.println("<h1>"+request.getRequestURI()+"</h1>");
        out.println("<h1>"+request.getContextPath()+"</h1>");
        out.println("<h1>"+request.getServletPath()+"</h1>");
        out.println("</body>");
        out.println("</html>");


    }
}
