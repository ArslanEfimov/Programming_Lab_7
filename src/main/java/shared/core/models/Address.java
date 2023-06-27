package shared.core.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Organization class Address
 */
@XmlRootElement(name = "officialAddress")
public class Address implements Serializable {
    @XmlElement(name = "street",required = true)
    private String street; //Длина строки не должна быть больше 130, Поле может быть null

    @XmlTransient
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if(street.length()<130 && !street.equals("")) {
            this.street = street;
        }
    }
    public Address(){

    }

    public Address(String street){
        this.street = street;
    }

    /**
     *
     * @return street in  Address
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                '}';
    }
}
