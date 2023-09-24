package util;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.util.*;

public class Reflections {

    public static void setFieldValue(final Object obj, final String fieldname, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldname);
        field.set(obj, value);
    }

    public static Object getFieldValue(final Object obj, final String fieldname) throws IllegalAccessException {
        final Field field = getField(obj.getClass(), fieldname);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }

    public static List getFieldObject(final Class<?> clazz, final String fieldName, final Object object) throws NoSuchFieldException, IllegalAccessException {
        Field field = null;

        field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        List<Object> list = new ArrayList<Object>();
        list.add(field.get(object));
        return list;
    }

    public static Map<String, Object> createMap(final String key, final Object val) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, val);
        return map;
    }

    public static  Remote getRemoteObject(Object object) throws Exception {

        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Map map1 = Reflections.createMap("handlerHashset", object);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(Target.class, map1);

        Remote remote = Remote.class.cast(Proxy.newProxyInstance(
                Remote.class.getClassLoader(),
                new Class[]{Remote.class}, handler));
        return remote;
    }

}
