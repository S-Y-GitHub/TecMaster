package enums;

public enum Theme{
    LIGHT,
    DARK,
    AUTO;
    public static Enum<Theme> getDefault(){
        return LIGHT;
    }
}
