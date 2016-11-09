package pub.zhouhui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by shane on 2016/10/30.
*/
 @SpringBootApplication
public class SpringBootSampleHibernate   extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder  application) {
        return application.sources(SpringBootSampleHibernate.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSampleHibernate.class, args);
    }
}
