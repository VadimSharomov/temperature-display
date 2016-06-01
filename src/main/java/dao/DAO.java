package dao;

import controller.Temperature;

import java.util.List;

/**
 * Created by Vadim on 01.06.2016.
 * 
 */
public interface DAO {

    List<Temperature> getTempListBetweenValues(int start, int end);

    void create(int time, int temperature);
}
