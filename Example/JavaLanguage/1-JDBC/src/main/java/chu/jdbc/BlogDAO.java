package chu.jdbc;

/**
 * Created by chuguangming on 16/8/26.
 */

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;


public class BlogDAO {
    private DataSource dataSource;

    public BlogDAO(DataSource dataSource) {
        this.dataSource=dataSource;
    }

    public void add(Blog blog) {
        try {
            Connection conn = dataSource.getConnection();

//            Statement statement=conn.createStatement();
//            String sql=String.format("insert into user(username,password) values('%s','%s')",
//                    blog.getUsername(),blog.getPassword());
//            statement.executeUpdate(sql);
            PreparedStatement statement=conn.prepareStatement("insert into user(username,password) values(?,?)");
            statement.setString(1,blog.getUsername());
            statement.setString(2,blog.getPassword());
            statement.executeUpdate();


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Blog> get() {
        List<Blog> blogs = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user ");
            while (resultSet.next()) {
                Blog blog = toBlog(resultSet);
                blogs.add(blog);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return blogs;
    }

    private Blog toBlog(ResultSet result) throws SQLException {
        Blog blog = new Blog();
        blog.setUsername(result.getString(2));
        blog.setPassword(result.getString(3));
        return blog;
    }

}
