package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflections {

    public static void setFieldValue(final Object obj, final String fieldname, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldname);
        field.set(obj, value);
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

}
