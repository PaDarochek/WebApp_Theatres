package entities;

import DAO.SessionDAO;
import DAO.TheatreDAO;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@FilterDef(name = "perfTheatreFilter", parameters = @ParamDef(name = "theatreParam", type = "java.lang.String"))

@Entity
@Table(name = "performances")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(targetEntity = Theatre.class)
    @JoinColumn(name = "theatre", referencedColumnName = "id")
    @FilterJoinTable(name = "perfTheatreFilter", condition = "theatres.name like :theatreParam")
    private Theatre theatre;

    @Column(name = "duration")
    private Long duration;

    public Performance(Long id, String name, Long theatreId, Long duration) {
        this.id = id;
        this.name = name;
        TheatreDAO theatreDAO = new TheatreDAO();
        this.theatre = theatreDAO.getEntityById(theatreId, Theatre.class);
        this.duration = duration;
    }

    public Performance() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        final Performance other = (Performance) object;
        return (this.id.equals(other.id) && this.name.equals(other.name) && this.theatre.equals(other.theatre) &&
                this.duration.equals(other.duration));
    }
}
