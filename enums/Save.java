package enums;
public enum Save{
    MANUAL,
    AUTO;
    public static Enum<Save> getDefault(){
        return MANUAL;
    }
}