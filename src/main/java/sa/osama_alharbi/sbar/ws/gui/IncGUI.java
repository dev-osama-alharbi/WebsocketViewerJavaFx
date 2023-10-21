package sa.osama_alharbi.sbar.ws.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sa.osama_alharbi.sbar.ws.JavaFxLauncher;
import sa.osama_alharbi.sbar.ws.gui.controller.*;
import sa.osama_alharbi.sbar.ws.gui.view.IndexView;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class IncGUI {
    public Stage stage;
    public Scene scene;

    // TODO pain
    public AnchorPane gui_Root,gui_Subscribe,gui_Send,gui_Connect;
    private final GUIControllerService controller;


    public void start(Stage stage) {
        this.stage = stage;
        generateGUI();
    }


    private void generateGUI() {
        this.<Root, AnchorPane>generateView(Root.NAME,Root.HAS_BEAN).forEach((i, o) -> {
            switch (i){
                case MAP_PAIN: gui_Root = (AnchorPane) o;break;
                case MAP_CONTROLLER: controller.root = (Root) o;break;
            }
        });
        this.<Subscribe, AnchorPane>generateView(Subscribe.NAME,Subscribe.HAS_BEAN).forEach((i, o) -> {
            switch (i){
                case MAP_PAIN: gui_Subscribe = (AnchorPane) o;break;
                case MAP_CONTROLLER: controller.subscribe = (Subscribe) o;break;
            }
        });
        this.<Send, AnchorPane>generateView(Send.NAME,Send.HAS_BEAN).forEach((i, o) -> {
            switch (i){
                case MAP_PAIN: gui_Send = (AnchorPane) o;break;
                case MAP_CONTROLLER: controller.send = (Send) o;break;
            }
        });
        this.<Connect, AnchorPane>generateView(Connect.NAME,Connect.HAS_BEAN).forEach((i, o) -> {
            switch (i){
                case MAP_PAIN: gui_Connect = (AnchorPane) o;break;
                case MAP_CONTROLLER: controller.connect = (Connect) o;break;
            }
        });


        controller.root.subPane.setCenter(gui_Subscribe);
        controller.root.sendPane.setTop(gui_Send);
        controller.root.connectPane.setCenter(gui_Connect);

        scene = new Scene(gui_Root);
        stage.setScene(scene);
        stage.show();
    }


    public FXMLLoader getLoader(URL url, boolean hasBean) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        if (hasBean) {
            loader.setControllerFactory(JavaFxLauncher.springContext::getBean);
        }
        return loader;
    }

    public Pane getPain(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <C extends InitView,P extends Pane> Map<String,Object> generateView(String name, boolean hasBeen) {
        FXMLLoader loader = getLoader(IndexView.class.getResource(name+".fxml"), hasBeen);
        Map<String,Object> m = new HashMap<>();
        m.put(MAP_PAIN,(P) getPain(loader));
        m.put(MAP_CONTROLLER,loader.<C>getController());
        return m;
    }
    public static final String MAP_PAIN = "PAIN";
    public static final String MAP_CONTROLLER = "CONTROLLER";
}
