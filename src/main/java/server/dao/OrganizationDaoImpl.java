package server.dao;

import server.Queries;
import server.ServerConnection.ServerConnection;
import shared.core.models.Address;
import shared.core.models.Coordinates;
import shared.core.models.Organization;
import shared.core.models.OrganizationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;
import java.util.logging.Logger;

public class OrganizationDaoImpl implements DAO<Organization>{

    private final Connection connection;
    private static final int SUCCESS = 0;

    private static final int ORGANIZATION_NAME_POS = 1;
    private static final int ORGANIZATION_ID_POS = 1;
    private static final int COORDINATE_X_POS = 1;
    private static final int ADDRESS_STREET_POS = 1;
    private static final int COORDINATES_TABLE_ID_POS = 1;
    private static final int COORDINATES_ORGANIZATION_TABLE_ID_POS = 2;
    private static final int COORDINATE_Y_POS = 2;
    private static final int USER_ID_ADDRESS = 2;
    private static final int CREATION_DATE_POS = 3;
    private static final int USER_ID_COORDINATES = 3;
    private static final int ANNUAL_TURNOVER_POS = 4;
    private static final int FULL_NAME_POS = 5;
    private static final int ORGANIZATION_TYPE_POS = 6;
    private static final int ORGANIZATION_ADDRESS_POS = 7;
    private static final int USER_ID_ORGANIZATION_TABLE_POS = 8;
    private static final int OFFICIALADDRESS_TABLE_ID_POS = 1;

    private static final int USER_ID_POS = 1;
    private static Logger logger = Logger.getLogger(OrganizationDaoImpl.class.getName());

    public OrganizationDaoImpl(Connection connection){
        this.connection = connection;
    }
    @Override
    public Vector<Organization> read(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.READ_DB_ORGANIZATIONS.getQuery());
            PreparedStatement preparedStatementUser = connection.prepareStatement(Queries.READ_DB_USER.getQuery());
            ResultSet resultSet = preparedStatement.executeQuery();
            Vector<Organization> organizationVectorDB = new Vector<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Coordinates coordinates = null;
                PreparedStatement preparedStatementCoordinatesTable = connection.prepareStatement(Queries.READ_DB_COORDINATES_BY_ORGANIZATION.getQuery());
                preparedStatementCoordinatesTable.setLong(COORDINATES_TABLE_ID_POS, id);
                ResultSet resultSetCoordinates = preparedStatementCoordinatesTable.executeQuery();
                if (resultSetCoordinates.next()) {
                    float x = resultSetCoordinates.getFloat("x");
                    int y = resultSetCoordinates.getInt("y");
                    coordinates = new Coordinates(x, y);
                }
                resultSetCoordinates.next();
                LocalDate creationDate = LocalDate.parse(resultSet.getString("creationdate"));
                float annualTurnover = resultSet.getFloat("annualturnover");
                String fulName = resultSet.getString("fullname");
                OrganizationType organizationType = OrganizationType.valueOf((String) resultSet.getObject("type"));
                Address officialAddress = null;
                PreparedStatement preparedStatementAddress = connection.prepareStatement(Queries.READ_DB_ADDRESS_BY_ORGANIZATION.getQuery());
                preparedStatementAddress.setLong(OFFICIALADDRESS_TABLE_ID_POS, id);
                ResultSet resultSetAddress = preparedStatementAddress.executeQuery();
                if (resultSetAddress.next()) {
                    officialAddress = new Address(resultSetAddress.getString("street"));
                }
                resultSetAddress.next();
                int userId = resultSet.getInt("user_id");
                preparedStatementUser.setInt(USER_ID_POS, userId);
                ResultSet resultSetUser = preparedStatementUser.executeQuery();
                resultSetUser.next();
                String login = resultSetUser.getString("login");
                Organization organization = new Organization(id, name, coordinates, creationDate, annualTurnover, fulName, organizationType, officialAddress, userId, login);
                organizationVectorDB.add(organization);
            }
            return organizationVectorDB;
        }catch (SQLException ex){
            logger.warning("Произошла ошибка при загрузки коллекции из базы данных!");
        }
        return null;
    }

    @Override
    public long create(Organization organization) {
            try {
                PreparedStatement preparedStatementCreateOrg = connection.prepareStatement(Queries.CREATE_DB_ORGANIZATION.getQuery());
                preparedStatementCreateOrg.setString(ORGANIZATION_NAME_POS, organization.getName());
                preparedStatementCreateOrg.setInt(COORDINATES_ORGANIZATION_TABLE_ID_POS, createCoordinates(organization.getCoordinates(), organization));
                preparedStatementCreateOrg.setString(CREATION_DATE_POS, organization.getCreationDate().toString());
                preparedStatementCreateOrg.setFloat(ANNUAL_TURNOVER_POS, organization.getAnnualTurnover());
                preparedStatementCreateOrg.setString(FULL_NAME_POS, organization.getFullName());
                preparedStatementCreateOrg.setString(ORGANIZATION_TYPE_POS, organization.getType().toString());
                preparedStatementCreateOrg.setLong(ORGANIZATION_ADDRESS_POS, createAddress(organization.getOfficialAddress(), organization));
                preparedStatementCreateOrg.setInt(USER_ID_ORGANIZATION_TABLE_POS, organization.getUserId());
                ResultSet resultSetOrganization = preparedStatementCreateOrg.executeQuery();
                resultSetOrganization.next();
                long id = resultSetOrganization.getLong("id");
                resultSetOrganization.close();
                return id;
            }catch (SQLException ex){
                return ERROR;
            }
    }

    private int createCoordinates(Coordinates coordinates, Organization organization){
        final int CREATOR_ID_POS = 3;
        try {
            PreparedStatement preparedStatementCreateCoord = connection.prepareStatement(Queries.CREATE_DB_COORDINATES.getQuery());
            preparedStatementCreateCoord.setFloat(COORDINATE_X_POS, coordinates.getX());
            preparedStatementCreateCoord.setInt(COORDINATE_Y_POS, coordinates.getY());
            preparedStatementCreateCoord.setInt(CREATOR_ID_POS, organization.getUserId());
            ResultSet resultSetCoordinates = preparedStatementCreateCoord.executeQuery();
            resultSetCoordinates.next();
            int id = resultSetCoordinates.getInt("id");
            resultSetCoordinates.close();
            return id;
        }catch (SQLException ex){
            return ERROR;
        }

    }

    private long createAddress(Address officialAddress, Organization organization){
        final int CREATOR_ID_POS = 2;
        try {
            PreparedStatement preparedStatementCreateAddress = connection.prepareStatement(Queries.CREATE_DB_ADDRESS.getQuery());
            preparedStatementCreateAddress.setString(ADDRESS_STREET_POS, officialAddress.getStreet());
            preparedStatementCreateAddress.setInt(CREATOR_ID_POS, organization.getUserId());
            ResultSet resultSetAddress = preparedStatementCreateAddress.executeQuery();
            resultSetAddress.next();
            long id = resultSetAddress.getLong("id");
            resultSetAddress.close();
            return id;
        }catch (SQLException ex){
            return ERROR;
        }

    }

    public long update(Organization organization){
        final int creationDate = 2;
        final int annualTurnoverPos = 3;
        final int fullNamePos = 4;
        final int typePos = 5;
        final int userIdPos = 6;
        final int organizationIdPos = 7;
        try {
            PreparedStatement preparedStatementUpdateOrganization = connection.prepareStatement(Queries.UPDATE_ORGANIZATION.getQuery());
            preparedStatementUpdateOrganization.setString(ORGANIZATION_NAME_POS, organization.getName());
            preparedStatementUpdateOrganization.setString(creationDate, organization.getCreationDate().toString());
            preparedStatementUpdateOrganization.setFloat(annualTurnoverPos, organization.getAnnualTurnover());
            preparedStatementUpdateOrganization.setString(fullNamePos, organization.getFullName());
            preparedStatementUpdateOrganization.setString(typePos, organization.getType().toString());
            preparedStatementUpdateOrganization.setInt(userIdPos, organization.getUserId());
            preparedStatementUpdateOrganization.setLong(organizationIdPos, organization.getId());
            preparedStatementUpdateOrganization.executeUpdate();
            updateCoordinates(organization.getCoordinates(), organization);
            updateAddress(organization.getOfficialAddress(), organization);
            return SUCCESS;
        }catch (SQLException ex){
            return ERROR;
        }
    }

    private int updateCoordinates(Coordinates coordinates, Organization organization) {
        final int X_COORDINATE_POS = 1;
        final int Y_COORDINATE_POS = 2;
        final int userIdPos = 3;
        final int organizationIdPos = 4;
        try {
            PreparedStatement preparedStatementUpdateCoordinates = connection.prepareStatement(Queries.UPDATE_COORDINATES.getQuery());
            preparedStatementUpdateCoordinates.setFloat(X_COORDINATE_POS, coordinates.getX());
            preparedStatementUpdateCoordinates.setInt(Y_COORDINATE_POS, coordinates.getY());
            preparedStatementUpdateCoordinates.setInt(userIdPos, organization.getUserId());
            preparedStatementUpdateCoordinates.setLong(organizationIdPos, organization.getId());
            preparedStatementUpdateCoordinates.executeUpdate();
            return SUCCESS;
        }catch (SQLException ex){
            return ERROR;
        }
    }

    private int updateAddress(Address address, Organization organization){
        final int STREET_POS = 1;
        final int userIdPos = 2;
        final int organizationIdPos = 3;
        try {
            PreparedStatement preparedStatementUpdateAddress = connection.prepareStatement(Queries.UPDATE_ADDRESS.getQuery());
            preparedStatementUpdateAddress.setString(STREET_POS, address.getStreet());
            preparedStatementUpdateAddress.setInt(userIdPos, organization.getUserId());
            preparedStatementUpdateAddress.setLong(organizationIdPos, organization.getId());
            preparedStatementUpdateAddress.executeUpdate();
            return SUCCESS;
        }catch (SQLException ex){
            return ERROR;
        }
    }

    public int deleteOrganizations(Organization organization) {
        try {
            PreparedStatement preparedStatementOrganization = connection.prepareStatement(Queries.DELETE_DB_ORGANIZATION.getQuery());
            PreparedStatement preparedStatementCoordinates = connection.prepareStatement(Queries.DELETE_DB_COORDINATES.getQuery());
            PreparedStatement preparedStatementAddress = connection.prepareStatement(Queries.DELETE_DB_ADDRESS.getQuery());
            int user_id = organization.getUserId();
            preparedStatementOrganization.setInt(USER_ID_POS, user_id);
            preparedStatementCoordinates.setInt(USER_ID_POS, user_id);
            preparedStatementAddress.setInt(USER_ID_POS, user_id);

            preparedStatementOrganization.executeUpdate();
            preparedStatementCoordinates.executeUpdate();
            preparedStatementAddress.executeUpdate();
            return SUCCESS;
        }catch (SQLException ex){
            return ERROR;
        }
    }

    public int deleteById(Organization organization){
        try {
            PreparedStatement preparedStatementOrganization = connection.prepareStatement(Queries.DELETE_DB_ORGANIZATION_BY_ID.getQuery());
            PreparedStatement preparedStatementCoordinates = connection.prepareStatement(Queries.DELETE_DB_COORDINATES_BY_ID.getQuery());
            PreparedStatement preparedStatementAddress = connection.prepareStatement(Queries.DELETE_DB_ADDRESS_BY_ID.getQuery());

            int user_id = organization.getUserId();
            long id = organization.getId();
            int coordId = getCoordinatesId(organization);
            int addrsId = getAddressId(organization);
            System.out.println(coordId);
            System.out.println(addrsId);
            if (coordId != ERROR && addrsId != ERROR) {
                preparedStatementOrganization.setLong(1, id);
                preparedStatementOrganization.setInt(2, user_id);
                preparedStatementCoordinates.setInt(1, coordId);
                preparedStatementCoordinates.setInt(2, user_id);
                preparedStatementAddress.setInt(1, addrsId);
                preparedStatementAddress.setInt(2, user_id);
                preparedStatementOrganization.executeUpdate();
                preparedStatementCoordinates.executeUpdate();
                preparedStatementAddress.executeUpdate();
                return SUCCESS;
            }
        }catch (SQLException ex){
            return ERROR;
        }
    return ERROR;
    }

    private int getCoordinatesId(Organization organization) {
        final int CREATOR_ID_POS = 2;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.FIND_COORDINATES_BY_ORGANIZATION_ID.getQuery());
            preparedStatement.setLong(COORDINATES_TABLE_ID_POS, organization.getId());
            preparedStatement.setInt(CREATOR_ID_POS, organization.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id_coordinates = resultSet.getInt("coordinates");
            resultSet.next();
            resultSet.close();
            return id_coordinates;
        }catch (SQLException ex){
            return ERROR;
        }

    }
    private int getAddressId(Organization organization){
        final int CREATOR_ID_POS = 2;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.FIND_ADDRESS_BY_ID.getQuery());
            preparedStatement.setInt(CREATOR_ID_POS, organization.getUserId());
            preparedStatement.setLong(ADDRESS_STREET_POS, organization.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id_address = resultSet.getInt("officialaddress");
            return id_address;
        }catch (SQLException ex){
            return ERROR;
        }
    }
}
