package server;

public enum Queries {

    READ_DB_ORGANIZATIONS("select * from organization;"),
    READ_DB_COORDINATES_BY_ORGANIZATION("select x,y from coordinates\n" +
            "join organization org on coordinates.id = org.coordinates where org.id = ?;"),
    READ_DB_ADDRESS_BY_ORGANIZATION("select street from address\n" +
            "join organization org on address.id = org.officialAddress where org.id = ?;"),
    GET_SALT("select salt from user_account where login = ?;"),
    READ_DB_USER("select * from user_account where id = ?;"),
    READ_ALL_DB_USER("select * from user_account;"),
    CREATE_DB_ORGANIZATION("insert into organization(name, coordinates, creationDate, annualTurnover, fullName, type, officialAddress, user_id) " +
            "values (?, ?, ?, ?, ?, cast(? as organization_type), ?, ?) returning id;"),
    CREATE_DB_COORDINATES("insert into coordinates(x, y, user_id) values (?, ?, ?) returning id;"),
    CREATE_DB_ADDRESS("insert into address (street, user_id) values (?, ?) returning id;"),
    CREATE_DB_USER("insert into user_account (login, password, salt) values (?, ?, ?) returning id;"),
    DELETE_DB_ORGANIZATION("delete from organization where user_id = ?;"),
    DELETE_DB_COORDINATES("delete from coordinates where user_id = ?;"),
    DELETE_DB_ADDRESS("delete from address where user_id = ?;"),
    DELETE_DB_ORGANIZATION_BY_ID("delete from organization where id = ? and user_id = ?;"),
    DELETE_DB_COORDINATES_BY_ID("delete from coordinates where id = ? and user_id = ?;"),
    DELETE_DB_ADDRESS_BY_ID("delete from address where id = ? and user_id = ?;"),
    FIND_COORDINATES_BY_ORGANIZATION_ID("select organization.coordinates from organization where id = ? and user_id = ?;"),
    FIND_ADDRESS_BY_ID("select organization.officialaddress from organization where id = ? and user_id = ?;"),
    CREATE_USER_TABLE("create table if not exists user_account(id BIGSERIAL PRIMARY KEY, login text not null, password text not null, salt text not null);"),
    CREATE_COORDINATES_TABLE("create table if not exists coordinates(id BIGSERIAL PRIMARY KEY, x FLOAT NOT NULL, y int NOT NULL CONSTRAINT coordinates_x_check check ( y > '-98'::int  ), user_id integer);"),
    CREATE_ADDRESS_TABLE("create table if not exists address(id BIGSERIAL PRIMARY KEY, street text NOT NULL constraint  street_check check (street <> ''::text and char_length(street)<='130'::int), user_id integer);"),
    CREATE_ORGANIZATION_TABLE("create table if not exists organization(id BIGSERIAL PRIMARY KEY, name text not null constraint name_check check ( name <> ''::text ), coordinates serial references coordinates, creationDate text not null, annualTurnover FLOAT not null constraint annualTurnover_check check ( annualTurnover > 0 ), fullName text not null, type organization_type not null, officialAddress serial references address, user_id serial references user_account);"),
    UPDATE_ORGANIZATION("update organization set name = ?, creationdate = ?, annualturnover = ?, fullname = ?, type = cast( ? as organization_type ) where user_id = ? and id = ?;"),
    UPDATE_COORDINATES("update coordinates set x = ?, y = ? where user_id = ? and id = ?;"),
    UPDATE_ADDRESS("update address set street = ? where user_id = ? and id = ?;");


    private final String query;

    Queries(String query){
        this.query = query;
    }

    public String getQuery(){
        return query;
    }
}
