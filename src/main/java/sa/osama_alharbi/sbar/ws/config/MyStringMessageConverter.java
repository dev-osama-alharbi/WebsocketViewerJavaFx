package sa.osama_alharbi.sbar.ws.config;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

public class MyStringMessageConverter extends AbstractMessageConverter {
    private final Charset defaultCharset;

    public MyStringMessageConverter() {
        this(StandardCharsets.UTF_8);
    }

    public MyStringMessageConverter(Charset defaultCharset) {
        super(new MimeType("application", "json", defaultCharset));
        Assert.notNull(defaultCharset, "Default Charset must not be null");
        this.defaultCharset = defaultCharset;
    }

    protected boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }

    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, @Nullable Object conversionHint) {
        Charset charset = this.getContentTypeCharset(this.getMimeType(message.getHeaders()));
        Object payload = message.getPayload();
        return payload instanceof String ? payload : new String((byte[])payload, charset);
    }

    @Nullable
    protected Object convertToInternal(Object payload, @Nullable MessageHeaders headers, @Nullable Object conversionHint) {
        if (byte[].class == this.getSerializedPayloadClass()) {
            Charset charset = this.getContentTypeCharset(this.getMimeType(headers));
            payload = ((String)payload).getBytes(charset);
        }

        return payload;
    }

    private Charset getContentTypeCharset(@Nullable MimeType mimeType) {
        return mimeType != null && mimeType.getCharset() != null ? mimeType.getCharset() : this.defaultCharset;
    }
}
