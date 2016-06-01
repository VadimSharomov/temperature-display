package dao;

import controller.Temperature;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vadim on 26.05.2016.
 * 
 */
class TempMapper implements RowMapper<Temperature> {
    @Override
    public Temperature mapRow(ResultSet resultSet, int i) {
        try {
            return new Temperature(resultSet.getInt("timestamp"), resultSet.getInt("temperature"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
