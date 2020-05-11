import org.aeonbits.owner.Config;

public interface TestConfig extends Config {

    @Key("server.url")
    String url();

}
