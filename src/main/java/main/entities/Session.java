package main.entities;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Performance.class)
    @JoinColumn(name = "performance", referencedColumnName = "id")
    private Performance performance;

    @Column(name = "date_time")
    private Timestamp date_time;

    @Column(name = "ground_floor_cost")
    private int ground_floor_cost;

    @Column(name = "balcony_cost")
    private int balcony_cost;

    @Column(name = "mezzanine_cost")
    private int mezzanine_cost;

    @OneToMany(targetEntity = Ticket.class, mappedBy = "session")
    private List<Ticket> tickets = new ArrayList<>();

    public Session(int id, int performanceId, Timestamp date_time, int ground_floor_cost, int balcony_cost, int mezzanine_cost) {
        this.id = id;
        this.performance.setId(performanceId);
        this.ground_floor_cost = ground_floor_cost;
        this.balcony_cost = balcony_cost;
        this.mezzanine_cost = mezzanine_cost;
    }

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Timestamp getDate_time() {
        return date_time;
    }

    public void setDate_time(Timestamp date_time) {
        this.date_time = date_time;
    }

    public int getGround_floor_cost() {
        return ground_floor_cost;
    }

    public void setGround_floor_cost(int ground_floor_cost) {
        this.ground_floor_cost = ground_floor_cost;
    }

    public int getBalcony_cost() {
        return balcony_cost;
    }

    public void setBalcony_cost(int balcony_cost) {
        this.balcony_cost = balcony_cost;
    }

    public int getMezzanine_cost() {
        return mezzanine_cost;
    }

    public void setMezzanine_cost(int mezzanine_cost) {
        this.mezzanine_cost = mezzanine_cost;
    }
}
