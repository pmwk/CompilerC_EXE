package src;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
    Label rootFolder_lab;
    @FXML
    TreeView treeFiles_tv;
    @FXML
    CheckBox launch_cb;

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
        });

        String rootFolder_lastName = Configuration.getLastFolder();
        if (rootFolder_lastName == null) {
            rootFolder_lastName = "No value";
        }
        rootFolderProperty.setValue(rootFolder_lastName);


        root_bp.prefHeightProperty().bind(root.heightProperty());
        root_bp.prefWidthProperty().bind(root.widthProperty());


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
        refreshTreeFiles();

        if (launch_cb.isSelected()) {
            console.exec("wine " + path + file_name_sh + ".exe");
        }

        //console.exec("i686-w64-mingw32-gcc FirstBlood.c -o FirstBlood.exe");
    }

    public void clickOnRefreshFiles() {
        refreshTreeFiles();
    }
}
