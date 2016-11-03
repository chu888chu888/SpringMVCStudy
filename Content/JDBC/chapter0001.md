#JDBC简介

![](../../images/JDBC/00001.jpg)

## 1 JDBC是什么？

JDBC是用于执行SQL的解决方案,开发人员使用JDBC的标准接口,数据库厂商则对接口进行操作,开发人员无须接触底层数据库驱动的差异性.

JDBC标准主要分为二个部分:JDBC应用程序开发接口(Applicaction Developer Interface)以及JDBC驱动程序开发者接口(Driver Developer Interface).如果你的应用程序需要联机数据库,就是调用JDBC应用程序开发者接口,相关API主要在java.sql与javax.sql两个包中.

![](../../images/JDBC/00002.jpg)

## 2 JDBC驱动的种类

1. Type 1 JDBC-ODBC Bridge Driver
2. Type 2 Native API Driver
3. Type 3 JDBC-Net Driver
4. Type 4 Native Protocol Driver

**Type 4这种类型的驱动程序操作通常由数据库厂商直接提供,驱动程序操作会将JDBC调用转换为与数据库特定的网络协议,以与数据库进行沟通操作,这类的驱动我们用的比较多**

## 3 连接数据库步骤

基本数据库操作相关的JDBC接口或类是位于java.sql包中.要取得数据库联机,必须有几个动作:

* 注册Driver操作对象
* 取得Connection操作对象
* 关闭Connection操作对象

一个基本的DEMO

```

import org.testng.annotations.Test;
import java.lang.Exception;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestJdbc {
    public static final String url = "jdbc:mysql://localhost:3306/blog";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "";
    public Connection conn = null;
    public PreparedStatement pst = null;
    @Test
    public void ConnectionDemo() {
        //打开数据库
        try
        {
            //指定连接类型
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement("select * from user");//准备执行语句
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //关闭数据库
        try
        {
            this.conn.close();
            this.pst.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}

```

## 4 一个简单的例子(POJO+DAO+Service)

**POJO**

```
package chu.jdbc;

import java.io.Serializable;
import java.util.Date;


public class Blog implements Serializable {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private String username;
    private String password;


    public Blog()
    {

    }
    public Blog(String username,String password)
    {
        this.username=username;
        this.password=password;
    }

}

```



**DAO**

```
package chu.jdbc;


import java.sql.*;
import java.util.*;
import java.util.Date;


public class BlogDAO {
    private String url;
    private String user;
    private String passwd;

    public BlogDAO(String url, String user, String passwd) {
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }

    public void add(Blog blog) {
        try {
            Connection conn = DriverManager.getConnection(url, user, passwd);

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
            Connection connection = DriverManager.getConnection(url, user, passwd);
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

```


**Service Test**

```
package chu.jdbc;
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

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}

```

## 5 经过改造的支持缓冲池的(POJO+DAO+Service)

**POJO**

```
package chu.jdbc;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chuguangming on 16/8/26.
 */
public class Blog implements Serializable {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private String username;
    private String password;


    public Blog()
    {

    }
    public Blog(String username,String password)
    {
        this.username=username;
        this.password=password;
    }

}

```


**DAO**

```
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

```

**TEST**

```
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
            BlogDAO dao=new BlogDAO(new SimpleConnectionPoolDataSource());
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
            BlogDAO dao=new BlogDAO(new SimpleConnectionPoolDataSource());
            for(Blog tmp:dao.get())
            {
                System.out.printf("username:%s password:%s \n",tmp.getUsername(),tmp.getPassword());
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}

```

**POOL**

```
package chu.jdbc;

import java.util.*;
import java.io.*;
import java.sql.*;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class SimpleConnectionPoolDataSource implements DataSource {
    private Properties props;
    private String url;
    private String user;
    private String passwd;
    private int max; // 連接池中最大Connection數目
    private List<Connection> conns;

    public SimpleConnectionPoolDataSource()
            throws IOException, ClassNotFoundException {
        this("/jdbc.properties");
    }

    public SimpleConnectionPoolDataSource(String configFile)
            throws IOException, ClassNotFoundException {
        props = new Properties();
        props.load(this.getClass().getResourceAsStream(configFile));

        url = props.getProperty("cc.openhome.url");
        user = props.getProperty("cc.openhome.user");
        passwd = props.getProperty("cc.openhome.password");
        max = Integer.parseInt(props.getProperty("cc.openhome.poolmax"));

        conns = Collections.synchronizedList(new ArrayList<Connection>());
    }

    public synchronized Connection getConnection() throws SQLException {
        if (conns.isEmpty()) {
            return new ConnectionWrapper(
                    DriverManager.getConnection(url, user, passwd),
                    conns,
                    max
            );
        } else {
            return conns.remove(conns.size() - 1);
        }
    }

    private class ConnectionWrapper implements Connection {
        private Connection conn;
        private List<Connection> conns;
        private int max;

        public ConnectionWrapper(Connection conn, List<Connection> conns, int max) {
            this.conn = conn;
            this.conns = conns;
            this.max = max;
        }

        @Override
        public void close() throws SQLException {
            if (conns.size() == max) {
                conn.close();
            } else {
                conns.add(this);
            }
        }

        @Override
        public Statement createStatement() throws SQLException {
            return conn.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return conn.prepareCall(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return conn.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return conn.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            conn.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return conn.getAutoCommit();
        }

        @Override
        public void commit() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void rollback() throws SQLException {
            conn.rollback();
        }


        @Override
        public boolean isClosed() throws SQLException {
            return conn.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return conn.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            conn.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return conn.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            conn.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return conn.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            conn.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return conn.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return conn.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            conn.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return conn.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            conn.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            conn.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return conn.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return conn.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return conn.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            conn.rollback();
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            conn.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return conn.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return conn.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return conn.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return conn.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return conn.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return conn.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return conn.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return conn.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            conn.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            conn.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return conn.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return conn.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return conn.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return conn.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            conn.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return conn.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            conn.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            conn.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return conn.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return conn.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return conn.isWrapperFor(iface);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
```