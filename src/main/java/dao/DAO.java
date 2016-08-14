package dao;

import controller.Temperature;

import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface DAO {

    List<Temperature> getTempListBetweenValues(int start, int end);

    void create(int time, int temperature);
}
