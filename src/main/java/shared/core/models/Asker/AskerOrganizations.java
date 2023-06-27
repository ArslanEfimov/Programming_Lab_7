package shared.core.models.Asker;

import shared.core.Exceptions.IncorrectValueException;
import shared.core.Exceptions.MustNotBeEmptyException;
import shared.core.Exceptions.NotInDeclaredLimitsException;
import shared.core.models.Address;
import shared.core.models.Coordinates;
import shared.core.models.Organization;
import shared.core.models.OrganizationType;
import shared.io.UserIO;
import shared.managers.CollectionManager;

import java.time.LocalDate;


/**
 * builds an object of type Organization
 */
public class AskerOrganizations {
    private UserIO userIO;
    private CollectionManager collectionManager;

    public AskerOrganizations() {
        this.userIO = new UserIO();
        this.collectionManager = collectionManager;
    }

    /**
     * set correct name for Organization
     * @return name
     * @exception MustNotBeEmptyException
     */
    public String setName(){
        String name;
        while (true) {
            try {
                userIO.print("введите имя: ");
                name = userIO.readLine().trim();
                if (name.isEmpty()) throw new MustNotBeEmptyException();
                break;
            } catch (MustNotBeEmptyException exception) {
                userIO.println("имя не может быть пустым, пожалуйста, введите еще раз");
            }
        }
        return name;
    }

    /**
     * set correct x coordinate for Organization
     * @return x
     * @exception NumberFormatException
     */
    public Float setX() {
        Float x;
        while (true) {
            try {
                userIO.print("введите координату x (Float type): ");
                x = Float.parseFloat(userIO.readLine().trim().replace(",","."));
                break;
            } catch (NumberFormatException ex) {
                userIO.println("координата x должна быть типа (float)");
            }
        }
        return x;

    }

    /**
     * set correct y coordinates for Organization
     * @return y
     * @exception NumberFormatException
     */
    public int setY() {
        int y;
        while (true) {
            try {
                userIO.print("введите координату y (int type), большую -98: ");
                y = Integer.parseInt(userIO.readLine().trim());
                if(y<=-98)  throw new NotInDeclaredLimitsException();
                break;
            } catch (NumberFormatException ex) {
                userIO.println("координата y должна быть типа (int)");
            }catch (NotInDeclaredLimitsException ex){
                userIO.println("координата у должен быть больше -98");
            }
        }
        return y;
    }

    /**
     * set Coordinates
     * @return Coordinates(x, y)
     */
    public Coordinates setCoordinates(){
        Float x = setX();
        int y = setY();
        return new Coordinates(x, y);
    }

    /**
     * set correct annualTurnover for Organization
     * @return annualTurnover
     * @exception NotInDeclaredLimitsException
     * @exception NumberFormatException
     */
    public Float setAnnualTurnover(){
        Float annualTrunover;
        while (true) {
            try {
                userIO.print("введите значение annualЕurnover типа (Float): ");
                annualTrunover = Float.parseFloat(userIO.readLine().trim().replace(",","."));
                if (annualTrunover <= 0) throw new NotInDeclaredLimitsException();
                break;
            } catch (NotInDeclaredLimitsException ex) {
                userIO.println("значение не соответствует указанному пределу");
            } catch (NumberFormatException ex) {
                userIO.println("значение annualTurnover должно быть типа (Float)");
            }
        }
        return annualTrunover;
    }

    /**
     * set correct fullName for Organization
     * @return fullName
     */
    public String setFullName() {
        String fullName;
        userIO.print("введите fullName: ");
        fullName = userIO.readLine().trim();
        if (fullName.isEmpty()) {
            return null;
        }
        return fullName;
    }

    /**
     * set correct street
     * @return street
     * @exception NotInDeclaredLimitsException
     * @exception MustNotBeEmptyException
     */
    public String setStreet(){
        String street;
        while (true) {
            try {
                userIO.print("введите street (длина < 130): ");
                street = userIO.readLine().trim();
                if (street.isEmpty()) throw new MustNotBeEmptyException();
                if (street.length() > 130) throw new NotInDeclaredLimitsException();
                break;
            } catch (NotInDeclaredLimitsException ex) {
                userIO.println("длина строки не должна превышать 130");
            }catch (MustNotBeEmptyException ex){
                userIO.println("не может быть пустым");
            }
        }
        return street;

    }

    /**
     * set officialAddress
     * @return officialAddress
     */
    public Address setOfficialAddress(){
        Address officialAddress = new Address(setStreet());
        return officialAddress;
    }

    /**
     * set correct type of Organization
     * @return type
     * @exception NumberFormatException
     * @exception NotInDeclaredLimitsException
     */
    public OrganizationType setType(){
        userIO.println("  1.COMMERCIAL  \n  2.PUBLIC  \n  3.GOVERNMENT  \n  4.TRUST \n 5.PRIVATE_LIMITED_COMPANY");
        int orgNumber;
        while (true) {
            try {
                userIO.print("введите номер нужного вам типа организации: ");
                orgNumber = Integer.parseInt(userIO.readLine().trim());
                if (orgNumber < 0 || orgNumber > 5) throw new NotInDeclaredLimitsException();
                if (orgNumber == 1) {
                    return OrganizationType.COMMERCIAL;
                }
                if (orgNumber == 2) {
                    return OrganizationType.PUBLIC;
                }
                if (orgNumber == 3) {
                    return OrganizationType.GOVERNMENT;
                }
                if (orgNumber == 4) {
                    return OrganizationType.TRUST;
                }
                if (orgNumber == 5) {
                    return OrganizationType.PRIVATE_LIMITED_COMPANY;
                }
            }catch(NumberFormatException ex){
                userIO.println("число должно быть целым");
            }
            catch(NotInDeclaredLimitsException ex){
                userIO.println("введите еще раз, ввод неверный (выберите int num от 1 до 5)");
            }
        }

    }

    public Organization setOrganization() {
        Organization organization = new Organization();
        try {
            organization.setName(setName());
            organization.setCoordinates(setCoordinates());
            organization.setCreationDate(LocalDate.now());
            organization.setAnnualTurnover(setAnnualTurnover());
            organization.setFullName(setFullName());
            organization.setOfficialAddress(setOfficialAddress());
            organization.setType(setType());
        }catch (IncorrectValueException e) {
            userIO.println(e.getMessage());
        }
        return organization;

    }

}
