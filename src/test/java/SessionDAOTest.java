import DAO.SessionDAO;
import entities.Session;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SessionDAOTest {
    private SessionDAO sessionDAO = new SessionDAO();

    @Test
    public void sessionGetCountTest() {
        Long sessionsNum = sessionDAO.getCount(Session.class);
        Assert.assertEquals(Long.valueOf(9), sessionsNum);
    }
}
