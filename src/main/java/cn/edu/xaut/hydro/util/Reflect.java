package cn.edu.xaut.hydro.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samantha on 2017/2/13.
 */
public class Reflect {

    public String getParamName(Object obj,String methodName,int index) {

        String fullClassName = obj.getClass().getName();


        String name = null;

        try {
            Class clazz = Class.forName(fullClassName);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().endsWith(methodName)) {
                    Class<?> [] paramTypes = method.getParameterTypes();
                    name = paramTypes[index].getName();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static void main(String[] args) {
        final List<String> list = new ArrayList<String>();
        list.add("123");
        list.add("1234");
        list.add("12345");
        Object proxy = Proxy.newProxyInstance(Class.class.getClassLoader(), new Class[]{java.util.List.class}, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(list, args);
            }
        });
        System.out.println(((List)proxy).get(2));
    }
}
