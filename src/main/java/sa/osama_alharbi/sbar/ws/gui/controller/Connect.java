package sa.osama_alharbi.sbar.ws.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sa.osama_alharbi.sbar.ws.service.WebsocketService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class Connect extends InitView implements Initializable {
    public static boolean HAS_BEAN = true;
    public static String NAME = "Connect";

    private final WebsocketService websocketService;

    @FXML
    public Button btnConnectDisconnect;
    @FXML
    public TextField txtHost;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtHost.textProperty().bindBidirectional(websocketService.hostProperty());
        btnConnectDisconnect.setOnAction(event -> {
            if(websocketService.isIsConnect()){
                new Thread(() -> Platform.runLater(() -> {
                    websocketService.disconnect();
                })).start();
            }else{
                new Thread(() -> Platform.runLater(() -> {
                    websocketService.connect();
                })).start();
            }
        });
        txtHost.disableProperty().bind(websocketService.isConnectProperty());
        websocketService.isConnectProperty().addListener((observable, oldValue, newValue) -> {
            new Thread(() -> Platform.runLater(() -> {
                if(newValue){
                    btnConnectDisconnect.setText("Disconnect");
                }else{
                    btnConnectDisconnect.setText("Connect");
                }
            })).start();
        });
    }
}
