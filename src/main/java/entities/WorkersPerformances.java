package entities;

import DAO.PerformanceDAO;
import DAO.SessionDAO;
import DAO.WorkerDAO;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "workers_performances")
public class WorkersPerformances implements Serializable {
    @Id
    @ManyToOne(targetEntity = Performance.class)
    @JoinColumn(name = "performance", referencedColumnName = "id")
    private Performance performance;

    @Id
    @ManyToOne(targetEntity = Worker.class)
    @JoinColumn(name = "worker", referencedColumnName = "id")
    private Worker worker;

    @Column(name = "job")
    private String job;

    @Column(name = "character")
    private String character;

    public WorkersPerformances(Long performanceId, Long workerId, String job, String character) {
        PerformanceDAO performanceDAO = new PerformanceDAO();
        this.performance = performanceDAO.getEntityById(performanceId, Performance.class);
        WorkerDAO workerDAO = new WorkerDAO();
        this.worker = workerDAO.getEntityById(workerId, Worker.class);
        this.job = job;
        this.character = character;
    }

    public WorkersPerformances () {
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
