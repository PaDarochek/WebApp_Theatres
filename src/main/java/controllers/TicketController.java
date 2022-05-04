package controllers;

import DAO.*;
import entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TicketController {
    private static List<Ticket> tickets;
    private static Map<Integer, Set<Integer>> free_seats = new HashMap<>();
    private static Map<Integer, Set<Integer>> bought_seats = new HashMap<>();
    private static Long perf_id;
    private static Long session_id;
    private static String category;
    private static Integer row;

    @RequestMapping(value = "/choose_tickets", method = RequestMethod.GET)
    public String chooseTickets(@RequestParam(name = "perf_id", required = true) Long perf_id,
                                @RequestParam(name = "session_id", required = true) Long session_id,
                                @CookieValue(value = "login", required = false) String login,
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
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            model.addAttribute("performance", performance);
            this.perf_id = perf_id;

            SessionDAO sessionDAO = new SessionDAO();
            Session session = sessionDAO.getEntityById(session_id, Session.class);
            model.addAttribute("sess", session);
            this.session_id = session_id;

            return "tickets/choose_tickets";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot choose tickets");
            return "error";
        }
    }

    @RequestMapping(value = "/choose_category", method = RequestMethod.GET)
    public String chooseCategory(@RequestParam(name = "category", required = true) String category,
                                 @CookieValue(value = "login", required = false) String login,
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
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            model.addAttribute("performance", performance);

            SessionDAO sessionDAO = new SessionDAO();
            Session session = sessionDAO.getEntityById(session_id, Session.class);
            model.addAttribute("sess", session);

            this.category = category;
            Theatre theatre = performance.getTheatre();
            TicketDAO ticketDAO = new TicketDAO();
            tickets = ticketDAO.getAll(Ticket.class);
            tickets = tickets.stream().filter(t -> t.getSession().equals(session)).collect(Collectors.toList());

            if (category.equals("ground_floor")) {
                free_seats = new HashMap<>();
                bought_seats = new HashMap<>();
                for (int row : tickets.stream().filter(t -> t.getCategory().equals("ground_floor")).map(t -> t.getRow()).collect(Collectors.toSet())) {
                    bought_seats.put(row, tickets.stream().filter(t -> t.getCategory().equals("ground_floor") && t.getRow() == row)
                            .map(t -> t.getRow_seat()).collect(Collectors.toSet()));
                }
                for (int row = 1; row <= theatre.getGround_floor_rows(); ++row) {
                    for (int seat = 1; seat <= theatre.getGround_floor_row_seats(); ++seat) {
                        if (bought_seats.isEmpty() || !bought_seats.containsKey(row) ||
                                !bought_seats.get(row).contains(seat)) {
                            if (!free_seats.containsKey(row)) {
                                free_seats.put(row, new HashSet<>());
                            }
                            free_seats.get(row).add(seat);
                        }
                    }
                }
                model.addAttribute("rows", free_seats.keySet());
            } else if (category.equals("mezzanine")) {
                free_seats = new HashMap<>();
                bought_seats = new HashMap<>();
                for (int row : tickets.stream().filter(t -> t.getCategory().equals("mezzanine"))
                        .map(t -> t.getRow()).collect(Collectors.toSet())) {
                    bought_seats.put(row, tickets.stream().filter(t -> t.getCategory().equals("mezzanine") && t.getRow() == row)
                            .map(t -> t.getRow_seat()).collect(Collectors.toSet()));
                }
                for (int row = 1; row <= theatre.getMezzanine_rows(); ++row) {
                    for (int seat = 1; seat <= theatre.getMezzanine_row_seats(); ++seat) {
                        if (bought_seats.isEmpty() || !bought_seats.containsKey(row) ||
                                !bought_seats.get(row).contains(seat)) {
                            if (!free_seats.containsKey(row)) {
                                free_seats.put(row, new HashSet<>());
                            }
                            free_seats.get(row).add(seat);
                        }
                    }
                }
                model.addAttribute("rows", free_seats.keySet());
            } else if (category.equals("balcony")) {
                free_seats = new HashMap<>();
                bought_seats = new HashMap<>();
                for (int row : tickets.stream().filter(t -> t.getCategory().equals("balcony"))
                        .map(t -> t.getRow()).collect(Collectors.toSet())) {
                    bought_seats.put(row, tickets.stream().filter(t -> t.getCategory().equals("balcony") && t.getRow() == row)
                            .map(t -> t.getRow_seat()).collect(Collectors.toSet()));
                }
                for (int row = 1; row <= theatre.getBalcony_rows(); ++row) {
                    for (int seat = 1; seat <= theatre.getBalcony_row_seats(); ++seat) {
                        if (bought_seats.isEmpty() || !bought_seats.containsKey(row) ||
                                !bought_seats.get(row).contains(seat)) {
                            if (!free_seats.containsKey(row)) {
                                free_seats.put(row, new HashSet<>());
                            }
                            free_seats.get(row).add(seat);
                        }
                    }
                }
                model.addAttribute("rows", free_seats.keySet());
            }

            model.addAttribute("category", category);
            return "tickets/choose_tickets";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get free seats");
            return "error";
        }
    }

    @RequestMapping(value = "/choose_row", method = RequestMethod.GET)
    public String chooseRow(@RequestParam(name = "row", required = true) Integer row,
                            @CookieValue(value = "login", required = false) String login,
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
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            model.addAttribute("performance", performance);

            SessionDAO sessionDAO = new SessionDAO();
            Session session = sessionDAO.getEntityById(session_id, Session.class);
            model.addAttribute("sess", session);

            this.row = row;
            model.addAttribute("seats", free_seats.get(row));
            model.addAttribute("rows", free_seats.keySet());
            model.addAttribute("row", row);
            model.addAttribute("category", category);
            return "tickets/choose_tickets";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot choose seat");
            return "error";
        }
    }

    @RequestMapping(value = "/buy_ticket", method = RequestMethod.GET)
    public String buyTicket(@RequestParam(name = "seat", required = true) Integer seat,
                            @CookieValue(value = "login", required = false) String login,
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
            TicketDAO ticketDAO = new TicketDAO();
            Long id = ticketDAO.getCount(Ticket.class) + 1;
            ticketDAO.create(new Ticket(id, session_id, category, row, seat));
            model.addAttribute("id", id);

            PerformanceDAO performanceDAO = new PerformanceDAO();
            Performance performance = performanceDAO.getEntityById(perf_id, Performance.class);
            model.addAttribute("performance", performance);

            SessionDAO sessionDAO = new SessionDAO();
            Session session = sessionDAO.getEntityById(session_id, Session.class);
            model.addAttribute("sess", session);

            model.addAttribute("category", category);
            model.addAttribute("row", row);
            model.addAttribute("seat", seat);
            return "tickets/buy_ticket";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot buy ticket");
            return "error";
        }
    }
}
