package src;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class ErrorPane extends VBox {

    private final double ERROR_OPACITY = 0.1;
    private final double NOERROR_OPACITY = 0.1;

    public ErrorPane() {
        super();

    }

    private void setOpacityLabel(Label lab, boolean b) {
        if (b) {
            lab.setOpacity(NOERROR_OPACITY);
        } else {
            lab.setOpacity(ERROR_OPACITY);
        }
    }


    public void addNotification(String message) {
        Notification notification = new Notification(message);

        getChildren().addAll(notification);

        notification.setOnDeleteClick(event -> {
            if (getChildren().contains(notification)) {
                getChildren().remove(notification);
            }
        });

        notification.prefWidthProperty().bind(widthProperty());
    }


    private class Notification extends Pane {

        private final double SIZE = 30;

        private Pane iv_root;

        public Notification(String text) {
            BorderPane root = new BorderPane();
            root.setPrefHeight(30);

            Label label = new Label(text);
            label.setTextFill(Color.RED);
            label.setWrapText(true);
            root.setLeft(label);

            iv_root = new Pane();
            Image deleteActive_image = new Image(getClass().getResourceAsStream("/Resource/Images/delete1_active.png"));
            Image deletePassive_image = new Image(getClass().getResourceAsStream("/Resource/Images/delete1_passive.png"));
            ImageView iv = new ImageView(deletePassive_image);
            iv.setFitWidth(SIZE);
            iv.setFitHeight(SIZE);
            iv_root.setPrefHeight(SIZE);
            iv_root.getChildren().addAll(iv);
            root.setRight(iv_root);

            getChildren().addAll(root);

            iv_root.setOnMouseEntered(event-> {
                iv.setImage(deleteActive_image);
            });

            iv_root.setOnMouseExited(event -> {
                iv.setImage(deletePassive_image);
            });

            root.prefWidthProperty().bind(widthProperty().subtract(20)); //20 - учитываем ширину вертикальной полски прокрутки
            label.prefWidthProperty().bind(root.widthProperty().subtract(SIZE));


        }

        public void setOnDeleteClick(EventHandler<? super MouseEvent> event) {
            iv_root.setOnMouseClicked(event);
        }
    }

    public void clear() {
        getChildren().clear();
    }

}
