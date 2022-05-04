package controllers;

import DAO.*;
import entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PerformanceController {
    private static int firstResult = 0;
    private static boolean lastPage = false;

    private static boolean filter = false;
    private static int firstResultFilter = 0;
    private static boolean lastPageFilter = false;
    private static List<Performance> performancesFilter = new ArrayList<>();
    private static Long perf_id;

    @RequestMapping(value = "/performances", method = RequestMethod.GET)
    public String getPerformances(@CookieValue(value = "login", required = false) String login,
                                  @CookieValue(value = "password", required = false) String password,
                                  Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            firstResult = 0;
            List<Performance> performances = performanceDAO.getAll(Performance.class, firstResult, 3);
            if (performances.size() < 3) {
                lastPage = true;
            }
            model.addAttribute("performances", performances);

            Map<Performance, List<Session>> sessions = new HashMap<>();
            for (Performance perf : performances) {
                SessionDAO sessionDAO = new SessionDAO();
                List<Session> sess = sessionDAO.getAll(Session.class);
                sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                sessions.put(perf, sess);
            }
            model.addAttribute("sessions", sessions);
            return "performances/performances";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get performances");
            return "error";
        }
    }
    // order to getAll
    @RequestMapping(value = "/performances_next", method = RequestMethod.GET)
    public String getNextPerformances(@CookieValue(value = "login", required = false) String login,
                                      @CookieValue(value = "password", required = false) String password,
                                      Model model) {
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            if (filter) {
                if (!lastPageFilter) {
                    firstResultFilter += 3;
                    List<Performance> performances = performancesFilter
                            .subList(firstResultFilter, Math.min(performancesFilter.size(), firstResultFilter + 3));
                    if (performances.size() < 3) {
                        lastPageFilter = true;
                    } else if (performances.size() == 0) {
                        firstResultFilter -= 3;
                        performances = performancesFilter.subList(firstResultFilter,
                                Math.min(performancesFilter.size(), firstResultFilter + 3));
                    }
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                } else {
                    List<Performance> performances = performancesFilter
                            .subList(firstResultFilter, Math.min(performancesFilter.size(), firstResultFilter + 3));
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                }
            } else {
                if (!lastPage) {
                    firstResult += 3;
                    List<Performance> performances = performanceDAO.getAll(Performance.class, firstResult, 3);
                    if (performances.size() < 3) {
                        lastPage = true;
                    } else if (performances.size() == 0) {
                        firstResult -= 3;
                        performances = performanceDAO.getAll(Performance.class, firstResult, 3);
                    }
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                } else {
                    List<Performance> performances = performanceDAO.getAll(Performance.class, firstResult, 3);
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                }
            }
            return "performances/performances";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get next performances");
            return "error";
        }
    }

    @RequestMapping(value = "/performances_prev", method = RequestMethod.GET)
    public String getPrevPerformances(@CookieValue(value = "login", required = false) String login,
                                      @CookieValue(value = "password", required = false) String password,
                                      Model model) {
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            if (filter) {
                if (firstResultFilter - 3 >= 0) {
                    firstResultFilter -= 3;
                    lastPageFilter = false;
                    List<Performance> performances = performancesFilter
                            .subList(firstResultFilter, Math.min(performancesFilter.size(), firstResultFilter + 3));
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                } else {
                    List<Performance> performances = performancesFilter
                            .subList(firstResultFilter, Math.min(performancesFilter.size(), firstResultFilter + 3));
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                }
            } else {
                if (firstResult - 3 >= 0) {
                    firstResult -= 3;
                    lastPage = false;
                    List<Performance> performances = performanceDAO.getAll(Performance.class, firstResult, 3);
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                } else {
                    List<Performance> performances = performanceDAO.getAll(Performance.class, firstResult, 3);
                    model.addAttribute("performances", performances);

                    Map<Performance, List<Session>> sessions = new HashMap<>();
                    for (Performance perf : performances) {
                        SessionDAO sessionDAO = new SessionDAO();
                        List<Session> sess = sessionDAO.getAll(Session.class);
                        sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                        sessions.put(perf, sess);
                    }
                    model.addAttribute("sessions", sessions);
                }
            }
            return "performances/performances";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get previous performances");
            return "error";
        }
    }

    @RequestMapping(value = "/performances_filter", method = RequestMethod.GET)
    public String filterPerformances(@RequestParam(name = "theatre", required = false) String theatre,
                                     @RequestParam(name = "start_date", required = false) String start_date,
                                     @RequestParam(name = "end_date", required = false) String end_date,
                                     @RequestParam(name = "director", required = false) String director,
                                     @RequestParam(name = "actors", required = false) String actors,
                                     @CookieValue(value = "login", required = false) String login,
                                     @CookieValue(value = "password", required = false) String password,
                                     Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            filter = true;
            firstResultFilter = 0;
            PerformanceDAO performanceDAO = new PerformanceDAO();
            List<Performance> performances_all = performanceDAO.getAll(Performance.class);
            List<Performance> performances_theatre;
            if (!theatre.isEmpty()) {
                performances_theatre = performanceDAO.getByTheatre(theatre);
            } else {
                performances_theatre = performances_all;
            }
            List<Performance> performances_date_time;
            if (!start_date.isEmpty()) {
                if (end_date.isEmpty()) {
                    end_date = start_date;
                }
                performances_date_time = performanceDAO.getByDate(Date.valueOf(start_date), Date.valueOf(end_date));
            } else {
                performances_date_time = performances_all;
            }
            List<Performance> performances_director;
            if (!director.isEmpty()) {
                performances_director = performanceDAO.getByDirector(director);
            } else {
                performances_director = performances_all;
            }
            List<Performance> performances_actors;
            if (!actors.isEmpty()) {
                performances_actors = performanceDAO.getByActors
                        (Arrays.stream(actors.split(",")).collect(Collectors.toList()));
            } else {
                performances_actors = performances_all;
            }

            if (!performances_theatre.isEmpty() && !performances_date_time.isEmpty() && !performances_director.isEmpty() &&
                    !performances_actors.isEmpty()) {
                performancesFilter = performances_theatre.stream().filter(performances_date_time::contains).collect(Collectors.toList());
                performancesFilter = performancesFilter.stream().filter(performances_director::contains).collect(Collectors.toList());
                performancesFilter = performancesFilter.stream().filter(performances_actors::contains).collect(Collectors.toList());
            }
            if (performancesFilter.size() < 3) {
                lastPageFilter = true;
            }
            model.addAttribute("performances", performancesFilter.subList(firstResultFilter, Math.min(performancesFilter.size(),
                    firstResultFilter + 3)));

            Map<Performance, List<Session>> sessions = new HashMap<>();
            for (Performance perf : performancesFilter.subList(firstResultFilter, Math.min(performancesFilter.size(),
                    firstResultFilter + 3))) {
                SessionDAO sessionDAO = new SessionDAO();
                List<Session> sess = sessionDAO.getAll(Session.class);
                sess = sess.stream().filter(s -> s.getPerformance().equals(perf)).collect(Collectors.toList());
                sessions.put(perf, sess);
            }
            model.addAttribute("sessions", sessions);
            return "performances/performances";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get filtered performances");
            return "error";
        }
    }

    @RequestMapping(value = "/performance_page", method = RequestMethod.GET)
    public String performancePage(@RequestParam(name = "id", required = true) Long id,
                                  @CookieValue(value = "login", required = false) String login,
                                  @CookieValue(value = "password", required = false) String password,
                                  Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            Performance performance = performanceDAO.getEntityById(id, Performance.class);
            model.addAttribute("performance", performance);

            WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
            List<WorkersPerformances> workersPerformances = workersPerformancesDAO.getAll(WorkersPerformances.class);
            workersPerformances = workersPerformances.stream().filter(wp ->
                            wp.getPerformance().equals(performance))
                    .collect(Collectors.toList());

            List<WorkersPerformances> directorsList = workersPerformances.stream().filter(wp -> wp.getJob().equals("director")).collect(Collectors.toList());
            Set<String> directors = directorsList.stream().map(wp -> wp.getWorker().getName()).collect(Collectors.toSet());
            model.addAttribute("directors", String.join((CharSequence) ", ", directors));

            List<WorkersPerformances> actorsList = workersPerformances.stream().filter(wp -> wp.getJob().equals("actor")).collect(Collectors.toList());
            Set<String> actors = actorsList.stream().map(wp -> wp.getWorker().getName()).collect(Collectors.toSet());
            model.addAttribute("actors", String.join((CharSequence) ", ", actors));

            SessionDAO sessionDAO = new SessionDAO();
            List<Session> sessions = sessionDAO.sort(Map.of("date_time", "asc"), Session.class)
                    .stream().limit(10).filter(session -> {
                        return session.getDate_time()
                                .after(new Timestamp(System.currentTimeMillis()))
                                && session.getPerformance().equals(performance);
                    }).collect(Collectors.toList());
            model.addAttribute("sessions", sessions);
            return "performances/performance_page";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get performance");
            return "error";
        }
    }

    @RequestMapping(value = "/delete_performance", method = RequestMethod.GET)
    public String deletePerformance(@RequestParam(name = "perf_id", required = true) Long perf_id,
                                    @CookieValue(value = "login", required = true) String login,
                                    @CookieValue(value = "password", required = true) String password,
                                    Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            performanceDAO.delete(performance);
            MainPageController mainPageController = new MainPageController();
            return mainPageController.getMainPage(login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot delete performances");
            return "error";
        }
    }

    @RequestMapping(value = "/update_performance", method = RequestMethod.GET)
    public String updatePerformance(@RequestParam(name = "perf_id", required = true) Long perf_id,
                                    @CookieValue(value = "login", required = true) String login,
                                    @CookieValue(value = "password", required = true) String password,
                                    Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            model.addAttribute("performance", performance);
            this.perf_id = perf_id;

            WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
            List<WorkersPerformances> workersPerformances = workersPerformancesDAO.getAll(WorkersPerformances.class);
            workersPerformances = workersPerformances.stream().filter(wp ->
                            wp.getPerformance().equals(performance))
                    .collect(Collectors.toList());

            List<WorkersPerformances> directorsList = workersPerformances.stream().filter(wp -> wp.getJob().equals("director")).collect(Collectors.toList());
            List<String> directors = directorsList.stream().map(wp -> wp.getWorker().getName()).collect(Collectors.toList());
            if (directors.size() != 1) {
                return "error";
            }
            model.addAttribute("director", directors.get(0));

            List<WorkersPerformances> actorsList = workersPerformances.stream().filter(wp -> wp.getJob().equals("actor")).collect(Collectors.toList());
            Map<Worker, String> actors = actorsList.stream().collect(Collectors.toMap(WorkersPerformances::getWorker, WorkersPerformances::getCharacter));
            model.addAttribute("actors", actors);

            SessionDAO sessionDAO = new SessionDAO();
            List<Session> sessions = sessionDAO.sort(Map.of("date_time", "asc"), Session.class)
                    .stream().filter(session -> {
                        return session.getPerformance().equals(performance);
                    })
                    .collect(Collectors.toList());
            model.addAttribute("sessions", sessions);
            return "performances/update_performance";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get performance page for update");
            return "error";
        }
    }

    @RequestMapping(value = "/update_performance_save", method = RequestMethod.GET)
    public String updatePerformanceSave(@RequestParam(name = "name", required = false) String name,
                                        @RequestParam(name = "duration", required = false) String duration,
                                        @RequestParam(name = "actor", required = false) String actor,
                                        @RequestParam(name = "actor_role", required = false) String actor_role,
                                        @RequestParam(name = "del_actor", required = false) Long del_actor,
                                        @RequestParam(name = "director", required = false) String director,
                                        @RequestParam(name = "del_session", required = false) Long del_session,
                                        @RequestParam(name = "session_date_time", required = false) String session_date_time,
                                        @RequestParam(name = "session_gr_fl", required = false) Long session_gr_fl,
                                        @RequestParam(name = "session_mez", required = false) Long session_mez,
                                        @RequestParam(name = "session_bal", required = false) Long session_bal,
                                        @CookieValue(value = "login", required = true) String login,
                                        @CookieValue(value = "password", required = true) String password,
                                        Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);

            if (name != null && !name.isEmpty()) {
                performance.setName(name);
                performance = performanceDAO.update(performance);
            }
            if (duration != null) {
                java.util.Date date = new SimpleDateFormat("HH:mm").parse(duration);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                Long time = calendar.get(Calendar.HOUR) * 60L + calendar.get(Calendar.MINUTE);
                performance.setDuration(time);
                performance = performanceDAO.update(performance);
            }

            if (actor != null && !actor.isEmpty()) {
                WorkerDAO workerDAO = new WorkerDAO();
                List<Worker> workers = workerDAO.getAll(Worker.class).stream()
                        .filter(w -> w.getName().equals(actor)).collect(Collectors.toList());
                Worker worker;
                if (workers.size() == 1) {
                    worker = workers.get(0);
                }
                else {
                    Long id = workerDAO.getCount(Worker.class) + 1;
                    worker = new Worker(id, actor);
                    workerDAO.create(worker);
                }
                String role = actor_role;
                if (actor_role == null) {
                    role = "";
                }
                WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
                workersPerformancesDAO.create(new WorkersPerformances(perf_id, worker.getId(), "actor", role));
            }

            if (del_actor != null) {
                WorkerDAO workerDAO = new WorkerDAO();
                Worker worker = workerDAO.getEntityById(del_actor, Worker.class);
                WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
                Performance finalPerformance = performance;
                List<WorkersPerformances> workersPerformances = workersPerformancesDAO.getAll(WorkersPerformances.class)
                        .stream().filter(wp -> wp.getPerformance().equals(finalPerformance) && wp.getWorker().equals(worker)
                        && wp.getJob().equals("actor")).collect(Collectors.toList());
                workersPerformances.forEach(workersPerformancesDAO::delete);
            }

            if (director != null && !director.isEmpty()) {
                WorkerDAO workerDAO = new WorkerDAO();
                List<Worker> workers = workerDAO.getAll(Worker.class).stream()
                        .filter(w -> w.getName().equals(director)).collect(Collectors.toList());
                Worker worker;
                if (workers.size() == 1) {
                    worker = workers.get(0);
                }
                else {
                    Long id = workerDAO.getCount(Worker.class) + 1;
                    worker = new Worker(id, director);
                    workerDAO.create(worker);
                }
                WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
                Performance finalPerformance = performance;
                List<WorkersPerformances> workersPerformances = workersPerformancesDAO.getAll(WorkersPerformances.class)
                        .stream().filter(wp -> wp.getPerformance().equals(finalPerformance)
                                && wp.getJob().equals("director")).collect(Collectors.toList());
                workersPerformances.forEach(workersPerformancesDAO::delete);
                workersPerformancesDAO.create(new WorkersPerformances(perf_id, worker.getId(), "director", ""));
            }

            if (del_session != null) {
                SessionDAO sessionDAO = new SessionDAO();
                Session session = sessionDAO.getEntityById(del_session, Session.class);
                sessionDAO.delete(session);
            }

            if (session_date_time != null && !session_date_time.isEmpty()) {
                SessionDAO sessionDAO = new SessionDAO();
                Long id = sessionDAO.getCount(Session.class) + 1;
                Timestamp date_time = Timestamp.valueOf(session_date_time);
                Session session = new Session(id, perf_id, new Timestamp(date_time.getTime()), session_gr_fl,
                        session_bal, session_mez);
                sessionDAO.create(session);
            }

            return updatePerformance(perf_id, login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot update performance");
            return "error";
        }
    }

    @RequestMapping(value = "/add_performance", method = RequestMethod.GET)
    public String addPerformance(@CookieValue(value = "login", required = true) String login,
                                 @CookieValue(value = "password", required = true) String password,
                                 Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }
        return "performances/add_performance";
    }

    @RequestMapping(value = "/add_performance_save", method = RequestMethod.GET)
    public String addPerformanceSave(@RequestParam(name = "name", required = true) String name,
                                     @RequestParam(name = "theatre_name", required = true) String theatre_name,
                                     @RequestParam(name = "duration", required = true) String duration,
                                     @RequestParam(name = "director", required = true) String director,
                                     @CookieValue(value = "login", required = true) String login,
                                     @CookieValue(value = "password", required = true) String password,
                                     Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            PerformanceDAO performanceDAO = new PerformanceDAO();
            Long id = performanceDAO.getCount(Performance.class) + 1;
            Performance performance = new Performance();
            performance.setId(id);

            if (!name.isEmpty()) {
                performance.setName(name);
            }

            TheatreDAO theatreDAO = new TheatreDAO();
            List<Theatre> theatres = theatreDAO.filter(Map.of("theatreFilter", List.of(theatre_name)), Theatre.class);
            if (theatres.size() != 1) {
                return "error";
            }
            Theatre theatre = theatres.get(0);
            performance.setTheatre(theatre);

            java.util.Date date = new SimpleDateFormat("HH:mm").parse(duration);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            Long time = calendar.get(Calendar.HOUR) * 60L + calendar.get(Calendar.MINUTE);
            performance.setDuration(time);

            performanceDAO.create(performance);

            if (!director.isEmpty()) {
                WorkerDAO workerDAO = new WorkerDAO();
                List<Worker> workers = workerDAO.getAll(Worker.class).stream()
                        .filter(w -> w.getName().equals(director)).collect(Collectors.toList());
                Worker worker;
                if (workers.size() == 1) {
                    worker = workers.get(0);
                }
                else {
                    Long worker_id = workerDAO.getCount(Worker.class) + 1;
                    worker = new Worker(worker_id, director);
                    workerDAO.create(worker);
                }
                WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
                workersPerformancesDAO.create(new WorkersPerformances(performance.getId(), worker.getId(), "director", ""));
            }
            perf_id = performance.getId();
            return updatePerformance(perf_id, login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot add performance");
            return "error";
        }
    }
}
