package sa.osama_alharbi.sbar.ws.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import sa.osama_alharbi.sbar.ws.service.WebsocketService;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private final WebsocketService websocketService;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        super.afterConnected(session, connectedHeaders);
        websocketService.setIsConnect(true);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return super.getPayloadType(headers);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        super.handleFrame(headers, payload);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        super.handleTransportError(session, exception);
        if(exception.getClass().getSimpleName().equals("ConnectionLostException")){
            websocketService.setIsConnect(false);
        }else{
            exception.printStackTrace();
        }
    }
}