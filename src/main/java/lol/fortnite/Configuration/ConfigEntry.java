package lol.fortnite.Configuration;

public class ConfigEntry {
    private String Key;
    private Object Value;

    public ConfigEntry(String key, Object value){
        Key = key;
        Value = value;
    }

    public String key(){return Key;}
    public Object value(){return Value;}
}
