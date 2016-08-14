package dao;

import controller.Temperature;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
public class DAOmySQL implements DAO {
    private final static Logger logger = getLogger(DAOmySQL.class);
    private String table;
    private JdbcTemplate jdbcTemplateObject;

    private DAOmySQL() {
        this.table = "temperature";
    }

    private static class SingleToneHelper {
        private static final DAOmySQL INSTANCE = new DAOmySQL();
    }

    public static DAOmySQL getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Temperature> getTempListBetweenValues(int start, int end) {
        String SQL = "SELECT * FROM " + table + " WHERE TIMESTAMP BETWEEN  ? AND ?";
        try {
            return new ArrayList<>(jdbcTemplateObject.query(SQL,
                    new Object[]{start, end}, new TempMapper()));
        } catch (DataAccessException e) {
            logger.error("DataAccessException in getTempListBetweenValues", e.getMessage());
            return null;
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
