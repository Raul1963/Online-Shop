package ro.msg.learning.shop.util;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class CsvMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public CsvMessageConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if(o instanceof List<?> list) {
            if(list.isEmpty()) return;

            Class<?> clazz = list.get(0).getClass();
            CsvUtil.toCsv(clazz, (List) o, outputMessage.getBody());
        }

    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException{
        return CsvUtil.fromCsv(clazz, inputMessage.getBody());
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        if(type instanceof ParameterizedType pt){
            Class<?> clazz = (Class<?>) pt.getActualTypeArguments()[0];
            return CsvUtil.fromCsv(clazz, inputMessage.getBody());
        }

        return CsvUtil.fromCsv((Class<?>) type, inputMessage.getBody());
    }
}
