package shared.managers;
import server.dao.DAO;
import server.dao.OrganizationDaoImpl;
import shared.auth.User;
import shared.auth.UserManager;
import shared.core.Exceptions.IncorrectValueException;
import shared.core.models.Address;
import shared.core.models.Organization;
import shared.io.UserIO;
import shared.network.factory.ResponseFactory;
import shared.network.messaging.RequestBody;
import shared.network.messaging.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@XmlRootElement(name = "organizations")
public class CollectionManager {
    @XmlElement(name = "organization")
    private Vector<Organization> organizationVector = new Vector<>();
    private OrganizationDaoImpl organizationDao;
    private LinkedList<Long> listForGenerateId = new LinkedList<>();
    private UserManager userManager;
    private static Logger logger = Logger.getLogger(CollectionManager.class.getName());

    public CollectionManager(OrganizationDaoImpl organizationDao, UserManager userManager) {
        this.organizationDao = organizationDao;
        organizationVector = organizationDao.read();
        if(organizationVector!=null){
            logger.info("Коллекция загружена из базы данных!");
        }
        Collections.sort(organizationVector);
        this.userManager = userManager;
    }
    public CollectionManager(){}


    public Vector<Organization> getCollectionVector(){
        return this.organizationVector;

    }

    @Override
    public String toString(){
        return "Тип коллекции: Vector"
                + "\n" + "Количество элементов в коллекции: " + organizationVector.size() +"\n"
                + "Дата создания коллекции: " + getCollectionVector().firstElement().getCreationDate().toString();

    }
    public String clearCollection(){
        this.organizationVector = new Vector<>();
        return "Коллекция успешно очищена!";
    }

    public synchronized Response clearUserCollection(RequestBody requestBody){
        User user = userManager.findUser(requestBody.getCredentials());
        Optional<Organization> organizationOptional = organizationVector.stream().filter(userId -> userId.getUserId()==user.getId()).findFirst();
        if(organizationOptional.isPresent()){
            if(organizationDao.deleteOrganizations(organizationOptional.get())!= DAO.ERROR){
                organizationVector.removeIf(organization -> organization.getUserId()== organizationOptional.get().getUserId());
                return ResponseFactory.createResponse("Все ваши объекты были успешно удалены!");
            }
        }
        return ResponseFactory.createResponse("Ваших объектов нет в коллекции, но вы можете их создать командой add");
    }
    public String removeFirst(){
        this.organizationVector = organizationVector.stream().skip(1).collect(Collectors.toCollection(Vector::new));
        return "Первый элемент успешно удален!";
    }
    public void addNewElement(Organization organization){
        organizationVector.add(organization);
    }
    public synchronized boolean addNewOrganization(Organization organization) {
        int id = (int) organizationDao.create(organization);
        if(id!=DAO.ERROR){
            organization.setId((long) id);
            organizationVector.add(organization);
            return true;
        }
        return false;

    }
    public boolean updateElement(Organization organization) throws SQLException {
        int returnCode  = (int) organizationDao.update(organization);
        if(returnCode!=DAO.ERROR){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean greaterThanMax(Organization organization){
        if(organization.getAnnualTurnover()>organizationVector.stream().filter(org->org.getAnnualTurnover()!=0).
        map(Organization::getAnnualTurnover).max(Comparator.naturalOrder()).orElse(null)){
            return true;
        }
        else{
            return false;
        }
    }
    public void fillListId(){
        for(Organization organization : organizationVector){
            listForGenerateId.add(organization.getId());
        }
    }

    public Long generateId(){
        Long id;
        fillListId();
        boolean flag = true;
        while (flag) {
            id = (long) (1L + (Math.random() * (listForGenerateId.size()+1)));
            if (!listForGenerateId.contains(id)) {
                listForGenerateId.add(id);
                return id;
            }
        }
        return null;
    }
    public boolean checkId(Long id){
        if (listForGenerateId.contains(id)) {
            return true;
        }
        else{
            return false;
        }

    }

    public int countGreaterThanOfficicalAddress(Address officialAddress){
        int count = 0;
        for (Organization organization : getCollectionVector()) {
            int result = organization.getOfficialAddress().getStreet().compareTo(String.valueOf(officialAddress));
            if (result > 0) {
                count += 1;
            }
        }
        return count;
    }
    public boolean removeById(Long id){
        return (getCollectionVector().removeIf(elem -> elem.getId() == id));
    }
    public synchronized Response removeById(RequestBody requestBody, Long id) throws SQLException {
        User user = userManager.findUser(requestBody.getCredentials());
        Optional<Organization> organizationOptional = organizationVector.stream().filter(org -> org.getUserId() == user.getId()).filter(u->u.getId() == id).findAny();
        if(organizationOptional.isPresent()){
            if(organizationDao.deleteById(organizationOptional.get())!=DAO.ERROR){
                organizationVector.removeIf(organization -> organization.getId()==id);
                return ResponseFactory.createResponse("Элемент был успешно удален!");
            }
        }
        return ResponseFactory.createResponse("Данный элемент принадлежит не вам или элемента с таким id нет!");
    }
    public boolean removeGreater(Float annualTurnover){
        return (getCollectionVector().removeIf(elem -> elem.getAnnualTurnover()>annualTurnover));

    }

    public synchronized Response removeGreater(RequestBody requestBody, float annualTurnover) throws SQLException {
        User user = userManager.findUser(requestBody.getCredentials());
        Optional<Organization> organizationOptional = organizationVector.stream().filter(org -> org.getUserId() == user.getId()).filter(el -> el.getAnnualTurnover()>annualTurnover).findAny();
        if(organizationOptional.isPresent()){
            ArrayList<Organization> organizationArrayList = organizationVector.stream().toList().stream().filter(org -> org.getUserId() == user.getId()).filter(el -> el.getAnnualTurnover()>annualTurnover).collect(Collectors.toCollection(ArrayList::new));
            int isNotError = 0;
            for(Organization organization : organizationArrayList){
                isNotError = organizationDao.deleteById(organization);
            }
            if(isNotError!=DAO.ERROR) {
                organizationVector.removeIf((elem -> elem.getAnnualTurnover() > annualTurnover && elem.getUserId() == user.getId()));
                return ResponseFactory.createResponse("Элементы были успешно удалены!");
            }
        }
        return ResponseFactory.createResponse("У вас пока что нет своих элементов или все элементы не превышают заданный");

    }
    public synchronized Response removeFirst(RequestBody requestBody) throws SQLException {
        User user = userManager.findUser(requestBody.getCredentials());
        Long id = organizationVector.firstElement().getId();
        Optional<Organization> organizationOptional = organizationVector.stream().filter(org -> org.getUserId() == user.getId()).filter(u->u.getId() == id).findAny();
        if(organizationOptional.isPresent()){
            if(organizationDao.deleteById(organizationOptional.get())!=DAO.ERROR){
                removeFirst();
                return ResponseFactory.createResponse("Первый элемент был успешно удален!");
            }
        }
        return ResponseFactory.createResponse("Первый элемент коллекции принадлежит не вам!");

    }
    public void checkCollection(Vector<Organization> vectorOrganization){
        UserIO userIO = new UserIO();
        try{
            for(Organization organization : vectorOrganization){
                organization.setId(organization.getId());
                if(checkId(organization.getId())) throw new IncorrectValueException("идентификатор должен быть уникальным");
                listForGenerateId.add(organization.getId());
                organization.setName(organization.getName());
                organization.getCoordinates().setX(organization.getCoordinates().getX());
                organization.getCoordinates().setY(organization.getCoordinates().getY());
                organization.setCreationDate(organization.getCreationDate());
                organization.setType(organization.getType());
                organization.setFullName(organization.getFullName());
                organization.getOfficialAddress().setStreet(organization.getOfficialAddress().getStreet());
                organization.setAnnualTurnover(organization.getAnnualTurnover());
            }
            this.organizationVector = vectorOrganization;
        }catch (IllegalArgumentException | IncorrectValueException ex){
            userIO.println(ex.getMessage());
            userIO.println("Данные в файле неверны, проверьте, возможно у вас нет нужных тегов");
            this.organizationVector = new Vector<>();

        }
    }


}
