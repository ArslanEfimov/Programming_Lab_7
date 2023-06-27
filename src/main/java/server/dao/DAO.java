package server.dao;

import java.sql.SQLException;
import java.util.Vector;

public interface DAO<T> {
    int ERROR = -1;

    Vector<T> read() throws SQLException;
    long create(T t) throws SQLException;
}
