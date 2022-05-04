package controllers;

import DAO.*;
import com.google.common.collect.Lists;
import entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TheatreController {
    private static int firstResult = 0;
    private static boolean lastPage = false;

    private static boolean filter = false;
    private static int firstResultFilter = 0;
    private static boolean lastPageFilter = false;

    private static List<Theatre> theatresFilter = new ArrayList<>();
    private static Long theatre_id;

    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getTheatres(@CookieValue(value = "login", required = false) String login,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            firstResult = 0;
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
            if (theatres.size() < 3) {
                lastPage = true;
            }
            model.addAttribute("theatres", theatres);
            return "theatres/theatres";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get theatres");
            return "error";
        }
    }

    @RequestMapping(value = "/theatres_next", method = RequestMethod.GET)
    public String getNextTheatres(@CookieValue(value = "login", required = false) String login,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            if (filter) {
                if (!lastPageFilter) {
                    firstResultFilter += 3;
                    List<Theatre> nextTheatres = theatresFilter.subList(firstResultFilter, Math.min(theatresFilter.size(),
                            firstResultFilter + 3));
                    if (nextTheatres.size() < 3) {
                        lastPageFilter = true;
                    } else if (nextTheatres.size() == 0) {
                        firstResultFilter -= 3;
                        nextTheatres = theatresFilter.subList(firstResultFilter, Math.min(theatresFilter.size(),
                                firstResultFilter + 3));
                    }
                    model.addAttribute("theatres", nextTheatres);
                } else {
                    List<Theatre> nextTheatres = theatresFilter.subList(firstResultFilter, theatresFilter.size());
                    model.addAttribute("theatres", nextTheatres);
                }
            } else {
                if (!lastPage) {
                    firstResult += 3;
                    List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
                    if (theatres.size() < 3) {
                        lastPage = true;
                    } else if (theatres.size() == 0) {
                        firstResult -= 3;
                        theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
                    }
                    model.addAttribute("theatres", theatres);
                } else {
                    List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
                    model.addAttribute("theatres", theatres);
                }
            }
            return "theatres/theatres";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get next theatres");
            return "error";
        }
    }

    @RequestMapping(value = "/theatres_prev", method = RequestMethod.GET)
    public String getPrevTheatres(@CookieValue(value = "login", required = false) String login,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            if (filter) {
                if (firstResultFilter - 3 >= 0) {
                    firstResultFilter -= 3;
                    lastPageFilter = false;
                    List<Theatre> nextTheatres = theatresFilter.subList(firstResultFilter, Math.min(theatresFilter.size(),
                            firstResultFilter + 3));
                    model.addAttribute("theatres", nextTheatres);
                } else {
                    List<Theatre> nextTheatres = theatresFilter.subList(firstResultFilter, Math.min(theatresFilter.size(),
                            firstResultFilter + 3));
                    model.addAttribute("theatres", nextTheatres);
                }
            } else {
                if (firstResult - 3 >= 0) {
                    firstResult -= 3;
                    lastPage = false;
                    List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
                    model.addAttribute("theatres", theatres);
                } else {
                    List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 3);
                    model.addAttribute("theatres", theatres);
                }
            }
            return "theatres/theatres";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get previous theatres");
            return "error";
        }
    }

    @RequestMapping(value = "/theatres_filter", method = RequestMethod.GET)
    public String filterTheatres(@RequestParam(name = "name", required = true) String name,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            theatresFilter = theatreDAO.filter(Map.of("theatreFilter",
                    Lists.newArrayList("%" + name + "%")), Theatre.class);
            if (theatresFilter.size() < 3) {
                lastPageFilter = true;
            }
            model.addAttribute("theatres", theatresFilter.subList(firstResultFilter, Math.min(theatresFilter.size(),
                    firstResultFilter + 3)));
            return "theatres/theatres";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get filtered theatres");
            return "error";
        }
    }

    @RequestMapping(value = "/theatre_page", method = RequestMethod.GET)
    public String theatrePage(@RequestParam(name = "id", required = true) Long id,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            Theatre theatre = theatreDAO.getEntityById(id, Theatre.class);
            model.addAttribute("theatre", theatre);

            WorkersPerformancesDAO workersPerformancesDAO = new WorkersPerformancesDAO();
            List<WorkersPerformances> workersPerformances = workersPerformancesDAO.getAll(WorkersPerformances.class);
            workersPerformances = workersPerformances.stream().filter(wp -> wp.getPerformance().getTheatre().equals(theatre))
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
                                && session.getPerformance().getTheatre().equals(theatre);
                    }).collect(Collectors.toList());
            model.addAttribute("sessions", sessions);
            return "theatres/theatre_page";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get theatre page");
            return "error";
        }
    }

    @RequestMapping(value = "/delete_theatre", method = RequestMethod.GET)
    public String deleteTheatre(@RequestParam(name = "th_id", required = true) Long th_id,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            Theatre theatre = theatreDAO.getEntityById(th_id, Theatre.class);
            theatreDAO.delete(theatre);
            MainPageController mainPageController = new MainPageController();
            return mainPageController.getMainPage(login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot delete theatre");
            return "error";
        }
    }

    @RequestMapping(value = "/update_theatre", method = RequestMethod.GET)
    public String updateTheatre(@RequestParam(name = "theatre_id", required = true) Long theatre_id,
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
            TheatreDAO theatreDAO = new TheatreDAO();
            Theatre theatre = theatreDAO.getEntityById(theatre_id, Theatre.class);
            model.addAttribute("theatre", theatre);
            this.theatre_id = theatre_id;
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get theatre for update");
            return "error";
        }

        return "theatres/update_theatre";
    }

    @RequestMapping(value = "/update_theatre_save", method = RequestMethod.GET)
    public String updateTheatreSave(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "address", required = false) String address,
                                    @RequestParam(name = "gr_fl_rows", required = false) Long gr_fl_rows,
                                    @RequestParam(name = "mez_rows", required = false) Long mez_rows,
                                    @RequestParam(name = "bal_rows", required = false) Long bal_rows,
                                    @RequestParam(name = "gr_fl_seats", required = false) Long gr_fl_row_seats,
                                    @RequestParam(name = "mez_seats", required = false) Long mez_row_seats,
                                    @RequestParam(name = "bal_seats", required = false) Long bal_row_seats,
                                    @CookieValue(value = "login", required = true) String login,
                                    @CookieValue(value = "password", required = true) String password,
                                    Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            TheatreDAO theatreDAO = new TheatreDAO();
            Theatre theatre = theatreDAO.getEntityById(theatre_id, Theatre.class);

            if (name != null && !name.isEmpty()) {
                theatre.setName(name);
                theatre = theatreDAO.update(theatre);
            }

            if (address != null && !address.isEmpty()) {
                theatre.setAddress(address);
                theatre = theatreDAO.update(theatre);
            }

            if (gr_fl_rows != null) {
                theatre.setGround_floor_rows(gr_fl_rows);
                theatre = theatreDAO.update(theatre);
            }
            if (gr_fl_row_seats != null) {
                theatre.setGround_floor_row_seats(gr_fl_row_seats);
                theatre = theatreDAO.update(theatre);
            }

            if (mez_rows != null) {
                theatre.setMezzanine_rows(mez_rows);
                theatre = theatreDAO.update(theatre);
            }
            if (mez_row_seats != null) {
                theatre.setMezzanine_row_seats(mez_row_seats);
                theatre = theatreDAO.update(theatre);
            }

            if (bal_rows != null) {
                theatre.setBalcony_rows(bal_rows);
                theatre = theatreDAO.update(theatre);
            }
            if (bal_row_seats != null) {
                theatre.setBalcony_row_seats(bal_row_seats);
                theatre = theatreDAO.update(theatre);
            }

            return updateTheatre(theatre_id, login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot update theatre");
            return "error";
        }
    }

    @RequestMapping(value = "/add_theatre", method = RequestMethod.GET)
    public String addTheatre(@CookieValue(value = "login", required = true) String login,
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
        return "theatres/add_theatre";
    }

    @RequestMapping(value = "/add_theatre_save", method = RequestMethod.GET)
    public String addPerformanceSave(@RequestParam(name = "name", required = true) String name,
                                     @RequestParam(name = "address", required = true) String address,
                                     @RequestParam(name = "gr_fl_rows", required = true) Long gr_fl_rows,
                                     @RequestParam(name = "mez_rows", required = true) Long mez_rows,
                                     @RequestParam(name = "bal_rows", required = true) Long bal_rows,
                                     @RequestParam(name = "gr_fl_seats", required = true) Long gr_fl_row_seats,
                                     @RequestParam(name = "mez_seats", required = true) Long mez_row_seats,
                                     @RequestParam(name = "bal_seats", required = true) Long bal_row_seats,
                                     @CookieValue(value = "login", required = true) String login,
                                     @CookieValue(value = "password", required = true) String password,
                                     Model model) {
        filter = false;
        firstResultFilter = 0;
        lastPageFilter = false;
        firstResult = 0;
        lastPage = false;
        try {
            TheatreDAO theatreDAO = new TheatreDAO();
            Long id = theatreDAO.getCount(Theatre.class) + 1;
            Theatre theatre = new Theatre();

            if (name == null || address == null || gr_fl_rows == null || gr_fl_row_seats == null
                    || mez_rows == null || mez_row_seats == null || bal_rows == null
                    || bal_row_seats == null || name.isEmpty() || address.isEmpty()) {
                return "error";
            }

            theatre.setName(name);

            theatre.setAddress(address);

            theatre.setGround_floor_rows(gr_fl_rows);
            theatre.setGround_floor_row_seats(gr_fl_row_seats);

            theatre.setMezzanine_rows(mez_rows);
            theatre.setMezzanine_row_seats(mez_row_seats);

            theatre.setBalcony_rows(bal_rows);
            theatre.setBalcony_row_seats(bal_row_seats);

            theatreDAO.create(theatre);
            this.theatre_id = theatre.getId();

            return updateTheatre(theatre_id, login, password, model);
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot add theatre");
            return "error";
        }
    }
}
