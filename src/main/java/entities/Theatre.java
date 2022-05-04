package entities;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@FilterDef(name = "theatreFilter", parameters = @ParamDef(name = "nameParam", type = "java.lang.String"))
@Filter(name = "theatreFilter", condition = "name like :nameParam")

@Entity
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "ground_floor_rows")
    private Long ground_floor_rows;

    @Column(name = "ground_floor_row_seats")
    private Long ground_floor_row_seats;

    @Column(name = "balcony_rows")
    private Long balcony_rows;

    @Column(name = "balcony_row_seats")
    private Long balcony_row_seats;

    @Column(name = "mezzanine_rows")
    private Long mezzanine_rows;

    @Column(name = "mezzanine_row_seats")
    private Long mezzanine_row_seats;

    public Theatre(Long id, String name, String address, Long ground_floor_rows, Long ground_floor_row_seats,
                   Long balcony_rows, Long balcony_row_seats, Long mezzanine_rows, Long mezzanine_row_seats) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.ground_floor_rows = ground_floor_rows;
        this.ground_floor_row_seats = ground_floor_row_seats;
        this.balcony_rows = balcony_rows;
        this.balcony_row_seats = balcony_row_seats;
        this.mezzanine_rows = mezzanine_rows;
        this.mezzanine_row_seats = mezzanine_row_seats;
    }

    public Theatre() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getGround_floor_rows() {
        return ground_floor_rows;
    }

    public void setGround_floor_rows(Long ground_floor_rows) {
        this.ground_floor_rows = ground_floor_rows;
    }

    public Long getGround_floor_row_seats() {
        return ground_floor_row_seats;
    }

    public void setGround_floor_row_seats(Long ground_floor_row_seats) {
        this.ground_floor_row_seats = ground_floor_row_seats;
    }

    public Long getBalcony_rows() {
        return balcony_rows;
    }

    public void setBalcony_rows(Long balcony_rows) {
        this.balcony_rows = balcony_rows;
    }

    public Long getBalcony_row_seats() {
        return balcony_row_seats;
    }

    public void setBalcony_row_seats(Long balcony_row_seats) {
        this.balcony_row_seats = balcony_row_seats;
    }

    public Long getMezzanine_rows() {
        return mezzanine_rows;
    }

    public void setMezzanine_rows(Long mezzanine_rows) {
        this.mezzanine_rows = mezzanine_rows;
    }

    public Long getMezzanine_row_seats() {
        return mezzanine_row_seats;
    }

    public void setMezzanine_row_seats(Long mezzanine_row_seats) {
        this.mezzanine_row_seats = mezzanine_row_seats;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        final Theatre other = (Theatre) object;
        return (this.id.equals(other.id) && this.name.equals(other.name) && this.address.equals(other.address));
    }
}