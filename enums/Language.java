package enums;
public enum Language{
    JAPANESE,
    ENGLISH;
    public String toStringInThisLanguage(){
        switch(this){
            case JAPANESE:return "日本語";
            case ENGLISH:return "English";
            default:return null;
        }
    }
    public static Enum<Language> getDefault(){
        return JAPANESE;
    }
}