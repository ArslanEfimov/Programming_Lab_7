package shared.core.models;



import shared.core.Exceptions.IncorrectValueException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Main Organization class
 */
@XmlRootElement(name = "organization")
public class Organization implements Serializable,Comparable<Organization>{
        @XmlElement(name = "id", required = true)
        private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
        @XmlElement(name = "name", required = true)
        private String name; //Поле не может быть null, Строка не может быть пустой
        @XmlElement(name = "coordinates", required = true)
        private Coordinates coordinates; //Поле не может быть null

        private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
        @XmlElement(name = "annualTurnover", required = true)
        private Float annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
        @XmlElement(name = "fullName", required = true)
        private String fullName; //Поле может быть null
        @XmlElement(name = "type", required = true)
        private OrganizationType type; //Поле не может быть null
        @XmlElement(name = "officialAddress", required = true)
        private Address officialAddress; //Поле не может быть null

        private int userId;
        private String userName;

    /**
     * constructor for filling in Organization fields
     * @param id
     * @param name
     * @param coordinates
     * @param creationDate
     * @param annualTurnover
     * @param fullName
     * @param type
     * @param officialAddress
     */
        public Organization(Long id, String name,Coordinates coordinates,java.time.LocalDate creationDate, Float annualTurnover,
                            String fullName, OrganizationType type, Address officialAddress) {
            this.id = id;
            this.name = name;
            this.coordinates = coordinates;
            this.creationDate = creationDate;
            this.annualTurnover = annualTurnover;
            this.fullName = fullName;
            this.officialAddress = officialAddress;
            this.type = type;
        }
    public Organization(Long id, String name,Coordinates coordinates,java.time.LocalDate creationDate, Float annualTurnover,
                        String fullName, OrganizationType type, Address officialAddress, int userId, String userName) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.officialAddress = officialAddress;
        this.type = type;
        this.userId = userId;
        this.userName = userName;
    }


        public Organization(){

        }


        @XmlTransient
        public Long getId() {
            return id;
        }

    /**
     * setter for id
     * @param id
     * @exception IllegalArgumentException
     */
    public void setId(Long id) {
            if(id==null || id<=0){
                throw new IllegalArgumentException("Идентификатор должен быть больше 0 и не может быть нулевым.");
            }
            this.id = id;
        }

        @XmlTransient
        public String getName() {
            return name;
        }

    /**
     * setter for name
     * @param name
     * @throws IncorrectValueException
     */
    public void setName(String name) throws IncorrectValueException {
            if(name == null || name.isEmpty()){
                throw new IncorrectValueException("Имя не может быть нулевым или пустым");
            }
            this.name = name;
        }

        @XmlTransient
        public Coordinates getCoordinates() {
            return coordinates;
        }

    /**
     * setter for Coordinates
     * @param coordinates
     * @throws IncorrectValueException
     */
    public void setCoordinates(Coordinates coordinates) throws IncorrectValueException {
            if(coordinates == null){
                throw new IncorrectValueException("Координаты не могут быть нулевыми");
            }
            this.coordinates = coordinates;
        }

        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        public LocalDate getCreationDate() {
            return creationDate;
        }

    /**
     * setter for Creation date
     * @param creationDate
     * @throws IncorrectValueException
     */
    public void setCreationDate(LocalDate creationDate) throws IncorrectValueException {
            if(creationDate == null){
                throw new IncorrectValueException("Дата создания не может быть нулевой");
            }
            this.creationDate = creationDate;
        }

        @XmlTransient
        public Float getAnnualTurnover() {
            return annualTurnover;
        }

    /**
     * setter for Annual Turnover
     * @param annualTurnover
     * @throws IncorrectValueException
     */
    public void setAnnualTurnover(Float annualTurnover) throws IncorrectValueException {
            if(annualTurnover<=0){
                throw new IncorrectValueException("annualTurnover должен быть больше 0");
            }
            this.annualTurnover = annualTurnover;
        }

        @XmlTransient
        public String getFullName() {
            return fullName;
        }

    /**
     * setter for full Name Organization
     * @param fullName
     * @throws IncorrectValueException
     */
    public void setFullName(String fullName) throws IncorrectValueException {
            if(fullName!=null && fullName.isEmpty()){
                throw new IncorrectValueException("fullName не может быть пустой строкой");
            }
            this.fullName = fullName;
        }

        @XmlTransient
        public OrganizationType getType() {
            return type;
        }

    /**
     * setter for type Organization
     * @param type
     */
    public void setType(OrganizationType type) {
            this.type = type;
        }

        @XmlTransient
        public Address getOfficialAddress() {
            return officialAddress;
        }

    /**
     * setter for Official Address
     * @param officialAddress
     */
    public void setOfficialAddress(Address officialAddress) {

            this.officialAddress = officialAddress;
        }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return {id, name, coordinates, creationDate, annualTurnover, fullName, type, officialAddress},
     */
    @Override
        public String toString() {
            return " userName = " + userName + '\n' +
                    " userId = "+ userId + '\n' +
                    " Organization{" +
                    "id=" + id +
                    " name=" + name + '\n' +
                    " coordinates=" + coordinates + '\n' +
                    " creationDate=" + creationDate + '\n' +
                    " annualTurnover=" + annualTurnover + '\n' +
                    " fullName=" + fullName + '\n' +
                    " type=" + type + '\n' +
                    " officialAddress=" + officialAddress +
                    '}'+'\n'+'\n';
        }


    @Override
    public int compareTo(Organization o) {
        return (int) (this.getCoordinates().getX()-o.getCoordinates().getX());
    }
}
