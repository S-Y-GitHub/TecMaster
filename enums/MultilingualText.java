package enums;

public enum MultilingualText{
    AN_ERROR_OCCURRED_IN_THE_SERIAL_COMMUNICATION_PROCESS,
    AN_ERROR_OCCURRED_IN_THE_USB_PORT_ACQUISITION_PROCESS,
    APPLY,
    DO_YOU_WANT_TO_CHANGE_THE_EXTENSION,
    DO_YOU_WANT_TO_REMOVE_THE_FILE,
    DO_YOU_WANT_TO_REMOVE_THE_FOLDER,
    DO_YOU_WANT_TO_SAVE_CHANGES,
    EXPLORER,
    FAILED_TO_ASSEMBLE__,
    FAILED_TO_CONNECT_TO_THE_TEC,
    FAILED_TO_CREATE_BINARY_FILE,
    FAILED_TO_CREATE_LIST_FILE,
    FAILED_TO_CREATE_THE_FILE,
    FAILED_TO_CREATE_THE_FOLDER,
    FAILED_TO_READ_FROM_FILE_,
    FAILED_TO_REMOVE_THE_FILE,
    FAILED_TO_REMOVE_THE_FOLDER,
    FAILED_TO_RENAME_THE_FILE,
    FAILED_TO_RENAME_THE_FOLDER,
    FAILED_TO_START_THE_SERIAL_COMMUNICATION_PROCESS,
    FAILED_TO_START_THE_USB_PORT_ACQUISITION_PROCESS,
    FAILED_TO_START_THE_WRITE_PROCESS,
    FAILED_TO_WRITE,
    FAILED_TO_WRITE_TO_FILE_,
    IMIDIATE_MODE_IS_NOT_AVAILABLE_FOR_COMMAND_,
    OTHER_SYNTAX_ERROR_,
    PARENTHESES_NOT_CLOSED_,
    PLEASE_ENTER_THE_FILE_NAME,
    PLEASE_ENTER_THE_FOLDER_NAME,
    RESULTS_WARE_STORED_IN__AND_,
    SELECT_THE_TEC_TO_OPERATE,
    SUCCESSFULLY_ASSEMBLED,
    THE_TEC_IS_NOT_CONNECTED,
    THE_FILE_WAS_NOT_FOUND,
    THIS_FILE_NAME_ALREADY_EXISTS,
    THIS_FOLDER_NAME_ALREADY_EXISTS,
    UNAUTHORIZED_OR_UNKNOWN_INDEX_REGISTER_,
    UNKNOWN_COMMAND_,
    UNKNOWN_LABEL_,
    UNKNOWN_OPERAND_,
    UNKNOWN_REGISTER
    ;
    public String getTxt(Language language){
        switch(language){
            case JAPANESE:{
                switch(this){
                    case AN_ERROR_OCCURRED_IN_THE_SERIAL_COMMUNICATION_PROCESS:{
                        return "シリアル通信プロセスでエラーが発生しました";
                    }
                    case AN_ERROR_OCCURRED_IN_THE_USB_PORT_ACQUISITION_PROCESS:{
                        return "USBポート取得プロセスでエラーが発生しました";
                    }
                    case APPLY:{
                        return "適用";
                    }
                    case DO_YOU_WANT_TO_CHANGE_THE_EXTENSION:{
                        return "拡張子を変更しますか?";
                    }
                    case DO_YOU_WANT_TO_REMOVE_THE_FILE:{
                        return "ファイルを削除しますか?";
                    }
                    case DO_YOU_WANT_TO_REMOVE_THE_FOLDER:{
                        return "フォルダを削除しますか?";
                    }
                    case DO_YOU_WANT_TO_SAVE_CHANGES:{
                        return "変更を保存しますか?";
                    }
                    case EXPLORER:{
                        return "エクスプローラ";
                    }
                    case FAILED_TO_ASSEMBLE__:{
                        return "アセンブルに失敗しました\n[%d行目]\t%16s\n%s";
                    }
                    case FAILED_TO_CONNECT_TO_THE_TEC:{
                        return "Tecへの接続に失敗しました";
                    }
                    case FAILED_TO_CREATE_BINARY_FILE:{
                        return "バイナリファイルの作成に失敗しました";
                    }
                    case FAILED_TO_CREATE_LIST_FILE:{
                        return "リストファイルの作成に失敗しました";
                    }
                    case FAILED_TO_CREATE_THE_FILE:{
                        return "ファイルの作成に失敗しました";
                    }
                    case FAILED_TO_CREATE_THE_FOLDER:{
                        return "フォルダの作成に失敗しました";
                    }
                    case FAILED_TO_READ_FROM_FILE_:{
                        return "ファイル:%s からの読み取りに失敗しました";
                    }
                    case FAILED_TO_REMOVE_THE_FILE:{
                        return "ファイルの削除に失敗しました";
                    }
                    case FAILED_TO_REMOVE_THE_FOLDER:{
                        return "フォルダの削除に失敗しました";
                    }
                    case FAILED_TO_RENAME_THE_FILE:{
                        return "ファイル名の変更に失敗しました";
                    }
                    case FAILED_TO_RENAME_THE_FOLDER:{
                        return "フォルダ名の変更に失敗しました";
                    }
                    case FAILED_TO_START_THE_SERIAL_COMMUNICATION_PROCESS:{
                        return "シリアル通信プロセスの起動に失敗しました";
                    }
                    case FAILED_TO_START_THE_USB_PORT_ACQUISITION_PROCESS:{
                        return "USBポート取得プロセスの起動に失敗しました";
                    }
                    case FAILED_TO_START_THE_WRITE_PROCESS:{
                        return "書き込みプロセスの起動に失敗しました";
                    }
                    case FAILED_TO_WRITE:{
                        return "書き込みに失敗しました";
                    }
                    case FAILED_TO_WRITE_TO_FILE_:{
                        return "ファイル:%s への書き込みに失敗しました";
                    }
                    case IMIDIATE_MODE_IS_NOT_AVAILABLE_FOR_COMMAND_:{
                        return "命令:%s ではイミディエイトモードを使用できません";
                    }
                    case OTHER_SYNTAX_ERROR_:{
                        return "その他の文法エラー: %s";
                    }
                    case PARENTHESES_NOT_CLOSED_:{
                        return "括弧が閉じられていません: %s";
                    }
                    case PLEASE_ENTER_THE_FILE_NAME:{
                        return "ファイル名を入力してください";
                    }
                    case PLEASE_ENTER_THE_FOLDER_NAME:{
                        return "フォルダ名を入力してください";
                    }
                    case RESULTS_WARE_STORED_IN__AND_:{
                        return "結果は[%s]と[%s]に格納されました";
                    }
                    case SELECT_THE_TEC_TO_OPERATE:{
                        return "操作を行うTecを選択してください";
                    }
                    case SUCCESSFULLY_ASSEMBLED:{
                        return "アセンブル成功";
                    }
                    case THE_TEC_IS_NOT_CONNECTED:{
                        return "Tecが接続されていません";
                    }
                    case THE_FILE_WAS_NOT_FOUND:{
                        return "ファイルが見つかりませんでした";
                    }
                    case THIS_FILE_NAME_ALREADY_EXISTS:{
                        return "このファイル名は既に存在しています";
                    }
                    case THIS_FOLDER_NAME_ALREADY_EXISTS:{
                        return "このフォルダ名は既に存在しています";
                    }
                    case UNAUTHORIZED_OR_UNKNOWN_INDEX_REGISTER_:{
                        return "許可されていない、または不明なインデクスレジスタ: %s";
                    }
                    case UNKNOWN_COMMAND_:{
                        return "不明な命令: %s";
                    }
                    case UNKNOWN_LABEL_:{
                        return "不明なラベル: %s";
                    }
                    case UNKNOWN_OPERAND_:{
                        return "不明なオペランド: %s";
                    }
                    case UNKNOWN_REGISTER:{
                        return "不明なレジスタ: %s";
                    }
                    default:
                        break;
                }
            }
            case ENGLISH:{
                switch(this){
                    case AN_ERROR_OCCURRED_IN_THE_SERIAL_COMMUNICATION_PROCESS:{
                        return "an error occurred in the serial communication process";
                    }
                    case AN_ERROR_OCCURRED_IN_THE_USB_PORT_ACQUISITION_PROCESS:{
                        return "an error occurred in the USB port acquisition process";
                    }
                    case APPLY:{
                        return "apply";
                    }
                    case DO_YOU_WANT_TO_CHANGE_THE_EXTENSION:{
                        return "Do you want to change the extension?";
                    }
                    case DO_YOU_WANT_TO_REMOVE_THE_FILE:{
                        return "Do you want to remove the file?";
                    }
                    case DO_YOU_WANT_TO_REMOVE_THE_FOLDER:{
                        return "Do you want to remove the folder?";
                    }
                    case DO_YOU_WANT_TO_SAVE_CHANGES:{
                        return "Do you want to save changes?";
                    }
                    case EXPLORER:{
                        return "explorer";
                    }
                    case FAILED_TO_ASSEMBLE__:{
                        return "failed to assemble\n[line %d]\t%16s\n%s";
                    }
                    case FAILED_TO_CONNECT_TO_THE_TEC:{
                        return "failed to connect to the Tec";
                    }
                    case FAILED_TO_CREATE_BINARY_FILE:{
                        return "failed to create binary file";
                    }
                    case FAILED_TO_CREATE_LIST_FILE:{
                        return "failed to create list file";
                    }
                    case FAILED_TO_CREATE_THE_FILE:{
                        return "failed to craate the file";
                    }
                    case FAILED_TO_CREATE_THE_FOLDER:{
                        return "failed to create the folder";
                    }
                    case FAILED_TO_READ_FROM_FILE_:{
                        return "failed to read from file:%s";
                    }
                    case FAILED_TO_REMOVE_THE_FILE:{
                        return "failed to rename the file";
                    }
                    case FAILED_TO_REMOVE_THE_FOLDER:{
                        return "failed to remove the folder";
                    }
                    case FAILED_TO_RENAME_THE_FILE:{
                        return "failed to rename the file";
                    }
                    case FAILED_TO_RENAME_THE_FOLDER:{
                        return "failed to rename the folder";
                    }
                    case FAILED_TO_START_THE_SERIAL_COMMUNICATION_PROCESS:{
                        return "failed to start the serial communication process";
                    }
                    case FAILED_TO_START_THE_USB_PORT_ACQUISITION_PROCESS:{
                        return "failed to start the USB port acquisition process";
                    }
                    case FAILED_TO_START_THE_WRITE_PROCESS:{
                        return "failed to start the write process";
                    }
                    case FAILED_TO_WRITE:{
                        return "failed to write";
                    }
                    case FAILED_TO_WRITE_TO_FILE_:{
                        return "failed to write to file:%s";
                    }
                    case IMIDIATE_MODE_IS_NOT_AVAILABLE_FOR_COMMAND_:{
                        return "imidiate mode is not available for command: %s";
                    }
                    case OTHER_SYNTAX_ERROR_:{
                        return "othre syntax error: %s";
                    }
                    case PARENTHESES_NOT_CLOSED_:{
                        return "parentheses not closed: %s";
                    }
                    case PLEASE_ENTER_THE_FILE_NAME:{
                        return "please enter the file name";
                    }
                    case PLEASE_ENTER_THE_FOLDER_NAME:{
                        return "please enter the folder name";
                    }
                    case RESULTS_WARE_STORED_IN__AND_:{
                        return "results ware stored in [%s] and [%s]";
                    }
                    case SELECT_THE_TEC_TO_OPERATE:{
                        return "select the Tec to operate";
                    }
                    case SUCCESSFULLY_ASSEMBLED:{
                        return "successfully assembled";
                    }
                    case THE_TEC_IS_NOT_CONNECTED:{
                        return "the Tec is not connected";
                    }
                    case THE_FILE_WAS_NOT_FOUND:{
                        return "the file was not found";
                    }
                    case THIS_FILE_NAME_ALREADY_EXISTS:{
                        return "this file name is already exists";
                    }
                    case THIS_FOLDER_NAME_ALREADY_EXISTS:{
                        return "this folder name is already exists";
                    }
                    case UNAUTHORIZED_OR_UNKNOWN_INDEX_REGISTER_:{
                        return "unauthorized or unknown index register: %s";
                    }
                    case UNKNOWN_COMMAND_:{
                        return "unknown command:%s";
                    }
                    case UNKNOWN_LABEL_:{
                        return "unknown label: %s";
                    }
                    case UNKNOWN_OPERAND_:{
                        return "unknown operand: %s";
                    }
                    case UNKNOWN_REGISTER:{
                        return "unknown register: %s";
                    }
                }
            }
        }
        return "this is bug";
    }
}