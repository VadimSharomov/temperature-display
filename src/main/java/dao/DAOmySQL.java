package dao;

import entity.Temperature;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service
public class DAOmySQL implements DAO {
    private final static Logger logger = getLogger(DAOmySQL.class);
    private String table;
    private JdbcTemplate jdbcTemplateObject;

    public DAOmySQL() {}

    @Override
    public void setDataSource(JdbcTemplate jdbcTemplate, String tableName) {
        this.table = tableName;
        this.jdbcTemplateObject = jdbcTemplate;
    }

    @Override
    public List<Temperature> getListTemperaturesInTimeInterval(int start, int end) {
        String SQL = "SELECT * FROM " + table + " WHERE TIMESTAMP BETWEEN  ? AND ?";
        try {
            return new ArrayList<>(jdbcTemplateObject.query(SQL,
                    new Object[]{start, end}, new TempMapper()));
        } catch (DataAccessException e) {
            logger.error("DataAccessException in getListTemperaturesInTimeInterval", e.getMessage());
            return new ArrayList<>();
        }
    }

    // This method was intended only for testing this task
    @Override
    public void create(int time, int temperature) {
        String SQL = "INSERT INTO " + table + " (TIMESTAMP, temperature) VALUES (?, ?)";
        jdbcTemplateObject.update(SQL, time, temperature);
        logger.info("Create record in DB '" + time + " - " + temperature + "'");
    }
}