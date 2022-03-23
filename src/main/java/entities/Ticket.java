package entities;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Session.class)
    @JoinColumn(name = "session", referencedColumnName = "id")
    private Session session;

    @Column(name = "category")
    private String category;

    @Column(name = "row")
    private int row;

    @Column(name = "row_seat")
    private int row_seat;

    public Ticket(int id, int sessionId, String category, int row, int row_seat) {
        this.id = id;
        this.session.setId(sessionId);
        this.category = category;
        this.row = row;
        this.row_seat = row_seat;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
