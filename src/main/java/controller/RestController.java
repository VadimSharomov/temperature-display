package controller;

import dao.DAO;
import entity.Temperature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Controller
public class RestController {
    private final static Logger logger = getLogger(RestController.class);

    @Autowired
    private DAO dao;

    @Component
    public class MyBean {
        @Autowired
        public MyBean(ApplicationArguments args) {
            String pathToConfigFile = "src/main/resources/application.properties";
            try {
                InputStream input = new FileInputStream(pathToConfigFile);
                Properties properties = new Properties();
                properties.load(input);
                logger.info("*** Config file has read '" + pathToConfigFile + "'");
                input.close();

                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setUrl(properties.getProperty("spring.datasource.url"));//host database
                dataSource.setUsername(properties.getProperty("spring.datasource.username"));//userName
                dataSource.setPassword(properties.getProperty("spring.datasource.password"));//password
                dao.setDataSource(new JdbcTemplate(dataSource), properties.getProperty("tableName"));

                logger.info("*** Database has initialised");
            } catch (IOException e) {
                logger.error("File properties not found in this path: '" + pathToConfigFile + "'", e.getMessage());
                System.exit(1);
            }
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
        int start = 0;
        int finish = 86400;//the number of seconds in 24 hours
        for (int i = start; i < finish; i += 10) {
            // calculating a random temperature from -60 to 60 degrees
            // and storage in the database at intervals of 10 seconds
            dao.create(i, (int) (61 * Math.random() * (Math.random() > 0.4999 ? 1 : -1)));
            System.out.println(finish - i);
        }
        return new Temperature(0, 0);
    }

    private Temperature getAverageValueLastHour() {
        Calendar cal = GregorianCalendar.getInstance();

        //get the current time in seconds
        int nowTimeInSec = (cal.get(Calendar.HOUR_OF_DAY) * 3600) + (cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND);

        //get list of values temperature in the last hour
        List<Temperature> tempList = dao.getTempListBetweenValues(nowTimeInSec - 3600, nowTimeInSec);

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
