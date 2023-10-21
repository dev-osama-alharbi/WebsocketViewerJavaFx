package sa.osama_alharbi.sbar.ws.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sa.osama_alharbi.sbar.ws.gui.model.WebsocketResponseModel;

public class Subscribe_item extends InitView{
    public static boolean HAS_BEAN = false;
    public static String NAME = "Subscribe_item";

    @FXML
    public Label lblTopic,lblBody;
    @FXML
    public VBox root;

    private WebsocketResponseModel websocketResponseModel;
    public Subscribe_item setWebsocketResponseModel(WebsocketResponseModel websocketResponseModel){
        this.websocketResponseModel = websocketResponseModel;
        lblTopic.setText(this.websocketResponseModel.getTopic());
        lblBody.setText(this.websocketResponseModel.getBody());
        return this;
    }

    public WebsocketResponseModel getWebsocketResponseModel() {
        return websocketResponseModel;
    }
}
