package chu.jdbc;

/**
 * Created by chuguangming on 16/8/26.
 */
import org.testng.annotations.Test;
import java.lang.Exception;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
public class TestBlog {
    public static final String url = "jdbc:mysql://localhost:3306/blog?"+
                                     "useUnicode=true&characterEncoding=UTF8";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "";
    public Connection conn = null;
    public PreparedStatement pst = null;


    @Test
    public void TestBlogAdd() {
        //打开数据库
        try
        {
            BlogDAO dao=new BlogDAO(url,user,password);
            dao.add(new Blog("楚广明","123456"));

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void TestBlogGet() {
        //打开数据库
        try
        {
            BlogDAO dao=new BlogDAO(url,user,password);
            for(Blog tmp:dao.get())
            {
                System.out.printf("username:%s password:%s \n",tmp.getUsername(),tmp.getPassword());
            }

//            for(Iterator<Blog>  it=dao.get().iterator();it.hasNext();){
//                Blog value=it.next();
//                System.out.printf("username:%s password:%s",value.getUsername(),value.getPassword());
//            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
