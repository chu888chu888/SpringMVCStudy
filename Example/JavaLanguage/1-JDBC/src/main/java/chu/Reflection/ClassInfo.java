package chu.Reflection;

/**
 * Created by chuguangming on 16/8/30.
 */
import org.testng.annotations.Test;
public class ClassInfo {
    @Test
    public void TestBlogAdd() {
        Class clz=String.class;
        System.out.println("类名称:"+clz.getName());
        System.out.println("是否为接口:"+clz.isInterface());
        System.out.println("是否为基本类型:"+clz.isPrimitive());
        System.out.println("是否为数组对象:"+clz.isArray());
        System.out.println("父类名称:"+clz.getSuperclass().getName());

    }

}
