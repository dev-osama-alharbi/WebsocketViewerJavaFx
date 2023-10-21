package sa.osama_alharbi.sbar.ws.gui;

import org.springframework.stereotype.Service;
import sa.osama_alharbi.sbar.ws.gui.controller.Connect;
import sa.osama_alharbi.sbar.ws.gui.controller.Root;
import sa.osama_alharbi.sbar.ws.gui.controller.Send;
import sa.osama_alharbi.sbar.ws.gui.controller.Subscribe;

@Service
public class GUIControllerService {
    public Root root;
    public Subscribe subscribe;
    public Send send;
    public Connect connect;
}
