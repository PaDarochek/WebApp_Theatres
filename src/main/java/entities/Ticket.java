package entities;

import DAO.SessionDAO;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Session.class)
    @JoinColumn(name = "session", referencedColumnName = "id")
    private Session session;

    @Column(name = "category")
    private String category;

    @Column(name = "row")
    private int row;

    @Column(name = "row_seat")
    private int row_seat;

    public Ticket(Long id, Long sessionId, String category, int row, int row_seat) {
        this.id = id;
        SessionDAO sessionDAO = new SessionDAO();
        this.session = sessionDAO.getEntityById(sessionId, Session.class);
        this.category = category;
        this.row = row;
        this.row_seat = row_seat;
    }

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow_seat() {
        return row_seat;
    }

    public void setRow_seat(int row_seat) {
        this.row_seat = row_seat;
    }
}
