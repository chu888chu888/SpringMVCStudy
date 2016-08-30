package chu.Reflection;

import org.testng.annotations.Test;

import static java.lang.System.out;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by chuguangming on 16/8/30.
 */
class Some2 {
    public String test;
    private static int testint;

    static {
        System.out.println("静态代码区域");
    }

    public String test() {
        return "test";
    }
}

public class ClassInfo2 {
    @Test
    public void Test() throws ClassNotFoundException {
        Class clz = Class.forName("chu.Reflection.Some2", false, Some2.class.getClassLoader());
        System.out.println("已经载入Some2.class");
        showPackageInfo(clz);
        showClassInfo(clz);
        showFiledsInfo(clz);
        showConstructorsInfo(clz);
        showMethodsInfo(clz);

    }

    private static void showPackageInfo(Class clz) {
        Package p = clz.getPackage();
        System.out.printf("package: %s ; %n ", p.getName());
    }

    private static void showClassInfo(Class clz) {
        int modifier = clz.getModifiers();//获取类型修饰常数
        System.out.printf("class:%s %s %s", Modifier.toString(modifier),
                Modifier.isInterface(modifier) ? "interface" : "class",
                clz.getName());
    }

    private static void showFiledsInfo(Class clz) throws SecurityException {
        // 取得宣告的資料成員代表物件
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            // 顯示權限修飾，像是public、protected、private
            out.printf("\t%s %s %s;%n",
                    Modifier.toString(field.getModifiers()),
                    field.getType().getName(), // 顯示型態名稱
                    field.getName() // 顯示資料成員名稱
            );
        }
    }

    private static void showConstructorsInfo(Class clz) throws SecurityException {
        // 取得宣告的建構方法代表物件
        Constructor[] constructors = clz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            // 顯示權限修飾，像是public、protected、private
            out.printf("\t%s %s();%n",
                    Modifier.toString(constructor.getModifiers()),
                    constructor.getName() // 顯示建構式名稱
            );
        }
    }

    private static void showMethodsInfo(Class clz) throws SecurityException {
        // 取得宣告的方法成員代表物件
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            // 顯示權限修飾，像是public、protected、private
            out.printf("\t%s %s %s();%n",
                    Modifier.toString(method.getModifiers()),
                    method.getReturnType().getName(), // 顯示返回值型態名稱
                    method.getName() // 顯示方法名稱
            );
        }
    }
}
