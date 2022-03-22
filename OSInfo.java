public class OSInfo{
    final static String OS_NAME=System.getProperty("os.name").toLowerCase();
    public static boolean isMacOS(){
        return OS_NAME.startsWith("mac");
    }
    public static boolean isWindows(){
        return OS_NAME.startsWith("windows");
    }
    public static boolean isLinux(){
        return OS_NAME.startsWith("linux");
    }
}
