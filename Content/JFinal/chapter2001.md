#JFinal开发环境配置

##一、安装 Maven
>Maven是一个项目构建和管理的工具，提供了帮助管理 构建、文档、报告、依赖、scms、发布、分发的方法。可以方便的编译代码、进行依赖管理、管理二进制库等等。  
Maven的好处在于可以将项目过程规范化、自动化、高效化以及强大的可扩展，利用 Maven自身及其插件还可以获得代码检查报告、单元测试覆盖率、实现持续集成等等。

###1. 下载 Maven
>[http://maven.apache.org/download.cgi（Maven官方下载地址）](http://maven.apache.org/download.cgi)

![](../../images/20/maven/1.png)

###2. 将下载后的压缩文件解压到任意目录，这里选择解压到 D:\Program Files目录下，并配置其环境变量（参考配置 JDK的环境变量）
配置 Maven环境变量前，先确认 “JAVA_HOME”这个环境变量是对应于JDK的安装目录。
![](../../images/20/maven/2.png)

配置 Maven的系统变量。  
![](../../images/20/maven/3.png)

验证配置是否正确。  
![](../../images/20/maven/4.png)

修改默认仓库路径。到 D:\Program Files\apache-maven-3.3.9\conf目录下打开 settings.xml文件，修改默认的仓库路径。
![](../../images/20/maven/5.png)

##二、安装 IDEA
####1. 下载 IDEA14或者 IDEA15
> [http://pan.baidu.com/s/1pLEixej](http://pan.baidu.com/s/1pLEixej)

####2. 鼠标双击安装程序开始安装
![](../../images/20/idea/1.png)

![](../../images/20/idea/2.png)

![](../../images/20/idea/3.png)

####3. 安装完成后打开IDEA，并完成激活
	License server address：
	http://idea.lanyus.com/

	备用的License server address：
	http://0.idea.lanyus.com
	http://1.idea.lanyus.com
	http://2.idea.lanyus.com
	 
![](../../images/20/idea/4.png)

###三、创建一个 JavaWeb工程项目
####1. 新建项目
点击 IDEA菜单栏左上角的 “File” 选项，在出现的下拉列表中选择第一项  “New”，在 “New” 选项的右边再选择 “Project” 选项打开 “New Project” 对话框。
![](../../images/20/idea/6.png)

设置 GroupId和 ArtifactId。  
![](../../images/20/idea/7.png)

设置 Maven的安装目录、settings.xml的文件位置和本地仓库的位置等信息。  
![](../../images/20/idea/8.png)

设置项目名称。  
![](../../images/20/idea/9.png)

项目创建完成。  
![](../../images/20/idea/10.png) 

####2. 在 pom.xml文件中添加 jar包依赖
![](../../images/20/idea/11.png)

>pom.xml文件说明：  
>
>project：pom.xml文件中的顶层元素；  
>
>modelVersion：指明 POM使用的对象模型的版本。这个值很少改动。  
>
>groupId：指明创建项目的组织或者小组的唯一标识。GroupId是项目的关键标识，典型的，此标识以组织的完全限定名来定义。比如，org.apache.maven.plugins是所有Maven插件项目指定的 groupId。  
>
>artifactId：指明此项目产生的主要产品的基本名称。项目的主要产品通常为一个 JAR文件。第二，象源代码包通常使用 artifactId作为最后名称的一部分。典型的产品名称使用这个格式： <artifactId>- <version>. <extension>(比如：myapp-1.0.jar)。
>   
>version：项目产品的版本号。Maven帮助你管理版本，可以经常看到 SNAPSHOT这个版本，表明项目处于开发阶段。  
>
>name：项目的显示名称，通常用于 maven产生的文档中。
>  
>url：指定项目站点，通常用于 maven产生的文档中。 
> 
>description：描述此项目，通常用于 maven产生的文档中。

要添加依赖我们要先到 maven仓库里面去查询。
>[Maven仓库地址：http://mvnrepository.com/](http://mvnrepository.com/)

首先我们要添加一些基本的依赖关系如： 
  
	<dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>

    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>

![](../../images/20/idea/12.png)

![](../../images/20/idea/13.png)

![](../../images/20/idea/14.png)

![](../../images/20/idea/15.png)

#####3. 配置项目
打开 “Project Structure”。
![](../../images/20/idea/16.png)

配置 “Project”。  
![](../../images/20/idea/17.png)

配置 “Modules”。  
![](../../images/20/idea/18.png)

配置 “Libraries”。 
 
>添加外部依赖的jar包。有时候将一些本地已存在的 jar包添加到了 src/main/webapp/WEB-INF/lib（该目录要手动创建）这个目录里，那么可以在这里设置将这些 jar包导进项目中而不需要通过在pom.xml中去添加这些jar包的依赖关系来导入。

![](../../images/20/idea/19.png)

选中存放 jar包的文件夹。  
![](../../images/20/idea/20.png)

配置 “Facets”。  
![](../../images/20/idea/21.png)

配置 “Artifacts”。
![](../../images/20/idea/22.png)

#####3. 配置Tomcat
打开配置。
![](../../images/20/idea/23.png)

添加新Tomcat Server配置。
![](../../images/20/idea/24.png)

![](../../images/20/idea/25.png)

![](../../images/20/idea/26.png)

![](../../images/20/idea/27.png)

![](../../images/20/idea/28.png)

启动 Tomcat。
![](../../images/20/idea/29)

正确启动后会默认显示index.jsp页面的内容
![](../../images/20/idea/30.png)

##四、简单搭建 JFinal框架（搭建 JFianl框架之前请先学习 JFinal官方手册）
>[JFinal官网：http://www.jfinal.com/](http://www.jfinal.com/)

>[详细语法说明请参考 JFinal官方手册：http://download.jfinal.com/upload/2.2/jfinal-2.2-manual.pdf](http://download.jfinal.com/upload/2.2/jfinal-2.2-manual.pdf)

####1. 搭建 JFinal项目步骤+说明
1. 一个 JFinal项目一般至少包括三个部分：配置（com.hithinksoft.config）、实体类(com.hithinksoft.model)、控制器（com.hithinksoft.controller）。
2. 在com.hithinksoft.config包中创建一个名为MyConfig（类名自定义）的类，它要继承 com.jfinal.config.JFinalConfig，并且要实现JFinalConfig中的五个抽象方法。这个类是必需的，它会对整个 web项目进行配置。
3. com.hithinksoft.model包下的实体类都要继承 com.jfinal.plugin.activerecord.Model。
4. com.hithinksoft.controller包下的控制器都要继承 com.jfinal.core.Controller。
5. 下面编写一个示例:
	- 定义两个实体类： com.hithinksoft.model.User.java和 com.hithinksoft.model.Blog.java;  
	- 定义两个控制器： com.hithinksoft.controller.UserController.java和 com.hithinksoft.controller.BlogController.java。
	- 定义页面：在 src\main\webapp\WEB-INF\pages\blogs下面定义四个页面，blogs.jsp、 blog.jsp、publish.jsp和 update.jsp。blogs.jsp页面时展示所有博客信息的，而 blog.jsp页面是展示某一条博客的详细信息的，publish.jsp页面是用来发布博客的，update.jsp页面是用来更改博客的；在 src\main\webapp\WEB-INF\pages\users下定义一个 login.jsp页面和 一个 register.jsp页面来进行登录和注册。

具体目录结构图：  
![](../../images/20/idea/j1.png)

####2. 添加依赖
	<dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.16</version>
    </dependency>

    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.0.8</version>
    </dependency>

    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.2</version>
    </dependency>

    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>mchange-commons-java</artifactId>
      <version>0.2.12</version>
    </dependency>

    <dependency>
      <groupId>com.jfinal</groupId>
      <artifactId>jfinal</artifactId>
      <version>2.2</version>
    </dependency>

####3. 配置 web.xml文件
在 web.xml中添加一下代码
	
	<filter>
		<filter-name>jfinalFilter</filter-name>
		<filter-class>com.jfinal.core.JFinalFilter</filter-class>
		<init-param>
			<param-name>configClass</param-name>
			<param-value>com.hithinksoft.config.MyConfig</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>jfinalFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

上面<filter>标签中的<filer-name>和<filter-mapping>的<filter-name>是一样的，<filter-name>是自定义的。<param-value>标签中的类就是上面的 MyConfig类。

####4. 创建 com.hithinksoft.config.MyConfig类，继承 com.jfinal.config.JFinalConfig并实现其中的方法

	package com.hithinksoft.config;

	import com.jfinal.config.*;

	public class MyConfig extends JFinalConfig {

		@Override
		public void configConstant(Constants constants) {
			
		}
	
		@Override
		public void configRoute(Routes routes) {
	
		}
	
		@Override
		public void configPlugin(Plugins plugins) {
	
		}
	
		@Override
		public void configInterceptor(Interceptors interceptors) {

		}
	
		@Override
		public void configHandler(Handlers handlers) {
	
		}
	}

####5. 验证项目的搭建是否成功
添加以下内容进行验证：  
1. 在 MyConfig类的 configRoute方法中添加一个路由，例如：routes.add("/", HelloController.class) （也可以是别的Controller类）。  
2. 在 HelloController.java中添加一个 index方法（方法名称自定义），在 index方法里面添加一条 renderJsp("index.jsp")跳转语句。  
3. 在 src\main\webapp\WEB-INF\目录下的 index.jsp页面中随便添加一些内容。
4. 启动Tomcat服务器，如果浏览器输入了 index.jsp页面的内容，则说明 JFinal项目的搭建成功。

####6. MyConfig类的具体内容
	public class MyConfig extends JFinalConfig {
	private static String VIEWURL = "/WEB-INF/pages/";
	
	@Override
	public void configConstant(Constants constants) {
		/*开发模式常量devMode设置为true，这样程序运行时会打印一些调试信息等*/
		constants.setDevMode(true);

		/*设置编码格式为UTF-8*/
		constants.setEncoding("UTF-8");

		/**
		 * 设置视图类型为JSP
		 * 如果页面中使用了FreeMarker，则应该写成：
		 * constants.setViewType(ViewType.FREE_MARKER);
		 * */
		constants.setViewType(ViewType.JSP);
	}  

	@Override
	public void configRoute(Routes routes) {
		routes.add("/", HelloController.class);
		/**
		 * 当在浏览器中访问 “http://localhost:8080/blog/users/login”时，
		 * 会映射到 UserController.java中的 login()方法。
		 * 第三个参数 MyConfig.VIEWURL是个常量，它的值是： "/WEB-INF/pages/"，
		 * 它表示Controller返回的视图的相对路径，
		 * 它的作用是：当调用了 UserController中的 login()方法的renderJsp("users/login.jsp")语句时，
		 * 代码会将“/WEB-INF/pages/”与renderJsp中的参数 “users/login.jsp”拼接起来得到的
		 * 路径作为即将打开的页面的路径。也就是说当在浏览器地址栏中输入了
		 * “http://localhost:8080/blog/users/login”之后，浏览器会访问到
		 * “/WEB-INF/pages/users/login.jsp”这个页面
		 */
		routes.add("/users", UserController.class, MyConfig.VIEWURL);
		routes.add("/blogs", BlogController.class, MyConfig.VIEWURL);
	}

	@Override
	public void configPlugin(Plugins plugins) {
		/**
		 * 通过 Prokit工具类加载db.txt文件，db.txt是自己创建的文件，里面填写了一些连接数据库时
		 * 必要的参数相似的也可以将其他一些配置信息（如configConstant()方法中的的devMode、视图
		 * 类型等等）放到一个文件中，然后通过这种方法来获取其中的参数。
		 * */
		PropKit.use("db.txt");

		/*从db.txt文件中获取以下四个属性值作为C3p0Plugin的参数来创建一个C3p0Plugin对象*/
		C3p0Plugin cp = new C3p0Plugin(PropKit.get("jdbcUrl"),
				PropKit.get("user"),
				PropKit.get("password").trim(),
				PropKit.get("driverClass"));

		/*将C3p0Plugin这个插件初始化完成后添加到工程项目中*/
		plugins.add(cp);

		/**
		 * C3p0Plugin是数据源，通过这个数据源创建一个ActiveRecordPlugin对象
		 * ActiveRecordPlugin是ActiveRecord的支持插件，ActiveRecord中定义了：
		 * addMapping(String tableName, Class<? extends Model> modelClass)方法
		 * 该方法建立了数据库表名到Model的映射关系，这样就可以通过调用提供的 API去操作数据库了
		 */
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		plugins.add(arp);
		arp.addMapping("user", User.class);
		arp.addMapping("blog", Blog.class);
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
	
	}

	@Override
	public void configHandler(Handlers handlers) {
	
	}

####7. 用户登录
	webapp/WEB-INF/pages/blogs/login.jsp

	<form method="post" action="/blog/users/loginUser"
          style="width:200px;margin:100px auto;">
        帐号：<input type="text" name="user.username"/><br/><br/>
        密码：<input type="password" name="user.password"/><br/><br/>
        <input type="submit" value="登录"/>
    </form>

填写正确信息并点击“登录”按钮后，代码会根据 “/blog/users/loginUser”映射到 UserController里面的 loginUser()方法里面继续执行。  
	
	com.hithinksoft.controller.UserController.java

	public void loginUser() {
		User user = getModel(User.class);
		String username = user.getStr("username");
		String password = user.getStr("password");
		/*验证登录的账户和密码是否匹配，如果匹配就获取该 User对象*/
		user = user.validate(username, password);
		if (user != null) {
			setSessionAttr("user", user);
			redirect("/blogs/blogs");
		} else {
			redirect("/users/login.jsp");
		}
	}
getModel用来接收页面表单域传递过来的model对象，表单域的名称以“modelName.attrName”方式命名，如帐号中 name属性的命令方式为 “user.username”，密码中 name属性的命名方式为 “user.password”。上述代码中的 getModel(User.class)表示接收页面表单域传递过来的 User实体对象，它里面保存有在页面填写的账户和密码。  
redirect("/blogs/blogs")方法的作用是跳转页面的，相当于在浏览器地址栏中输入了 “localhost:8080/blog/blogs/blogs”，代码会进入 com.hithinksoft.controller.BlogController中的 blogs()方法（注：redirect方法参数中的第一个“/”就代表根路径“localhost:8080/blog/”）。  
	
	com.hithinksoft.controller.BlogController

	public void blogs() {
		//查询所有的博客
		List<Blog> blogs = Blog.blogRepository.find("select * from blog");
		//将查询到的博客保存进 request作用域中，这样就可以在前端页面中获取到这些博客
		setAttr("blogs", blogs);
		renderJsp("blogs/blogs.jsp");
	}
上述代码最后是跳转到 “src/main/webapp/WEB-INF/pages/blogs/blogs.jsp”页面。  
renderJsp是 render系列方法中的一种，它的作用就是渲染不同类型的视图并返回给客户端，简单地说就是按某种视图类型在浏览器中打开一个已经存在的页面。它与前面的 redirect的区别是 redirect是用来跳转请求的，redirect的请求结果是请求服务器来打开一个页面，而 render系列方法就是打开一个页面。这跟 Servlet中的请求分发和重定向类似。

####8. 浏览博客

	com.hithinksoft.controller.BlogController  

	public void blog() {
		Integer blogId = getParaToInt(0);
		Blog blog = Blog.blogRepository.findById(blogId);
		setAttr("blog", blog);
		renderJsp("blogs/blog.jsp");
	}

跳转页面   
	
	webapp/WEB-INF/pages/blogs/blog.jsp  

	<div style="width:600px;margin:50px auto;border:1px solid 	#A9A9A9;padding:10px 20px;">
        <div style="padding:10px 0px 40px;border-bottom:1px solid #A9A9A9;text-align: center;position:relative;">
            <div>${blog.title}</div>
            <span style="position:absolute;right:40px;bottom:10px;">作者：&nbsp;${blog.getUser().username}</span>
        </div>
        <div style="padding:10px 0px;">
            ${blog.content}
        </div>
    </div>

####9. 发布博客

	webapp/WEB-INF/pages/blogs/publish.jsp  

	<form method="post" action="/blog/blogs/publishBlog/${user.id}" style="width:600px;margin:50px auto;border:1px solid #A9A9A9;padding:10px 20px;">
        标题：
        <input type="text" name="blog.title" value="${blog.title}" 
			style="width: 600px;padding: 4px;"/><br/><br/>

        内容：
        <textarea name="blog.content" style="width:600px;padding:4px;height:400px;">  
		    ${blog.content}
		</textarea>
        <input type="submit" value="发布"/>
    </form>
上述代码也是个表单，所以它的表单元素的 name属性值得写法与 login.jsp页面的写法一样都是 “modelName.attrName”。当点击 “发布”按钮时代码会根据 action中的 “/blog/blogs/publishBlog/${user.id}”映射到 BlogController类中的 publishBlog方法，action最后面的 “/${user.id}”是要传递的参数（具体语法参考 JFinal官方文档），这个 user就是在登录后调用 setSessionAttr("user", user)方法保存到 session作用域中的 user。  
 
	com.hithinksoft.controller.BlogController

	public void publishBlog() {
		//获取 action传递过来的参数
		Integer userId = getParaToInt(0);
		Blog blog = getModel(Blog.class);
		blog.set("user_id", userId);
		//将发布的博客持久化到数据库中
		blog.save();
		redirect("/blogs/blogs");
	}

####10. 修改已发布的博客  

	webapp/WEB-INF/pages/blogs/update.jsp  
	
	<form method="post" action="/blog/blogs/updateBlog/${blog.id}" 
          style="width:600px;margin:50px auto;border:1px solid #A9A9A9;padding:10px 20px;">
        标题：
        <input type="text" name="blog.title" value="${blog.title}" 
               style="width: 600px;padding: 4px;"/><br/><br/>

        内容：
        <textarea name="blog.content" style="width: 600px;padding: 4px;height: 400px;">
            ${blog.content}
        </textarea>
        <input type="submit" value="修改"/>
    </form>  
点击 “修改”按钮进入 BlogController中的 updateBlog方法，并且用被修改的博客的id作为参数。  

	com.hithinksoft.controller.BlogController
	
	public void updateBlog() {
		Integer blogId = getParaToInt(0);
		Blog blog = getModel(Blog.class);
		blog.set("id", blogId);
		blog.update();
		redirect("/blogs/blogs");
	}

####11. 删除已发布的博客

	com.hithinksoft.controller.BlogController

	public void delete() {
		Integer blogId = getParaToInt(0);
		Blog.blogRepository.deleteById(blogId);
		redirect("/blogs/blogs");
	}

###五、IDEA的常用快捷键
	Alt+回车 导入包,自动修正
	Ctrl+N   查找类
	Ctrl+Shift+N 查找文件
	Ctrl+Alt+L  格式化代码
	Ctrl+Alt+O 优化导入的类和包
	Alt+Insert 生成代码(如get,set方法,构造函数等)
	Ctrl+E或者Alt+Shift+C  最近更改的代码
	Ctrl+R 替换文本
	Ctrl+F 查找文本
	Ctrl+Shift+Space 自动补全代码
	Ctrl+空格 代码提示
	Ctrl+Alt+Space 类名或接口名提示
	Ctrl+P 方法参数提示
	Ctrl+Shift+Alt+N 查找类中的方法或变量
	Alt+Shift+C 对比最近修改的代码
	Shift+F6  重构-重命名
	Ctrl+Shift+先上键
	Ctrl+X 删除行
	Ctrl+D 复制行
	Ctrl+/ 或 Ctrl+Shift+/  注释（// 或者/*...*/ ）
	Ctrl+J  自动代码
	Ctrl+E 最近打开的文件
	Ctrl+H 显示类结构图
	Ctrl+Q 显示注释文档
	Alt+F1 查找代码所在位置
	Alt+1 快速打开或隐藏工程面板
	Ctrl+Alt+ left/right 返回至上次浏览的位置
	Alt+ left/right 切换代码视图
	Alt+ Up/Down 在方法间快速移动定位
	Ctrl+Shift+Up/Down 代码向上/下移动。
	F2 或Shift+F2 高亮错误或警告快速定位
	代码标签输入完成后，按Tab，生成代码。
	选中文本，按Ctrl+Shift+F7 ，高亮显示所有该文本，按Esc高亮消失。
	Ctrl+W 选中代码，连续按会有其他效果
	选中文本，按Alt+F3 ，逐个往下查找相同文本，并高亮显示。
	Ctrl+Up/Down 光标跳转到第一行或最后一行下
	Ctrl+B 快速打开光标处的类或方法  

