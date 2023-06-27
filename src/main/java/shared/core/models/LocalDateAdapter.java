package shared.core.models;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * class for proper data serialization and deserialization
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String dateString){
        return LocalDate.parse(dateString);
    }

    @Override
    public String marshal(LocalDate localDate){
        return localDate.toString();
    }
}
