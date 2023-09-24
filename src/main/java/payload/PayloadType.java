package payload;

public interface PayloadType <T> {

    public T getObject(String cmd, Boolean flag, String... args) throws Exception;


}
