package lol.fortnite.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
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
            String dir = file.getParent();
            new File(dir).mkdirs();
            FileWriter fileWriter = new FileWriter(file);
            for(int i = 0; i < json.length(); i++)
                fileWriter.write(json.charAt(i));
            fileWriter.close();
        }
        catch (Exception e){
            System.out.println("Failed to SaveToDisk!");
            System.out.println(e);
        }
    }

    public String ReadFromDisk(){
        try{
            final StringBuilder contentBuilder = new StringBuilder();
            File file = new File(ConfigLocation);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null){
                contentBuilder.append(currentLine).append("\n");
            }
            bufferedReader.close();
            fileReader.close();
            return contentBuilder.toString();
        }
        catch (Exception e){
            System.out.println("Failed to ReadFromDisk!");
            System.out.println(e);
            return "";
        }
    }
}