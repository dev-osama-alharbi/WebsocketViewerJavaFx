package sa.osama_alharbi.sbar.ws.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class Root extends InitView{
    public static boolean HAS_BEAN = false;
    public static String NAME = "Root";

    @FXML
    public BorderPane subPane;
    @FXML
    public BorderPane sendPane;
    @FXML
    public BorderPane connectPane;
}
