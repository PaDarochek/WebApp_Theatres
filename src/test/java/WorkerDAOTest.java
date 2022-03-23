import DAO.WorkerDAO;
import entities.Worker;
import org.junit.Assert;
import org.junit.Test;

public class WorkerDAOTest {
    private final WorkerDAO workerDAO = new WorkerDAO();

    @Test
    public void workerUpdateTest() {
        Worker worker = new Worker(15, "Вячеслав Александрович Лопатин");
        worker = workerDAO.update(worker);
        Assert.assertEquals("Вячеслав Александрович Лопатин", worker.getName());
    }
}
