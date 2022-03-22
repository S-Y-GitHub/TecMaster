import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import enums.Language;
import enums.Save;
import enums.Theme;

public class Config{
    final private static File CONFIG_FILE=new File("tec_master.config");
    static{
        reloadConfig();
    }
    private static Map<Enum<Key>,Enum<?>> configs;
    private static void reloadConfig(){
        if(!CONFIG_FILE.exists()){
            createConfig();
        }
        try{
            BufferedReader reader=new BufferedReader(new FileReader(CONFIG_FILE));
            String line;
            configs=new HashMap<>();
            while((line=reader.readLine())!=null){
                String[] split=line.split(":");
                final Enum<Key> key;
                final Enum<?> value;
                switch(split[0]){
                    case "LANGUAGE":{
                        key=Key.LANGUAGE;
                        switch(split[1]){
                            case "JAPANESE":{
                                value=Language.JAPANESE;
                                break;
                            }
                            case "ENGLISH":{
                                value=Language.ENGLISH;
                                break;
                            }
                            default:{
                                continue;
                            }
                        }
                        break;
                    }
                    case "SAVE":{
                        key=Key.SAVE;
                        switch(split[1]){
                            case "MANUAL":{
                                value=Save.MANUAL;
                                break;
                            }
                            case "AUTO":{
                                value=Save.AUTO;
                                break;
                            }
                            default:{
                                continue;
                            }
                        }
                        break;
                    }
                    case "THEME":{
                        key=Key.THEME;
                        switch(split[1]){
                            case "LIGHT":{
                                value=Theme.LIGHT;
                                break;
                            }
                            case "DARK":{
                                value=Theme.DARK;
                                break;
                            }
                            case "AUTO":{
                                value=Theme.AUTO;
                                break;
                            }
                            default:{
                                continue;
                            }
                        }
                        break;
                    }
                    default:{
                        continue;
                    }
                }
                configs.put(key,value);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private static void createConfig(){
        try{
            CONFIG_FILE.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static enum Key{
        LANGUAGE,
        SAVE,
        THEME
    }
    public static Enum<?> get(Key key){
        Enum<?> value=configs.get(key);
        if(value==null){
            switch(key){
                case LANGUAGE:{
                    value=Language.getDefault();
                    break;
                }
                case SAVE:{
                    value=Save.getDefault();
                    break;
                }
                case THEME:{
                    value=Theme.getDefault();
                    break;
                }
            }
            set(key,value);
        }
        return value;
    }
    public static void set(Key key,Enum<?> value){
        configs.put(key,value);
        try{
            BufferedWriter writer=new BufferedWriter(new FileWriter(CONFIG_FILE));
            for(Enum<Key> k:configs.keySet()){
                writer.write(k.toString()+":"+configs.get(k));
                writer.newLine();
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
