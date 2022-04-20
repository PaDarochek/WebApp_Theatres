import DAO.PerformanceDAO;
import entities.Performance;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

public class PerformanceDAOTest {
    private PerformanceDAO performanceDAO = new PerformanceDAO();

    @Test
    public void performanceGetByDirectorTest() {
        List<Performance> performances = performanceDAO.getByDirector("Михаил Фоменко");
        Assert.assertEquals(2, performances.size());
        Assert.assertTrue(!(performances.get(0).getName()
                .equals(performances.get(1).getName())));
        performances.forEach(performance -> {
            Assert.assertTrue(performance.getName().equals("Моя бедная крыша") ||
                    performance.getName().equals("Светлый ручей"));
        });
    }

    @Test
    public void performanceGetByActorsTest() {
        List<Performance> performances = performanceDAO.getByActors(List.of("Дарья Шевчук", "Василий Дахненко", "Ольга Смирнова"));
        Assert.assertEquals(5, performances.size());
        List<Integer> ids = List.of(1, 3, 5, 6, 7);
        performances.forEach(performance -> {
            Assert.assertTrue(ids.contains(performance.getId()));
        });
    }

    @Test
    public void performanceGetByDateTest() {
        List<Performance> performances = performanceDAO.getByDate(
                Date.valueOf("2022-04-03"), Date.valueOf("2022-04-07"));
        Assert.assertEquals(2, performances.size());
        List<Integer> ids = List.of(4, 6);
        performances.forEach(performance -> {
            Assert.assertTrue(ids.contains(performance.getId()));
        });
    }

    @Test
    public void performanceGetByTheatreTest() {
        List<Performance> performances = performanceDAO.getByTheatre("%Государственный%");
        Assert.assertEquals(5, performances.size());
        List<Integer> ids = List.of(2, 3, 5);
        performances.forEach(performance -> {
            Assert.assertTrue(ids.contains(performance.getTheatre().getId()));
        });
    }
}
