import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DarkMode{
    public static boolean isDarkMode(){
        try{
            if(OSInfo.isWindows()){
                Process process=new ProcessBuilder("python","darkmode.py").start();
                BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
                return reader.readLine().equals("True");
            }else if(OSInfo.isMacOS()){
                Process process=new ProcessBuilder("swift","DarkMode.swift").start();
                BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
                return reader.readLine().equals("true");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}