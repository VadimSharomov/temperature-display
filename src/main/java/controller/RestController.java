package controller;

import dao.DAO;
import dao.DAOmySQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class RestController {

    @Component
    public class MyBean {
        @SuppressWarnings("SpringJavaAutowiringInspection")
        @Autowired
        public MyBean(ApplicationArguments args) {
        }

        @Bean
        public DriverManagerDataSource getMySQLDriverManagerDatasource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/temperature?autoReconnect=true&useSSL=false&serverTimezone=UTC");//host database
            dataSource.setUsername("user");//userName
            dataSource.setPassword("1111");//password
            DAOmySQL.getInstance().setDataSource(dataSource);
            return dataSource;
        }
    }

    @RequestMapping("/temperature")
    public
    @ResponseBody
    Temperature getTemperature(@RequestParam(required = false) String name) {
        return getAverageValueLastHour();
    }

    // filling data base random temperature every 10 second
    // This method was intended only for testing this task
    @RequestMapping("/filling")
    public
    @ResponseBody
    Temperature filling(@RequestParam(required = false) String name) {
        DAO db = DAOmySQL.getInstance();
        int start = 0;
        int finish = 86400;//the number of seconds in 24 hours
        for (int i = start; i < finish; i += 10) {
            // calculating a random temperature from -60 to 60 degrees
            // and storage in the database at intervals of 10 seconds
            db.create(i, (int) (61 * Math.random() * (Math.random() > 0.4999 ? 1 : -1)));
            System.out.println(finish - i);
        }
        return new Temperature(0, 0);
    }

    private Temperature getAverageValueLastHour() {
        Calendar cal = GregorianCalendar.getInstance();

        //get the current time in seconds
        int nowTimeInSec = (cal.get(Calendar.HOUR_OF_DAY) * 3600) + (cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND);

        //get list of values temperature in the last hour
        List<Temperature> tempList = DAOmySQL.getInstance().getTempListBetweenValues(nowTimeInSec - 3600, nowTimeInSec);

        //calculate the average temperature
        int averageTemperature = 0;
        for (Temperature aTempList : tempList) {
            averageTemperature += aTempList.getValue();
        }
        if (tempList.size() != 0) {
            averageTemperature = averageTemperature / tempList.size();
        }

        return new Temperature(nowTimeInSec, averageTemperature);
    }

}
