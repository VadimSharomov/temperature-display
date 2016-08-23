package dao;

import entity.Temperature;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
class TempMapper implements RowMapper<Temperature> {
    private final static Logger logger = getLogger(TempMapper.class);

    @Override
    public Temperature mapRow(ResultSet resultSet, int i) {
        try {
            return new Temperature(resultSet.getInt("timestamp"), resultSet.getInt("temperature"));
        } catch (SQLException e) {
            logger.error("SQLException in mapRow", e.getMessage());
        }
        return null;
    }
}
