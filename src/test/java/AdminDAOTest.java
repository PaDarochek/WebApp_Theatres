import DAO.AdminDAO;
import entities.Admin;
import org.junit.Assert;
import org.junit.Test;

public class AdminDAOTest {
    private final AdminDAO adminDAO = new AdminDAO();

    @Test
    public void adminCreateTest() {
        Admin admin = adminDAO.getEntityById(6L, Admin.class);
        if (admin != null) {
            adminDAO.delete(admin);
        }
        admin = new Admin(6L,"admin_6", "qwerty");
        Assert.assertTrue(adminDAO.create(admin));
        admin = adminDAO.getEntityById(admin.getId(), Admin.class);
        Assert.assertNotNull(admin);
        Assert.assertEquals("qwerty", admin.getPassword());
    }

    @Test
    public void adminDeleteTest() {
        Admin admin = adminDAO.getEntityById(6L, Admin.class);
        if (admin == null) {
            admin = new Admin(6L,"admin_6", "qwerty");
            adminDAO.create(admin);
        }
        Assert.assertTrue(adminDAO.delete(admin));
        Assert.assertEquals("admin_6", admin.getLogin());
        admin = adminDAO.getEntityById(6L, Admin.class);
        Assert.assertNull(admin);
    }
}
