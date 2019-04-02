package src;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
    ScrollPane treeFiles_root_sp;

    @FXML
    VBox consoleMessages_root;

    @FXML
    VBox settings_original_root;
    @FXML
    VBox settings_copied_root;
    @FXML
    VBox settings_counterCopied_root;

    @FXML
    CheckBox settings_notSaveInThisCatalog_cb;
    @FXML
    CheckBox settings_createCopy_cb;
    @FXML
    CheckBox settings_counterOriginal_cb;
    @FXML
    CheckBox settings_counterCopied_cb;
    @FXML
    TextField settings_nameOriginal_tf;
    @FXML
    Label settings_counterOriginal_lab;
    @FXML
    Label settings_catalogCopiedPath_lab;
    @FXML
    CheckBox settings_launch_cb;
    @FXML
    CheckBox settings_launchCopy_cb;
    @FXML
    ComboBox<String> settings_counterOriginalType_cb;
    @FXML
    TextField settings_counterOriginalLastValue_tf;
    @FXML
    TextField settings_nameCopied_tf;
    @FXML
    TextField settings_counterCopiedLastValue_tf;
    @FXML
    ComboBox<String> settings_counterCopiedType_cb;

    @FXML
    Label catalogPath_lab;
    @FXML
    TreeView<String> treeFiles_tv;
    @FXML
    Button refreshFiles_but;

    private final double HEIGHT_BUT= 30; //высота кнопки "RefreshFiles"
    private final double ERROR_PART= 0.5; //какую часть от output_root занимает error_root

    private ErrorPane errorPane; //эта панель овтечает за отображение различных ошибок

    /*в программе существют различные ???Property ответственные за перемнные, выбранные на текущий момент пользователем*/
    private StringProperty catalogProperty = new SimpleStringProperty(); //исходный каталог
    private StringProperty catalogCopiedProperty = new SimpleStringProperty(); //каталог для копирвоания

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshFiles_but.setPrefHeight(HEIGHT_BUT); //явно определяем размеры кнопки, для  корректных взаимных измнений размеров панелей
        errorPane = new ErrorPane();
        error_root.setContent(errorPane);

        binding();
        listining();

        init(); //загружаем инфорамцию из конфигурационного файла (!важно, что после binding() и listining(), так как различные label не проставяться)


        treeFiles_tv.getSelectionModel().selectedItemProperty().addListener((ov, new_value, old_value) -> {
            //checkedError();
        });
    }

    private void binding() {
        root_bp.prefHeightProperty().bind(root.heightProperty());
        root_bp.prefWidthProperty().bind(root.widthProperty()); //связывает самую галвную панель (root) с её единственным дочерним элементов (bp_root)

        catalogPath_lab.textProperty().bind(catalogProperty); //Label с путём до каталога связывается с перемнной,хрнаящей путь до каталога
        settings_catalogCopiedPath_lab.textProperty().bind(catalogCopiedProperty); //Label с путём до каталога, в который копируется, связывается со свойством, хранящий этот путь

        error_root.prefHeightProperty().bind(output_root.heightProperty().add(-HEIGHT_BUT).multiply(ERROR_PART));
        console_root.prefHeightProperty().bind(output_root.heightProperty().add(-HEIGHT_BUT).multiply(1 - ERROR_PART)); //настроили размеры и соотношение между собой error и console _root
        consoleMessages_root.prefWidthProperty().bind(error_root.widthProperty());
        errorPane.prefWidthProperty().bind(error_root.widthProperty());

        treeFiles_root_sp.prefWidthProperty().bind(root.widthProperty().divide(2));
        output_root.prefWidthProperty().bind(root.widthProperty().divide(2).subtract(10)); //разделяем основную область экрана между областью для дерева и областью вывода

        treeFiles_tv.prefWidthProperty().bind(treeFiles_root_sp.widthProperty().subtract(10));
    } //этот метод связывает все необходимые величины

    private void listining() {
        catalogProperty.addListener((ov, old_value, new_value) -> {
            File catalog = new File(new_value);
            if (!catalog.exists()) {
                errorPane.addNotification("This catalog is mythical");
            } else if (!catalog.isDirectory()) {
                errorPane.addNotification("This catalog is catalog");
            } else {
                refreshTreeFiles();
            }
        }); //действие при выборе путя до каталога

        settings_notSaveInThisCatalog_cb.selectedProperty().addListener((ov, old_value, new_value) -> {
            settings_original_root.setDisable(new_value.booleanValue());
        }); //если пользователь выбирает "Не сохранять файл в текущей директории, то панель по настройке имени становится не действительной"

        settings_createCopy_cb.selectedProperty().addListener((ov, old_value, new_value) -> {
            settings_copied_root.setDisable(!new_value.booleanValue());
        }); //если пользователь выбирает "Сохранять копию файла, то панель но настройки для копии становиться активной"

        settings_counterCopied_cb.selectedProperty().addListener((ov, old_value, new_value) -> {
            settings_counterCopied_root.setDisable(!new_value.booleanValue());
        }); //если пользователь выбирает использование счётчика, то становится доступной настройка счётчика

    } //этот метод выставляет все необходимы "слушатели" на различные property

    private void init() {
        String catalog_lastPath = Configuration.getLastFolder();
        if (catalog_lastPath == null) {
            catalog_lastPath = "No value";
        }
        catalogProperty.setValue(catalog_lastPath); //загружаем последний исползованный путь

        String[] typeCounter = src.typeCounter.getValues();
        settings_counterOriginalType_cb.setItems(FXCollections.observableArrayList(typeCounter));
        settings_counterOriginalType_cb.getSelectionModel().select(0);
        settings_counterCopiedType_cb.setItems(FXCollections.observableArrayList(typeCounter));
        settings_counterCopiedType_cb.getSelectionModel().select(0);

    } //этот метод инициализирует все требуемые поля значениями из конфигурационного файла


    private String getItem_str() {
        TreeItem<String> item = treeFiles_tv.getSelectionModel().getSelectedItem();
        if (item == null) {
            return null;
        } else {
            String item_str = item.getValue();
            return item_str;
        }
    }

    private String getNameOriginal() {
        return settings_nameOriginal_tf.getText();
    }

    private String getNameOriginal_result() {
        return null;
    }

    private boolean isSaveOriginal() {
        return !settings_notSaveInThisCatalog_cb.isSelected();
    }

    private boolean isCounterOriginal() {
        return settings_counterOriginal_cb.isSelected();
    }

    private String getCounterOriginalType() {
        return settings_counterOriginalType_cb.getSelectionModel().getSelectedItem();
    }

    private String getCounterOriginalLastValue() {
        return settings_counterOriginalLastValue_tf.getText();
    }

    private boolean isSaveCopied () {
        return settings_createCopy_cb.isSelected();
    }

    private String getCatalog() {
        return catalogProperty.getValue();
    }

    private String getCatalogCopied() {
        return catalogCopiedProperty.getValue();
    }

    private String getNameCopied() {
        return settings_nameCopied_tf.getText();
    }

    private String getNameCopied_result() {
        return null;
    }

    private boolean isCounterCopied() {
        return settings_counterCopied_cb.isSelected();
    }

    private String getCounterCopiedLastValue() {
        return settings_counterCopiedLastValue_tf.getText();
    }

    private String getCounterCopiedType() {
        return settings_counterCopiedType_cb.getSelectionModel().getSelectedItem();
    }

    private boolean isLaunch() {
        return settings_launch_cb.isSelected();
    }

    private boolean isLaunchCopied() {
        return settings_launchCopy_cb.isSelected();
    }

    private Settings collectSettings() {
        Settings.settingsBuilder builder =  Settings.builder();

        String catalog = getCatalog();
        String lastFocusFile = getItem_str();
        String nameOriginal = getNameOriginal();

        boolean isCounterOriginal = isCounterOriginal();
        String counterOriginal_type = getCounterOriginalType();
        String counterOriginalLastValue = getCounterOriginalLastValue();
        boolean isSaveOriginal = isSaveOriginal();

        boolean isSaveCopied = isSaveCopied();
        String catalogCopied = getCatalogCopied();
        String nameCopied = getNameCopied();
        boolean isCounterCopied = isCounterCopied();
        String counterCopied_type = getCounterCopiedType();
        String counterCopiedLastValue = getCounterCopiedLastValue();

        boolean isLaunch = isLaunch();
        boolean isLaunchCopied = isLaunchCopied();

        builder.setCatalogPath(catalog)
                .setLastFocusFile(lastFocusFile)
                .setNameOriginal(nameOriginal)
                .setOriginalCounter(isCounterOriginal, counterOriginal_type, counterOriginalLastValue)
                .setSaveOriginal(isSaveOriginal)
                .setSaveCopied(isSaveCopied)
                .setCatalogCopiedPath(catalogCopied)
                .setNameCopied(nameCopied)
                .setCopiedCounter(isCounterCopied, counterCopied_type, counterCopiedLastValue)
                .setLaunch(isLaunch)
                .setLaunchCopied(isLaunchCopied);

        Settings settings = builder.build();
        return settings;
    }

    private String getPathThisCatalog() {
        return catalogProperty.toString();
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
        File rootFile = new File(catalogProperty.get());
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
    public void clickOnRootFolder() {
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(new Stage());
        if (file != null) {
            String rootFolder_newValue = file.getAbsolutePath();
            catalogProperty.setValue(rootFolder_newValue);
            Configuration.setLastFolder(rootFolder_newValue);
        }

        //update tree
    }

    public void clickOnCompile() {
        System.out.println("compile");
        boolean b = isError();
        if (!b) {
            Settings settings = collectSettings();
            Console console = new Console();
            String pathSource = settings.getPathSource();

            if (settings.isSaveOriginal()) {
                String path_forOriginalExe = settings.getPathForOriginalExe();
                console.exec_createExe(pathSource, path_forOriginalExe);
                processIsCompleted(console.getLastExecCommand());
            }

            if (settings.isSaveCopied()) {
                String path_forCopiedExe = settings.getPathForCopiedExe();
                console.exec_createExe(pathSource, path_forCopiedExe);
                processIsCompleted(console.getLastExecCommand());
            }

            if (settings.isLaunch()) {
                String path_forOriginalExe = settings.getPathForOriginalExe();
                console.exec_launch(path_forOriginalExe);
                processIsCompleted(console.getLastExecCommand());
            }

            if (settings.isLaunchCopied()) {
                String path_forCopiedExe = settings.getPathForCopiedExe();
                console.exec_launch(path_forCopiedExe);
                processIsCompleted(console.getLastExecCommand());
            }

            console.close();
        }
//        String path = catalogProperty.getValue() + "/";
//        //path = path.replaceAll(" ", "\\\\\\\\ ");
//        String file_name = getItem_str();
//        String file_name_sh = file_name.substring(0, file_name.length()-2);
//
//
//        Console console = new Console();
//        console.exec("i686-w64-mingw32-gcc " + path + file_name +"  -o " + path + file_name_sh + ".exe");
//        processIsCompleted(console.getLastExecCommand());
//        refreshTreeFiles();
//
//        if (launch_cb.isSelected()) {
//            console.exec("wine " + path + file_name_sh + ".exe");
//            processIsCompleted(console.getLastExecCommand());
//        }
//
//        console.close();

        //console.exec("i686-w64-mingw32-gcc FirstBlood.c -o FirstBlood.exe");
    }

    private ArrayList<String> getErrors() {
        Settings settings = collectSettings();
        return settings.getErrors();
    }

    private void displayError(ArrayList<String> errors) {
        for (String error : errors) {
            errorPane.addNotification(error);
        }
    }

    private void findDisplayError() {
        displayError(getErrors());
    }

    private boolean isError() {
        ArrayList<String> errors = getErrors();
        if (errors.size() == 0) {
            return false;
        } else {
            displayError(errors);
            return true;
        }
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

    public void clickOnFindErrors() {
        System.out.println("findErrors");
    }

    public void clickOnClearWarnings() {
        System.out.println("clearWarnings");
    }

    public void clickOnClearConsole() {
        System.out.println("clearConsole");
    }

    public void clickOnAutogenerateName_original() {
        System.out.println("clickOnAutogenerateName_original");
    }

    public void clickOnOpenPathCopiedCatalog() {
        System.out.println("clickOnOpenPathCopiedCatalog");
    }

    public void clickOnAutogenerateName_copied() {
        System.out.println("clickOnAutogenerateName_copied");
    }

    public void clickOnLikeTheOriginal_copied() {
        System.out.println("clickOnLikeTheOriginal_copied");
    }

}
