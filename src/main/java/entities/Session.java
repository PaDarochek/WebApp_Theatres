package entities;

import DAO.PerformanceDAO;
import DAO.SessionDAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = Performance.class)
    @JoinColumn(name = "performance", referencedColumnName = "id")
    private Performance performance;

    @Column(name = "date_time")
    private Timestamp date_time;

    @Column(name = "ground_floor_cost")
    private Long ground_floor_cost;

    @Column(name = "balcony_cost")
    private Long balcony_cost;

    @Column(name = "mezzanine_cost")
    private Long mezzanine_cost;

    public Session(Long id, Long performanceId, Timestamp date_time, Long ground_floor_cost, Long balcony_cost,
                   Long mezzanine_cost) {
        this.id = id;
        PerformanceDAO performanceDAO = new PerformanceDAO();
        this.performance = performanceDAO.getEntityById(performanceId, Performance.class);
        this.date_time = date_time;
        this.ground_floor_cost = ground_floor_cost;
        this.balcony_cost = balcony_cost;
        this.mezzanine_cost = mezzanine_cost;
    }

    public Session() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getGround_floor_cost() {
        return ground_floor_cost;
    }

    public void setGround_floor_cost(Long ground_floor_cost) {
        this.ground_floor_cost = ground_floor_cost;
    }

    public Long getBalcony_cost() {
        return balcony_cost;
    }

    public void setBalcony_cost(Long balcony_cost) {
        this.balcony_cost = balcony_cost;
    }

    public Long getMezzanine_cost() {
        return mezzanine_cost;
    }

    public void setMezzanine_cost(Long mezzanine_cost) {
        this.mezzanine_cost = mezzanine_cost;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        final Session other = (Session) object;
        return (this.id.equals(other.id) && this.performance.equals(other.performance) &&
                this.date_time.equals(other.date_time) && this.ground_floor_cost.equals(other.ground_floor_cost) &&
                this.mezzanine_cost.equals(other.mezzanine_cost) && this.balcony_cost.equals(other.balcony_cost));
    }
}
