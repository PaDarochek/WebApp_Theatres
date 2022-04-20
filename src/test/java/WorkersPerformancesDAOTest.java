import DAO.WorkersPerformancesDAO;
import entities.WorkersPerformances;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class WorkersPerformancesDAOTest {
    private WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();

    @Test
    public void workersPerformancesSortTest() {
        List<WorkersPerformances> workersPerformances =
                workersPerformancesDAO.sort(Map.of("job", "asc", "character", "desc"),
                        WorkersPerformances.class);
        Assert.assertEquals(27, workersPerformances.size());
        Assert.assertEquals("actor", workersPerformances.get(0).getJob());
        Assert.assertEquals("Элеонора Австрийская", workersPerformances.get(0).getCharacter());
        Assert.assertEquals("director", workersPerformances.get(workersPerformances.size() - 1).getJob());
        Assert.assertEquals("", workersPerformances.get(workersPerformances.size() - 1).getCharacter());
    }
}
