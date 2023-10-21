package sa.osama_alharbi.sbar.ws.gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WebsocketResponseModel {
    private StringProperty topic;
    private StringProperty body;

    public WebsocketResponseModel(String topic, String body) {
        this.topic = new SimpleStringProperty(topic);
        this.body = new SimpleStringProperty(body);
    }

    public String getTopic() {
        return topic.get();
    }

    public StringProperty topicProperty() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public String getBody() {
        return body.get();
    }

    public StringProperty bodyProperty() {
        return body;
    }

    public void setBody(String body) {
        this.body.set(body);
    }
}
