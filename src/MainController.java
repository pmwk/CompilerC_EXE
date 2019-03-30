package src;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    Pane root;
    @FXML
    BorderPane root_bp;
    @FXML
    VBox output_root;
    @FXML
    ScrollPane error_root;
    @FXML
    ScrollPane console_root;

    @FXML
    VBox consoleMessages_root;
    @FXML
    VBox errorMessages_root;

    @FXML
    Label rootFolder_lab;
    @FXML
    TreeView treeFiles_tv;
    @FXML
    CheckBox launch_cb;
    @FXML
    Button refreshFiles_but;

    private final double HEIGHT_BUT= 30;
    private final double ERROR_PART= 0.5; //какую часть от output_root занимает error_root

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootFolderProperty.addListener((ov, old_value, new_value) -> {
            rootFolder_lab.setText(new_value);

            File file = new File(new_value);
            if (file.exists()) {
                refreshTreeFiles();
            } else {
                //сообщить
            }
        }); //при изменение папки с исходниками

        String rootFolder_lastName = Configuration.getLastFolder();
        if (rootFolder_lastName == null) {
            rootFolder_lastName = "No value";
        }
        rootFolderProperty.setValue(rootFolder_lastName);

        createErrorLabels();

        root_bp.prefHeightProperty().bind(root.heightProperty());
        root_bp.prefWidthProperty().bind(root.widthProperty());

        refreshFiles_but.setPrefHeight(HEIGHT_BUT);
        error_root.prefHeightProperty().bind(output_root.heightProperty().add(-HEIGHT_BUT).multiply(ERROR_PART));
        console_root.prefHeightProperty().bind(output_root.heightProperty().add(-HEIGHT_BUT).multiply(1 - ERROR_PART)); //настроили размеры и соотношение между собой error и console _root
        consoleMessages_root.prefWidthProperty().bind(error_root.widthProperty());

        treeFiles_tv.getSelectionModel().selectedItemProperty().addListener((ov, new_value, old_value) -> {
            checkedError();
        });
    }

    private boolean checkedError() {
        boolean b = true;

        if (!checkFileExtension()) {
            fileFormatInvalid_lab.setOpacity(0.1);
        } else {
            fileFormatInvalid_lab.setOpacity(1);
            b = false;
        }

        String outputFileName = getOutputFileName();

        if (checkFileExistInCatalog(getPathThisCatalog(), outputFileName)) {
            newFileIsExist_thisFolder_lab.setOpacity(0.1);
            b = false;
        } else {
            newFileIsExist_thisFolder_lab.setOpacity(1);
        }

        if (checkFileExistInCatalog(getPathCopyCatalog(), outputFileName)) {
            newFileIsExist_copyFolder_lab.setOpacity(0.1);
            b = false;
        } else {
            newFileIsExist_copyFolder_lab.setOpacity(1);
        }//+добавить проверку на требуемость этой проверки, null



        return b;
    }

    private String getPathThisCatalog() {
        return rootFolderProperty.toString();
    }

    private String getPathCopyCatalog() {
        return getPathThisCatalog();
    }

    private boolean checkFileExistInCatalog(String catalogName, String fileName) {
        boolean b = false;

        File catalog = new File(catalogName);
        if (catalog.exists() && catalog.isDirectory()) {
            for (File file : catalog.listFiles()) {
                if (file.getName().equals(fileName)) {
                    b = true;
                }
            }
        }

        return b;
    }

    private boolean checkFileExtension() {
        String fileName = getItem_str();
        return fileName.substring(fileName.length() - 2, fileName.length()).equals(".c");
    }

    private String getOutputFileName() {
        if (checkFileExtension()) {
            String fileName = getItem_str();
            String outputFileName = fileName.substring(fileName.length()-2, fileName.length()) + ".exe";
            return outputFileName;
        } else {
            return null;
        }
    }

    public void refreshTreeFiles () {
        File rootFile = new File(rootFolderProperty.get());
        if (rootFile.exists()) {
            TreeItem<String> root = getNodeForTreeFiles(rootFile);
            treeFiles_tv.setRoot(root);
            root.setExpanded(true);
        }
    }

    private TreeItem<String> getNodeForTreeFiles (File file) {
        TreeItem<String> item = new TreeItem<>(file.getName());

        if (file.isDirectory()) {
            for (File children : file.listFiles()) {
                item.getChildren().add(getNodeForTreeFiles(children));
            }
        }

        return item;
    }

    private StringProperty rootFolderProperty = new SimpleStringProperty("");
    public void clickOnRootFolder() {
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(new Stage());
        if (file != null) {
            String rootFolder_newValue = file.getAbsolutePath();
            rootFolderProperty.setValue(rootFolder_newValue);
            Configuration.setLastFolder(rootFolder_newValue);
        }

        //update tree
    }

    private String getItem_str() {
        return ((TreeItem)treeFiles_tv.getSelectionModel().getSelectedItem()).getValue().toString();
    }

    public void clickOnStart() {
        String path = rootFolderProperty.getValue() + "/";
        //path = path.replaceAll(" ", "\\\\\\\\ ");
        String file_name = getItem_str();
        String file_name_sh = file_name.substring(0, file_name.length()-2);


        Console console = new Console();
        console.exec("i686-w64-mingw32-gcc " + path + file_name +"  -o " + path + file_name_sh + ".exe");
        processIsCompleted(console.getLastExecCommand());
        refreshTreeFiles();

        if (launch_cb.isSelected()) {
            console.exec("wine " + path + file_name_sh + ".exe");
            processIsCompleted(console.getLastExecCommand());
        }

        console.close();

        //console.exec("i686-w64-mingw32-gcc FirstBlood.c -o FirstBlood.exe");
    }

    private void processIsCompleted(ExecCommand execCommand) {
        Label answer_lab = new Label(execCommand.getAnswer());
        answer_lab.setWrapText(true);
        answer_lab.setTextFill(Color.GREEN);

        Label error_lab = new Label(execCommand.getError());
        error_lab.setWrapText(true);
        error_lab.setTextFill(Color.RED);

        consoleMessages_root.getChildren().addAll(answer_lab, error_lab, new Label());

        answer_lab.prefWidthProperty().bind(consoleMessages_root.prefWidthProperty());
        error_lab.prefWidthProperty().bind(consoleMessages_root.prefWidthProperty());
    }

    public void clickOnRefreshFiles() {
        refreshTreeFiles();
    }

    private Label noSelectedFile_lab = new Label("No file chosen");
    private Label fileFormatInvalid_lab = new Label("The file format is invalid");
    private Label newFileIsExist_thisFolder_lab = new Label("There is already such a file name in this folder");
    private Label newFileIsExist_copyFolder_lab = new Label("There is already such a file name in copy folder");

    private void createErrorLabels() {
        noSelectedFile_lab.setTextFill(Color.RED);
        fileFormatInvalid_lab.setTextFill(Color.RED);
        newFileIsExist_thisFolder_lab.setTextFill(Color.RED);
        newFileIsExist_copyFolder_lab.setTextFill(Color.RED);

        errorMessages_root.getChildren().addAll(noSelectedFile_lab,
                fileFormatInvalid_lab,
                newFileIsExist_thisFolder_lab,
                newFileIsExist_copyFolder_lab);
    }


}
