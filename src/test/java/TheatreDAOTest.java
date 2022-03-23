import DAO.TheatreDAO;
import com.beust.jcommander.internal.Lists;
import entities.Theatre;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TheatreDAOTest {
    private TheatreDAO theatreDAO = new TheatreDAO();

    @Test
    public void getTheatreByIdTest() {
        Integer id = Integer.valueOf(3);
        Theatre theatre = theatreDAO.getEntityById(id, Theatre.class);
        Assert.assertEquals("Государственный академический Малый театр", theatre.getName());
    }

    @Test
    public void filterTheatreTest() {
        List<Theatre> theatres = theatreDAO.filter(Map.of("theatreFilter",
                Lists.newArrayList("%Московский%")), Theatre.class);
        Assert.assertEquals(3, theatres.size());
        List<Integer> ids = List.of(1, 4, 5);
        theatres.forEach(theatre -> {
            Assert.assertTrue(ids.contains(theatre.getId()));
        });
    }

    @Test
    public void filterTheatreLimitTest() {
        List<Theatre> theatres = theatreDAO.filter(Map.of("theatreFilter",
                Lists.newArrayList("%Московский%")), Theatre.class, 2, 1);
        Assert.assertEquals(1, theatres.size());
        Assert.assertTrue(theatres.get(0).getId() == 5);
    }
}
