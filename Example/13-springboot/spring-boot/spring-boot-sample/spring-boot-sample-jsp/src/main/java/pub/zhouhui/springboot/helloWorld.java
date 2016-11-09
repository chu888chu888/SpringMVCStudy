package pub.zhouhui.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Created by shane on 2016/10/30.
 */
@SpringBootApplication
public class helloWorld  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder  configure(SpringApplicationBuilder  application) {
        return application.sources(helloWorld.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(helloWorld.class, args);
    }
}
