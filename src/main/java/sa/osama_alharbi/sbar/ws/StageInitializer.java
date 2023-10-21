package sa.osama_alharbi.sbar.ws;

import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import sa.osama_alharbi.sbar.ws.gui.IncGUI;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<JavaFxLauncher.StageReadyEvent> {
    private final IncGUI gui;

    @Override
    public void onApplicationEvent(JavaFxLauncher.StageReadyEvent event) {
        Stage stage = event.getStage();
        gui.start(stage);
    }
}
