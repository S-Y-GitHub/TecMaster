import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import enums.Language;
import enums.MultilingualText;
import enums.Save;
import enums.Theme;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TecMaster extends Application{
    public static void main(String[] args){
        launch(args);
    }

    private int iconSize=20;
    private Language language;
    private Theme theme;
    private boolean autoSave;

    private String python;

    private Stage owner;
    private TreeView<String> tvExplorer;
    private TabPane tabPane;
    private Button btnWA,btnShaping,btnShell,btnSetting;
    private ImageView ivWrite,ivAssemble,ivShaping,ivDummy;
    private ContextMenu cmOption;
    
    @Override
    public void start(Stage stage){
        language=(Language)Config.get(Config.Key.LANGUAGE);
        theme=(Theme)Config.get(Config.Key.THEME);
        autoSave=(Save)Config.get(Config.Key.SAVE)==Save.AUTO;

        if(OSInfo.isWindows()){
            python="python";
        }else{
            python="python3";
        }

        owner=stage;

        Label labelExplorer=new Label(MultilingualText.EXPLORER.getTxt(language));
        labelExplorer.setFont(new Font(iconSize));

        Button btnSelect=new Button();
        btnSelect.setPrefWidth(iconSize);
        btnSelect.setOnAction(e->selectDirectory());

        ImageView ivFolder=new ImageView("./images/folder.png");
        ivFolder.setFitWidth(iconSize);
        ivFolder.setPreserveRatio(true);

        btnSelect.setGraphic(ivFolder);

        BorderPane bpExplorerBar=new BorderPane();
        bpExplorerBar.getStyleClass().add("bp-explorer-bar");
        bpExplorerBar.setLeft(labelExplorer);
        bpExplorerBar.setRight(btnSelect);

        tvExplorer=new TreeView<>();

        cmOption=new ContextMenu();
        tvExplorer.setContextMenu(cmOption);
        tvExplorer.setOnContextMenuRequested(
            e->{
                cmOption.hide();
                cmOption=createContextMenu();
                cmOption.show(tvExplorer,e.getScreenX(),e.getScreenY());
                e.consume();
            }
        );
        tvExplorer.setOnMouseClicked(
            e->{
                if(e.getButton()!=MouseButton.SECONDARY){
                    cmOption.hide();
                }
            }
        );

        ImageView ivSetting=new ImageView("./images/setting.png");
        ivSetting.setFitWidth(iconSize);
        ivSetting.setPreserveRatio(true);

        btnSetting=new Button();
        btnSetting.setMaxWidth(Double.MAX_VALUE);
        btnSetting.setGraphic(ivSetting);
        btnSetting.setOnAction(
            e->{
                btnSetting.setDisable(true);
                openSettings();
            }
        );

        HBox hbSetting=new HBox(btnSetting);

        BorderPane bpExplorer=new BorderPane();
        bpExplorer.setTop(bpExplorerBar);
        bpExplorer.setCenter(tvExplorer);
        bpExplorer.setBottom(hbSetting);
        bpExplorer.getStyleClass().add("bp-explorer");

        tabPane=new TabPane();

        ivDummy=new ImageView("./images/dummy.png");
        ivDummy.setFitWidth(iconSize);
        ivDummy.setPreserveRatio(true);

        btnWA=new Button();
        btnWA.setMaxWidth(Double.MAX_VALUE);
        btnWA.setDisable(true);
        btnWA.setGraphic(ivDummy);

        ivWrite=new ImageView("./images/write.png");
        ivWrite.setFitWidth(iconSize);
        ivWrite.setPreserveRatio(true);

        ivAssemble=new ImageView("./images/binary.png");
        ivAssemble.setFitWidth(iconSize);
        ivAssemble.setPreserveRatio(true);

        btnShaping=new Button();
        btnShaping.setMaxWidth(Double.MAX_VALUE);
        btnShaping.setDisable(true);
        btnShaping.setGraphic(ivDummy);

        ivShaping=new ImageView("./images/star.png");
        ivShaping.setFitWidth(iconSize);
        ivShaping.setPreserveRatio(true);

        btnShell=new Button();
        btnShell.setMaxWidth(Double.MAX_VALUE);
        btnShell.setOnAction(
            e->{
                btnShell.setDisable(true);
                launchShell();
            }
        );

        ImageView ivShell=new ImageView("./images/shell.png");
        ivShell.setFitWidth(iconSize);
        ivShell.setPreserveRatio(true);

        btnShell.setGraphic(ivShell);

        VBox vbButtons=new VBox();
        vbButtons.getStyleClass().add("vb-buttons");
        vbButtons.getChildren().addAll(btnWA,btnShaping,btnShell);

        BorderPane borderPane=new BorderPane();
        borderPane.setLeft(bpExplorer);
        borderPane.setCenter(tabPane);
        borderPane.setRight(vbButtons);

        Scene scene=new Scene(borderPane,800,450);
        stage.setScene(scene);
        stage.setTitle("tec master");
        stage.show();

        final String DARK=this.getClass().getResource("./styles/dark.css").toExternalForm();
        Timeline timeline=new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                e->{
                    if(autoSave){
                        Tab tab=tabPane.getSelectionModel().getSelectedItem();
                        if(tab!=null){
                            save((TextArea)tab.getContent(),tab.getId());
                        }
                    }
                    switch(theme){
                        case LIGHT:{
                            scene.getStylesheets().clear();
                            break;
                        }
                        case DARK:{
                            scene.getStylesheets().add(DARK);
                            break;
                        }
                        case AUTO:{
                            if(DarkMode.isDarkMode()){
                                scene.getStylesheets().add(DARK);
                            }else{
                                scene.getStylesheets().clear();
                            }
                        }
                    }
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void openSettings(){
        final int WIDTH=320;
        final int HEIGHT=180;

        Stage settings=new Stage();
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);

        ObservableList<String> languagesList=FXCollections.observableArrayList();
        for(Language lang:Language.values()){
            languagesList.add(lang.toStringInThisLanguage());
        }
        ChoiceBox<String> cbLanguage=new ChoiceBox<String>(languagesList);
        cbLanguage.setValue(language.toStringInThisLanguage());
        cbLanguage.setMaxWidth(Double.MAX_VALUE);

        ToggleButton tbSaveMode=new ToggleButton();
        tbSaveMode.setOnAction(
            e->{
                if(tbSaveMode.isSelected()){
                    tbSaveMode.setText("AUTO");
                }else{
                    tbSaveMode.setText("MANUAL");
                }
            }
        );
        tbSaveMode.setSelected(autoSave);
        tbSaveMode.setText(autoSave?"AUTO":"MANUAL");
        tbSaveMode.setMaxWidth(Double.MAX_VALUE);

        ObservableList<String> themeList=FXCollections.observableArrayList();
        for(Theme theme:Theme.values()){
            themeList.add(theme.toString());
        }
        ChoiceBox<String> cbTheme=new ChoiceBox<String>(themeList);
        cbTheme.setValue(theme.toString());
        cbTheme.setMaxWidth(Double.MAX_VALUE);

        VBox vbSettings=new VBox(cbLanguage,tbSaveMode,cbTheme);
        vbSettings.setPrefWidth(WIDTH);

        ScrollPane spSettings=new ScrollPane(vbSettings);

        Button cancel=new Button("cancel");
        cancel.setMaxWidth(Double.MAX_VALUE);
        cancel.setOnAction(
            e->{
                settings.close();
                btnSetting.setDisable(false);
            }
        );
        Button apply=new Button(MultilingualText.APPLY.getTxt(language));
        apply.setMaxWidth(Double.MAX_VALUE);
        apply.setOnAction(
            e->{
                boolean isLanguageChanged=language!=(language=Language.values()[cbLanguage.getSelectionModel().getSelectedIndex()]);
                boolean isSaveModeChanged=autoSave!=(autoSave=tbSaveMode.isSelected());
                boolean isThemeChanged=theme!=(theme=Theme.values()[cbTheme.getSelectionModel().getSelectedIndex()]);
                if(isSaveModeChanged){
                    Config.set(Config.Key.SAVE,autoSave?Save.AUTO:Save.MANUAL);
                }
                if(isThemeChanged){
                    Config.set(Config.Key.THEME,theme);
                }
                if(isLanguageChanged){
                    Config.set(Config.Key.LANGUAGE,language);

                    Alert alert=new Alert(AlertType.CONFIRMATION,"変更を適応するには再起動する必要があります\n今すぐ再起動しますか?",new ButtonType("後で"),ButtonType.YES);
                    ButtonType select=alert.showAndWait().get();
                    if(select==ButtonType.YES){
                        closeAllTabsAndTreeItems();
                        start(owner);
                    }
                }
                settings.close();
                btnSetting.setDisable(false);
            }
        );

        BorderPane bpClose=new BorderPane();
        bpClose.setLeft(cancel);
        bpClose.setRight(apply);

        BorderPane bpSettings=new BorderPane();
        bpSettings.setCenter(spSettings);
        bpSettings.setBottom(bpClose);

        Scene settingsScene=new Scene(bpSettings);

        settings.setScene(settingsScene);
        settings.show();
    }
    
    File currentDir=null;

    private void selectDirectory(){
        if(currentDir!=null){
            closeAllTabsAndTreeItems();
        }
        DirectoryChooser chooser=new DirectoryChooser();
        File newDir=chooser.showDialog(owner);
        if(newDir!=null){
            setDirectory(newDir);
        }
    }
    private void closeAllTabsAndTreeItems(){
        boolean save=autoSave;
        for(Tab tab:tabPane.getTabs()){
            TextArea textArea=(TextArea)tab.getContent();
            String filePath=tab.getId();
            String fileWritten;
            try{
                fileWritten=readFile(filePath);
            }catch(IOException e){
                errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),filePath));
                continue;
            }
            if(!isSame(textArea.getText(),fileWritten)){
                if(!save){
                    Alert alert=new Alert(AlertType.CONFIRMATION,MultilingualText.DO_YOU_WANT_TO_SAVE_CHANGES.getTxt(language),ButtonType.YES,ButtonType.NO);
                    ButtonType select=alert.showAndWait().get();
                    if(select==ButtonType.YES){
                        save=true;
                    }else if(select==ButtonType.NO){
                        break;
                    }
                }
                if(save){
                    save(textArea,filePath);
                }
            }
        }
        tabPane.getTabs().clear();
        tvExplorer.getSelectionModel().clearSelection();
    }

    private String shellRead,shellWrite;
    private void launchShell(){
        String device=getDevice();
        if(device==null){
            btnShell.setDisable(false);
            return;
        }

        Process shellProcess;
        try{
            shellProcess=new ProcessBuilder(python,"screen.py",device).start();
        }catch(IOException e){
            errorAlert(MultilingualText.FAILED_TO_START_THE_SERIAL_COMMUNICATION_PROCESS.getTxt(language));
            return;
        }

        TextArea textArea=new TextArea();
        shellRead="";
        shellWrite="";
        textArea.setEditable(false);
        textArea.getStylesheets().add("./styles/terminal.css");

        TextField textField=new TextField();
        textField.setOnKeyPressed(
            e->{
                if(e.getCode()==KeyCode.ENTER){
                    String txt=textField.getText()+"\n";
                    shellWrite+=txt;
                    textArea.setText(textArea.getText()+txt);
                    textField.setText("");
                }
            }
        );
        textField.getStylesheets().add("./styles/terminal.css");

        Thread write=new Thread(){
            public void run(){
                OutputStreamWriter writer=new OutputStreamWriter(shellProcess.getOutputStream());
                try{
                    while(!interrupted()){
                        if(!shellWrite.isEmpty()){
                            String txt=shellWrite;
                            shellWrite="";
                            writer.write(txt);
                            writer.flush();
                            System.out.print(txt);
                        }
                    }
                }catch(IOException e){
                }
                try{
                    writer.close();
                }catch(IOException e){
                }
                shellProcess.destroy();
            };
        };
        Thread read=new Thread(){
            public void run(){
                InputStreamReader reader=new InputStreamReader(shellProcess.getInputStream());
                try{
                    while(!interrupted()){
                        int i;
                        while((i=reader.read())!=-1){
                            char c=(char)i;
                            shellRead+=c;
                            System.out.print(c);
                        }
                    }
                }catch(IOException e){
                }
                try{
                    reader.close();
                }catch(IOException e){
                }
                shellProcess.destroy();
            };
        };

        write.setDaemon(true);
        read.setDaemon(true);
        write.start();
        read.start();

        BorderPane borderPane=new BorderPane();
        borderPane.setCenter(textArea);
        borderPane.setBottom(textField);
        

        Stage stage=new Stage();
        stage.setTitle("tec screen");
        stage.setScene(new Scene(borderPane));
        AnimationTimer timer=new AnimationTimer(){
            @Override
            public void handle(long now){
                if(!shellProcess.isAlive()){
                    Alert alert=new Alert(AlertType.ERROR,MultilingualText.AN_ERROR_OCCURRED_IN_THE_SERIAL_COMMUNICATION_PROCESS.getTxt(language),ButtonType.CLOSE);
                    alert.show();
                    stage.close();
                    stop();
                    write.interrupt();
                    read.interrupt();
                    btnShell.setDisable(false);
                }

                if(!shellRead.isEmpty()){
                    textArea.setText(textArea.getText()+shellRead);
                    shellRead="";
                }
            }
        };
        timer.start();
        stage.setOnCloseRequest(
            e->{
                timer.stop();
                write.interrupt();
                read.interrupt();
                btnShell.setDisable(false);
            }
        );
        stage.show();
    }
    private void setDirectory(File currentDir){
        this.currentDir=currentDir;
        tvExplorer.setRoot(createTreeItem(currentDir));
        tvExplorer.getSelectionModel().selectedItemProperty().addListener((c,o,n)->openTab(n));
        sortTreeView();
    }

    private ContextMenu createContextMenu(){
        TreeItem<String> item=tvExplorer.getSelectionModel().getSelectedItem();
        String filePath=getPath(item);

        ContextMenu cmOption=new ContextMenu();

        File file=new File(filePath);

        List<MenuItem> menuItems=new ArrayList<>();

        if(item.getParent()==null){
            MenuItem miCreateFile=new MenuItem("create new file");
            miCreateFile.setOnAction(e->createFile(item));
            menuItems.add(miCreateFile);

            MenuItem miCreateFolder=new MenuItem("create new folder");
            miCreateFolder.setOnAction(e->createFolder(item));
            menuItems.add(miCreateFolder);
        }else if(file.isFile()){
            MenuItem miRemove=new MenuItem("remove this file");
            miRemove.setOnAction(e->removeFile(item));
            menuItems.add(miRemove);

            MenuItem miRename=new MenuItem("rename this file");
            miRename.setOnAction(e->renameFile(item));
            menuItems.add(miRename);
        }else if(file.isDirectory()){
            MenuItem miCreateFile=new MenuItem("create new file");
            miCreateFile.setOnAction(e->createFile(item));
            menuItems.add(miCreateFile);

            MenuItem miCreateFolder=new MenuItem("create new folder");
            miCreateFolder.setOnAction(e->createFolder(item));
            menuItems.add(miCreateFolder);

            menuItems.add(new SeparatorMenuItem());

            MenuItem miRemove=new MenuItem("remove this folder");
            miRemove.setOnAction(e->removeFolder(item));
            menuItems.add(miRemove);

            MenuItem miRename=new MenuItem("rename this folder");
            miRename.setOnAction(e->renameFolder(item));
            menuItems.add(miRename);
        }

        cmOption.getItems().addAll(menuItems);

        return cmOption;
    }

    private void removeFile(TreeItem<String> item){
        Alert alert=new Alert(AlertType.CONFIRMATION,MultilingualText.DO_YOU_WANT_TO_REMOVE_THE_FILE.getTxt(language),ButtonType.YES,ButtonType.NO);
        ButtonType select=alert.showAndWait().get();
        if(select==ButtonType.YES){
            File file=new File(getPath(item));
            if(!file.delete()){
                errorAlert(MultilingualText.FAILED_TO_REMOVE_THE_FILE.getTxt(language));
                return;
            }
            item.getParent().getChildren().remove(item);
            List<Tab> tabs=tabPane.getTabs();
            for(Tab tab:tabs){
                if(tab.getId().equals(file.getAbsolutePath())){
                    tabs.remove(tab);
                    break;
                }
            }
        }
    }

    private void renameFile(TreeItem<String> item){
        File file=new File(getPath(item));
        String oldName=file.getName();
        String oldPath=file.getAbsolutePath();
        TextInputDialog tiDialog=new TextInputDialog(oldName);
        Optional<String> opt=tiDialog.showAndWait();
        if(opt.isPresent()){
            String newName=opt.get();
            if(!isSameType(oldName,newName)){
                Alert alert=new Alert(AlertType.WARNING,MultilingualText.DO_YOU_WANT_TO_CHANGE_THE_EXTENSION.getTxt(language),ButtonType.OK,ButtonType.CANCEL);
                ButtonType select=alert.showAndWait().get();
                if(select==ButtonType.CANCEL){
                    return;
                }
            }

            File newFile=new File(file.getParentFile().getAbsolutePath()+"/"+newName);

            if(!file.renameTo(newFile)){
                errorAlert(MultilingualText.FAILED_TO_RENAME_THE_FILE.getTxt(language));
                return;
            }

            String newPath=newFile.getAbsolutePath();

            item.setValue(newName);

            sortTreeItem(item.getParent());

            for(Tab tab:tabPane.getTabs()){
                if(tab.getId().equals(oldPath)){
                    tab.setId(newPath);
                    tab.setText(newName);
                    if(!isRelevant(newName)){
                        onTabClose(tab);
                        item.getParent().getChildren().remove(item);
                    }else{
                        setButtons(newPath,item,(TextArea)tab.getContent());
                    }
                }
            }

            eliminateDuplicateTabs();
        }
    }

    private void renameFolder(TreeItem<String> item){
        File file=new File(getPath(item));
        String oldName=file.getName();
        TextInputDialog tiDialog=new TextInputDialog(oldName);
        Optional<String> opt=tiDialog.showAndWait();
        if(opt.isPresent()){
            String newName=opt.get();

            Map<String,TreeItem<String>> change=getChangeItems(item);

            File newFile=new File(file.getParentFile().getAbsolutePath()+"/"+newName);

            if(!file.renameTo(newFile)){
                errorAlert(MultilingualText.FAILED_TO_RENAME_THE_FOLDER.getTxt(language));
                return;
            }

            item.setValue(newName);

            for(Tab tab:tabPane.getTabs()){
                String id=tab.getId();
                if(change.containsKey(id)){
                    String newId=getPath(change.get(id));
                    tab.setId(newId);
                    if(tab.getText().equals(id)){
                        tab.setText(newId);
                    }
                }
            }

            sortTreeItem(item.getParent());

        }
    }
    private Map<String,TreeItem<String>> getChangeItems(TreeItem<String> item){
        Map<String,TreeItem<String>> changeItems=new HashMap<>();

        for(TreeItem<String> childItem:item.getChildren()){
            changeItems.putAll(getChangeItems(childItem));
        }

        changeItems.put(getPath(item),item);

        return changeItems;
    }

    private void createFile(TreeItem<String> item){
        TextInputDialog tiDialog=new TextInputDialog(MultilingualText.PLEASE_ENTER_THE_FILE_NAME.getTxt(language));
        Optional<String> opt=tiDialog.showAndWait();
        if(opt.isPresent()){
            String newFileName=opt.get();
            File newFile=new File(getPath(item)+"/"+newFileName);
            try{
                if(!newFile.createNewFile()){
                    errorAlert(MultilingualText.THIS_FILE_NAME_ALREADY_EXISTS.getTxt(language));
                }else{
                    if(isRelevant(newFileName)){
                        item.getChildren().add(new TreeItem<>(newFileName));
                    }
                }
            }catch(IOException e){
                errorAlert(MultilingualText.FAILED_TO_CREATE_THE_FILE.getTxt(language));
            }
        }
    }

    private void createFolder(TreeItem<String> item){
        TextInputDialog tiDialog=new TextInputDialog(MultilingualText.PLEASE_ENTER_THE_FOLDER_NAME.getTxt(language));
        Optional<String> opt=tiDialog.showAndWait();
        if(opt.isPresent()){
            String newFolderName=opt.get();
            File newFolder=new File(getPath(item)+"/"+newFolderName);
            if(!newFolder.mkdir()){
                errorAlert(MultilingualText.THIS_FOLDER_NAME_ALREADY_EXISTS.getTxt(language));
            }else{
                ImageView ivFolderMono=new ImageView("./images/folder_mono.png");
                ivFolderMono.setFitWidth(iconSize);
                ivFolderMono.setPreserveRatio(true);

                TreeItem<String> newItem=new TreeItem<>(newFolderName);
                newItem.setGraphic(ivFolderMono);

                item.getChildren().add(newItem);
            }
        }
    }

    private void removeFolder(TreeItem<String> item){
        Alert alert=new Alert(AlertType.CONFIRMATION,MultilingualText.DO_YOU_WANT_TO_REMOVE_THE_FOLDER.getTxt(language),ButtonType.YES,ButtonType.NO);
        ButtonType select=alert.showAndWait().get();
        if(select==ButtonType.YES){
            if(!removeFolder(new File(getPath(item)))){
                errorAlert(MultilingualText.FAILED_TO_REMOVE_THE_FOLDER.getTxt(language));
                return;
            }
            item.getParent().getChildren().remove(item);
        }
    }

    private boolean removeFolder(File directory){
        for(File file:directory.listFiles()){
            if(file.isDirectory()){
                removeFolder(file);
            }else if(file.isFile()){
                if(!file.delete()){
                    return false;
                }
            }
        }
        return directory.delete();
    }

    private void eliminateDuplicateTabs(){
        List<String> names=new ArrayList<>();
        for(Tab tab:tabPane.getTabs()){
            if(names.contains(tab.getText())){
                tab.setText(tab.getId());
            }else{
                names.add(tab.getText());
            }
        }
    }


    private TreeItem<String> createTreeItem(File directory){
        TreeItem<String> item=new TreeItem<>(directory.getName());
        for(File file:directory.listFiles()){
            if(file.isFile()){
                String fileName=file.getName();
                if(isRelevant(fileName)){
                    item.getChildren().add(new TreeItem<>(fileName));
                }
            }else if(file.isDirectory()){
                item.getChildren().add(createTreeItem(file));
            }
        }

        ImageView ivFolderMono=new ImageView("./images/folder_mono.png");
        ivFolderMono.setFitWidth(iconSize);
        ivFolderMono.setPreserveRatio(true);
        item.setGraphic(ivFolderMono);
        
        return item;
    }

    private boolean isRelevant(String fileName){
        return isSourceFile(fileName)||isBinaryFile(fileName)||isListFile(fileName);
    }

    private boolean isSameType(String fileName1,String fileName2){
        boolean hasPeriod1=fileName1.contains(".");
        boolean hasPeriod2=fileName2.contains(".");
        if(hasPeriod1^hasPeriod2){
            return false;
        }else if((!hasPeriod1)&(!hasPeriod2)){
            return true;
        }
        String extension1=fileName1.substring(fileName1.lastIndexOf('.'));
        String extension2=fileName2.substring(fileName2.lastIndexOf('.'));
        return extension1.equals(extension2);
    }

    private boolean isSourceFile(String fileName){
        return fileName.endsWith(".t7");
    }

    private boolean isBinaryFile(String fileName){
        return fileName.endsWith(".bin");
    }

    private boolean isListFile(String fileName){
        return fileName.endsWith(".lst");
    }

    private void openTab(TreeItem<String> item){
        if(item==null){
            return;
        }
        String fileName=item.getValue();
        if(item.getChildren().size()==0&&isRelevant(fileName)){
            ObservableList<Tab> tabs=tabPane.getTabs();
            boolean duplicate=false;
            String filePath=getPath(item);
            for(Tab tab:tabs){
                if(tab.getText().equals(fileName)){
                    if(tab.getId().equals(filePath)){
                        tabPane.getSelectionModel().select(tab);
                        return;
                    }else{
                        duplicate=true;
                    }
                }
            }
            TextArea textArea=new TextArea();
            textArea.setFont(Font.font("MONOSPACED"));

            Tab tab=new Tab();
            tab.setId(filePath);
            tab.setText(duplicate?filePath:fileName);
            tab.setContent(textArea);
            tab.setOnSelectionChanged(
                e->{
                    String thisTimePath=tab.getId();

                    if(tab.isSelected()){
                        tvExplorer.getSelectionModel().select(item);
                        setButtons(thisTimePath,item,textArea);
                    }else{
                        btnWA.setGraphic(ivDummy);
                        btnWA.setDisable(true);

                        btnShaping.setGraphic(ivDummy);
                        btnShaping.setDisable(true);
                    }
                }
            );
            tab.setOnCloseRequest(e->onTabClose(tab));

            textArea.setOnKeyPressed(e->{
                if(e.getCode()==KeyCode.S&&(OSInfo.isMacOS()?e.isMetaDown():e.isControlDown())){
                    save(textArea,tab.getId());
                }
            });

            updateTab(tab);

            tabs.add(tab);
            tabPane.getSelectionModel().select(tab);
        }
    }
    private void setButtons(String filePath,TreeItem<String> item,TextArea textArea){
        if(isBinaryFile(filePath)){
            btnWA.setGraphic(ivWrite);
            btnWA.setDisable(false);
            btnWA.setOnAction(e->write(filePath,item));
        }else if(isSourceFile(filePath)){
            btnWA.setGraphic(ivAssemble);
            btnWA.setDisable(false);
            btnWA.setOnAction(e->assemble(filePath,item));

            btnShaping.setGraphic(ivShaping);
            btnShaping.setDisable(false);
            btnShaping.setOnAction(e->shaping(textArea));
        }else if(isListFile(filePath)){
            btnWA.setGraphic(ivDummy);
            btnWA.setDisable(true);
        }
    }
    private void onTabClose(Tab tab){
        String path=tab.getId();
        TextArea textArea=(TextArea)tab.getContent();
        String fileWritten;
        try{
            fileWritten=readFile(path);
        }catch(IOException e){
            errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),path));
            return;
        }
        if(!isSame(textArea.getText(),fileWritten)){
            boolean save;
            if(autoSave){
                save=true;
            }else{
                Alert alert=new Alert(AlertType.CONFIRMATION,MultilingualText.DO_YOU_WANT_TO_SAVE_CHANGES.getTxt(language),ButtonType.YES,ButtonType.NO);
                ButtonType select=alert.showAndWait().get();
                save=select==ButtonType.YES;
            }
            if(save){
                save(textArea,path);
            }
        }
    }
    private boolean isSame(String str1,String str2){
        if(str1.equals(str2)){
            return true;
        }else{
            int minLen=Math.min(str1.length(),str2.length());
            for(int i=0;i<minLen;i++){
                if(str1.charAt(i)!=str2.charAt(i)){
                    return false;
                }
            }
            for(int i=minLen;i<str1.length();i++){
                if(str1.charAt(i)!='\n'){
                    return false;
                }
            }
            for(int i=minLen;i<str2.length();i++){
                if(str2.charAt(i)!='\n'){
                    return false;
                }
            }
            return true;
        }
    }
    private void save(TextArea textArea,String path){
        String writtenTxt=textArea.getText();
        try{
            if(!isBinaryFile(path)){
                writeFile(path,writtenTxt);
            }
        }catch(IOException e){
            errorAlert(String.format(MultilingualText.FAILED_TO_WRITE_TO_FILE_.getTxt(language),path));
        }
    }
    private void save(TreeItem<String> item){
        String path=getPath(item);
        for(Tab tab:tabPane.getTabs()){
            if(tab.getId().equals(path)){
                TextArea textArea=(TextArea)tab.getContent();
                save(textArea,path);
            }
        }
    }

    private String getDevice(){
        Process getPorts;
        try{
            getPorts=new ProcessBuilder(python,"ports_getter.py").start();
        }catch(IOException e){
            errorAlert(MultilingualText.FAILED_TO_START_THE_USB_PORT_ACQUISITION_PROCESS.getTxt(language));
            return null;
        }
        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(getPorts.getInputStream()));
            final int LENGTH=Integer.parseInt(reader.readLine());
            if(LENGTH==0){
                errorAlert(MultilingualText.THE_TEC_IS_NOT_CONNECTED.getTxt(language));
                return null;
            }
            String[] devices=new String[LENGTH];
            for(int i=0;i<LENGTH;i++){
                devices[i]=reader.readLine();
            }
            reader.close();
            if(LENGTH==1){
                return devices[0];
            }
            ChoiceDialog<String> dialog=new ChoiceDialog<>(devices[0],devices);
            dialog.setTitle(MultilingualText.SELECT_THE_TEC_TO_OPERATE.getTxt(language));
            return dialog.showAndWait().get();
        }catch(IOException e){
            errorAlert(MultilingualText.AN_ERROR_OCCURRED_IN_THE_USB_PORT_ACQUISITION_PROCESS.getTxt(language));
            return null;
        }
    }

    private void write(String filePath,TreeItem<String> item){
        String device=getDevice();
        if(device==null){
            return;
        }

        List<String> data=new ArrayList<>();
        
        try(BufferedInputStream in=new BufferedInputStream(new FileInputStream(filePath))){
            int tmp;
            while((tmp=in.read())!=-1){
                byte b=(byte)tmp;
                String byteStr=to2DigitHex(b);
                System.out.printf("[%s]",byteStr);
                data.add(byteStr);
            }
            System.out.println();
        }catch(FileNotFoundException e){
            errorAlert(MultilingualText.THE_FILE_WAS_NOT_FOUND.getTxt(language));
            return;
        }catch(IOException e){
            errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),filePath));
            return;
        }

        try{
            data.add(0,python);
            data.add(1,"write.py");
            data.add(2,device);

            Process write=new ProcessBuilder(data).start();

            int exitCode=write.waitFor();
            if(exitCode==-1){
                errorAlert(MultilingualText.FAILED_TO_CONNECT_TO_THE_TEC.getTxt(language));
            }else if(exitCode!=0){
                errorAlert(MultilingualText.FAILED_TO_WRITE.getTxt(language));
            }
        }catch(Exception e){
            errorAlert(MultilingualText.FAILED_TO_START_THE_WRITE_PROCESS.getTxt(language));
        }
    }

    private void assemble(String filePath,TreeItem<String> item){
        save(item);

        Object[] assembled;
        try{
           assembled=Assembler.assenmble(readFile(filePath));
        }catch(IOException e){
            errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),filePath));
            return;
        }catch(Assembler.AssembleException e){
            errorAlert(String.format(MultilingualText.FAILED_TO_ASSEMBLE__.getTxt(language),e.line,e.lineStr,String.format(e.message.getTxt(language),e.messageStr)));
            return;
        }
        byte[] binaryData=(byte[])assembled[0];
        String listData=(String)assembled[1];

        File binaryFile=new File(filePath.substring(0,filePath.length()-2)+"bin");
        try{
            if(!binaryFile.exists()){
                binaryFile.createNewFile();
                item.getParent().getChildren().add(new TreeItem<>(binaryFile.getName()));
            }
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(binaryFile));
            out.write(binaryData);
            out.flush();
            out.close();
        }catch(IOException e){
            errorAlert(MultilingualText.FAILED_TO_CREATE_BINARY_FILE.getTxt(language));
            return;
        }

        for(Tab tab:tabPane.getTabs()){
            if(new File(tab.getId()).equals(binaryFile)){
                updateTab(tab);
            }
        }

        File listFile=new File(filePath.substring(0,filePath.length()-2)+"lst");
        try{
            if(!listFile.exists()){
                listFile.createNewFile();
                item.getParent().getChildren().add(new TreeItem<>(listFile.getName()));
            }
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(listFile),"UTF-8"));
            writer.write(listData);
            writer.flush();
            writer.close();

        }catch(IOException e){
            errorAlert(MultilingualText.FAILED_TO_CREATE_LIST_FILE.getTxt(language));
            return;
        }
        
        for(Tab tab:tabPane.getTabs()){
            if(new File(tab.getId()).equals(listFile)){
                updateTab(tab);
            }
        }

        sortTreeView();

        Alert alert=new Alert(
            AlertType.INFORMATION,
            String.format(MultilingualText.RESULTS_WARE_STORED_IN__AND_.getTxt(language),binaryFile.getName(),listFile.getName()),
            ButtonType.CLOSE
        );
        alert.setTitle(MultilingualText.SUCCESSFULLY_ASSEMBLED.getTxt(language));
        alert.showAndWait();
    }

    private void shaping(TextArea textArea){
        String[] lines=textArea.getText().split("\n");
        String newText="";
        for(int i=0;i<lines.length;i++){
            String[] split=lines[i].split("[\s]+");
            for(int j=0;j<split.length;j++){
                if(split[j].startsWith(";")){
                    while(j<split.length){
                        newText+=split[j++];
                    }
                    break;
                }
                if(j==2){
                    newText+=String.format("%-12s",split[j]);
                }else{
                    newText+=String.format("%-8s",split[j]);
                }
            }
            newText+="\n";
        }
        textArea.setText(newText);
    }

    private String getPath(TreeItem<String> item){
        String path=item.getValue();
        while((item=item.getParent())!=null){
            path=item.getValue()+"/"+path;
        }
        return new File(currentDir.getParentFile().getAbsolutePath()+"/"+path).getAbsolutePath();
    }

    private void updateTab(Tab tab){
        TextArea textArea=(TextArea)tab.getContent();
        String filePath=tab.getId();
        if(isBinaryFile(filePath)){
            textArea.setEditable(false);
            try{
                textArea.setText(readBinaryFile(filePath));
            }catch(IOException e){
                errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),filePath));
            }
        }else{
            try{
                textArea.setText(readFile(filePath));
            }catch(IOException e){
                errorAlert(String.format(MultilingualText.FAILED_TO_READ_FROM_FILE_.getTxt(language),filePath));
            }
        }
    }

    private void sortTreeView(){
        TreeItem<String> root=tvExplorer.getRoot();
        sortTreeItems(root);
    }

    private void sortTreeItems(TreeItem<String> parent){
        sortTreeItem(parent);
        for(TreeItem<String> child:parent.getChildren()){
            sortTreeItems(child);
        }
    }

    private void sortTreeItem(TreeItem<String> parent){
        Collections.sort(parent.getChildren(),new Comparator<TreeItem<String>>(){
            @Override
            public int compare(TreeItem<String> o1, TreeItem<String> o2){
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    private void errorAlert(String text){
        Alert alert=new Alert(AlertType.ERROR,text,ButtonType.CLOSE);
        alert.showAndWait();
    }

    private String readFile(String filePath)throws IOException{
        BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
        String line;
        String text="";
        while((line=reader.readLine())!=null){
            text+=line+"\n";
        }
        reader.close();
        return text;
    }
    private String readBinaryFile(String filePath)throws IOException{
        BufferedInputStream in=new BufferedInputStream(new FileInputStream(filePath));
        String str="";
        int tmp;
        while((tmp=in.read())!=-1){
            byte b=(byte)tmp;
            str+=String.format("[%s]",to2DigitHex(b));
        }
        in.close();
        return str;
    }

    private void writeFile(String filePath,String text)throws IOException{
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
        writer.write(text);
        writer.flush();
        writer.close();
    }

    final String[] HEX={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    private String to2DigitHex(byte b){
        return HEX[Byte.toUnsignedInt(b)>>>4]+HEX[Byte.toUnsignedInt(b)&0b0000_1111];
    }

}