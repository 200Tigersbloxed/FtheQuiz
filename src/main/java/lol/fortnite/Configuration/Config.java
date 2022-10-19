package lol.fortnite.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Objects;

public class Config {
    @Expose(serialize = false)
    public final transient String ConfigLocation;

    public Config(String configLocation){
        ConfigLocation = configLocation;
    }

    public String Serialize(){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        builder.setPrettyPrinting().serializeNulls();
        return gson.toJson(this);
    }

    public <T> void Deserialize(){
        String json = ReadFromDisk();
        try{
            if(Objects.equals(json, ""))
                throw new Exception("json is null!");
            Gson gson = new Gson();
            T instance = (T) gson.fromJson(json, this.getClass());
            for (Field fieldInstance : instance.getClass().getFields()){
                if(fieldInstance.getAnnotation(SerializedName.class) != null){
                    Field copyTo = this.getClass().getField(fieldInstance.getName());
                    if(copyTo != null){
                        copyTo.set(this, fieldInstance.get(instance));
                    }
                }
            }
            OnDeserialize();
        }
        catch (Exception e){
            System.out.println("Failed to Deserialize " + json);
            System.out.println(e);
        }
    }

    public void OnDeserialize(){}

    public void SaveToDisk(){
        try{
            String json = Serialize();
            File file = new File(ConfigLocation);
            Files.createDirectories(Path.of(file.getParent()));
            Files.writeString(Path.of(ConfigLocation), json);
        }
        catch (Exception e){
            System.out.println("Failed to SaveToDisk!");
            System.out.println(e);
        }
    }

    public String ReadFromDisk(){
        try{
            return Files.readString(Path.of(ConfigLocation));
        }
        catch (Exception e){
            System.out.println("Failed to ReadFromDisk!");
            System.out.println(e);
            return "";
        }
    }
}