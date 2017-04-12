package demos.gui.uicomponents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/ui/Dialog.fxml", title = "Material Design Example")
public class DialogController {

    public static final String CONTENT_PANE = "ContentPane";
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXButton centerButton;
    @FXML
    private JFXButton topButton;
    @FXML
    private JFXButton rightButton;
    @FXML
    private JFXButton bottomButton;
    @FXML
    private JFXButton leftButton;
    @FXML
    private JFXButton acceptButton;
    @FXML
    private StackPane root;
    @FXML
    private JFXDialog dialog;

    /**
     * init fxml when loaded.
     */
    @PostConstruct
    public void init() {
        root.getChildren().remove(dialog);

        centerButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(DialogTransition.CENTER);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        topButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(DialogTransition.TOP);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        rightButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(DialogTransition.RIGHT);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        bottomButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(DialogTransition.BOTTOM);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        leftButton.setOnMouseClicked((e) -> {
            dialog.setTransitionType(DialogTransition.LEFT);
            dialog.show((StackPane) context.getRegisteredObject(CONTENT_PANE));
        });

        acceptButton.setOnMouseClicked((e) -> dialog.close());
    }

}
