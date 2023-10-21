package sa.osama_alharbi.sbar.ws.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sa.osama_alharbi.sbar.ws.service.WebsocketService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class Send extends InitView implements Initializable {
    public static boolean HAS_BEAN = true;
    public static String NAME = "Send";

    private final WebsocketService websocketService;

    @FXML
    public AnchorPane root;

    @FXML
    public TextField txtTopic;
    @FXML
    public TextArea txtBody;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        txtTopic.setText("/chat");
//        txtBody.setText("{\"username\":\"osamaazazaza\",\"msg\":\"msg\",\"dateTime\":\"2023-06-07 03:36:27\"}");
        root.disableProperty().bind(websocketService.isConnectProperty().not());
        websocketService.isConnectProperty().addListener((observable, oldValue, newValue) -> {
            new Thread(() -> Platform.runLater(() -> {
                if(!newValue){
                    txtTopic.setText("");
                    txtBody.setText("");
                }
            })).start();
        });
    }

    @FXML
    public void onClickSend(){
        websocketService.send(txtTopic.getText(),txtBody.getText());
    }
}
