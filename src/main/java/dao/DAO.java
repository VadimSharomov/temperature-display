package dao;

import entity.Temperature;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface DAO {

    void setDataSource(JdbcTemplate jdbcTemplate, String tableName);

    List<Temperature> getListTemperaturesInTimeInterval(int start, int end);

    void create(int time, int temperature);
}
