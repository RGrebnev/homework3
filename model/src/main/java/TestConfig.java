import org.aeonbits.owner.Config;

public interface TestConfig extends Config {
    @Key("otus")
    String otus();
    @Key("yandexMarket")
    String yandexM();

}
