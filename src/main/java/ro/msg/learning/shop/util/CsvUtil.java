package ro.msg.learning.shop.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Component
public class CsvUtil {

    private static final CsvMapper csvMapper = new CsvMapper();

    public static <T> List<T> fromCsv(Class<T> type, InputStream csvStream) throws IOException {
        CsvSchema schema = csvMapper
                .schemaFor(type)
                .withHeader();

        MappingIterator<T> iterator = csvMapper
                .readerFor(type)
                .with(schema)
                .readValues(csvStream);

        return iterator.readAll();
    }

    public static <T> void toCsv(Class<T> type, List<T> typeList, OutputStream out) throws IOException {
        CsvSchema schema = csvMapper
                .schemaFor(type)
                .withHeader();

        csvMapper
                .writer(schema)
                .writeValue(out,typeList);
    }
}
