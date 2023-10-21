package sa.osama_alharbi.sbar.ws.gui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import sa.osama_alharbi.sbar.ws.gui.IncGUI;
import sa.osama_alharbi.sbar.ws.gui.model.WebsocketResponseModel;
import sa.osama_alharbi.sbar.ws.service.WebsocketService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequiredArgsConstructor
public class Subscribe extends InitView implements Initializable {
    public static boolean HAS_BEAN = true;
    public static String NAME = "Subscribe";
    public final WebsocketService websocketService;
    public final IncGUI incGUI;

    @FXML
    public AnchorPane root;

    @FXML
    public TextField txtTopic;
    @FXML
    public ListView<Subscribe_item> lstMsg;
    public ObservableList<Subscribe_item> obsLstMsg;
    public FilteredList<Subscribe_item> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obsLstMsg = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(obsLstMsg, s -> true);
        lstMsg.setItems(filteredData);
        txtTopic.textProperty().addListener(obs->{
            String filter = txtTopic.getText();
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }else{
                filteredData.setPredicate(s -> s.getWebsocketResponseModel().getTopic().contains(filter));
            }
        });
        lstMsg.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Subscribe_item item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty && item != null){
                    setGraphic(item.root);
                }else{
                    setText(null);
                    setGraphic(null);
                }
            }
        });
        root.disableProperty().bind(websocketService.isConnectProperty().not());
        websocketService.isConnectProperty().addListener((observable, oldValue, newValue) -> {
            new Thread(() -> Platform.runLater(() -> {
                obsLstMsg.clear();
                txtTopic.setText("");
            })).start();
        });
    }

    public void onAddNewMsg(String path,String body){
        AtomicReference<Subscribe_item> subscribe_item = new AtomicReference<>();
        incGUI.<Subscribe_item, VBox>generateView(Subscribe_item.NAME,Subscribe_item.HAS_BEAN).forEach((i, o) -> {
            switch (i){
                case IncGUI.MAP_PAIN: break;
                case IncGUI.MAP_CONTROLLER: subscribe_item.set((Subscribe_item) o);break;
            }
        });
        obsLstMsg.add(subscribe_item.get().setWebsocketResponseModel(new WebsocketResponseModel(path,body)));
    }
}
