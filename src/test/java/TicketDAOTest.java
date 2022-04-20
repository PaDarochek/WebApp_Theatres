import DAO.TicketDAO;
import entities.Ticket;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketDAOTest {
    private TicketDAO ticketDAO = new TicketDAO();

    @Test
    public void ticketGetAllTest() {
        List<Ticket> tickets = ticketDAO.getAll(Ticket.class);
        Assert.assertEquals(9, tickets.size());
    }

    @Test
    public void ticketGetAllLimitTest() {
        List<Ticket> ticketsAll = ticketDAO.getAll(Ticket.class);
        List<Ticket> tickets = ticketDAO.getAll(Ticket.class, 5, 2);
        Assert.assertEquals(2, tickets.size());
        AtomicInteger i = new AtomicInteger(5);
        tickets.forEach(ticket -> {
            Assert.assertEquals(ticket.getId(), ticketsAll.get(i.getAndIncrement()).getId());
        });
    }
}
