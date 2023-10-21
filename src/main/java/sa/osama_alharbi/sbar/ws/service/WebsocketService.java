package sa.osama_alharbi.sbar.ws.service;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.annotation.PostConstruct;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import sa.osama_alharbi.sbar.ws.config.MyStringMessageConverter;
import sa.osama_alharbi.sbar.ws.gui.GUIControllerService;
import sa.osama_alharbi.sbar.ws.handler.MyStompSessionHandler;
import sa.osama_alharbi.sbar.ws.var.SubscriptionTopics;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class WebsocketService {
    private WebSocketClient client;
    private WebSocketStompClient stompClient;
    private StompSessionHandler stompSessionHandler;
    private StompSession stompSession;
    private BooleanProperty isConnect = new SimpleBooleanProperty(false);
    private StringProperty host = new SimpleStringProperty("ws://localhost:8080/ws");

    private List<StompSession.Subscription> subscriptions = new ArrayList<>();

    private final GUIControllerService controller;


    @PostConstruct
    public void init(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        threadPoolTaskScheduler.initialize();

        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setTaskScheduler(threadPoolTaskScheduler);
        stompClient.setDefaultHeartbeat(new long[]{10000,10000});
        stompClient.setMessageConverter(new MyStringMessageConverter());
        stompSessionHandler = new MyStompSessionHandler(this);

        isConnect.addListener((observable, oldValue, newValue) -> {
            subscriptions.clear();
            if(newValue){
                SubscriptionTopics.topics.forEach(path -> {
                    subscriptions.add(stompSession.subscribe(path, new StompFrameHandler() {
                        @Override
                        public Type getPayloadType(StompHeaders headers) {
                            return String.class;
                        }

                        @Override
                        public void handleFrame(StompHeaders headers, Object payload) {
                            new Thread(() -> Platform.runLater(() -> {
                                controller.subscribe.onAddNewMsg(path,(String) payload);
                            })).start();
                        }
                    }));
                });
            }
        });
    }

    public boolean connect(){
        if(!isConnect.get()){
            try {
                stompSession = stompClient.connectAsync(getHost(),stompSessionHandler).get();
            } catch (InterruptedException e) {
                System.out.println("[ws Exception] InterruptedException");
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                System.out.println("[ws Exception] ExecutionException");
                throw new RuntimeException(e);
            }
            return true;
        }else{
            return false;
        }
    }

    public boolean disconnect(){
        if(isConnect.get()){
            subscriptions.forEach(subscription -> subscription.unsubscribe());
            stompSession.disconnect();
            setIsConnect(false);
            return true;
        }else{
            return false;
        }
    }

    public boolean send(String path, Object body){
        if(isIsConnect()){
            stompSession.send(path,body);
            return true;
        }else{
            return false;
        }
    }

    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> {

            // formatter
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // deserializers
            builder.deserializers(new LocalDateDeserializer(dateFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

            // serializers
            builder.serializers(new LocalDateSerializer(dateFormatter));
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
        };
    }

    public boolean isIsConnect() {
        return isConnect.get();
    }

    public BooleanProperty isConnectProperty() {
        return isConnect;
    }

    public void setIsConnect(boolean isConnect) {
        this.isConnect.set(isConnect);
    }

    public String getHost() {
        return host.get();
    }

    public StringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }
}
