package controllers;

import DAO.AdminDAO;
import DAO.PerformanceDAO;
import DAO.SessionDAO;
import entities.Admin;
import entities.Performance;
import entities.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainPageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(@CookieValue(value = "login", required = false) String login,
                              @CookieValue(value = "password", required = false) String password,
                              Model model) {
        try {
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.getByLogin(login);
            if (admin != null) {
                if (admin.getPassword().equals(password)) {
                    model.addAttribute("admin", true);
                } else {
                    model.addAttribute("msg", "Incorrect password");
                    return "error";
                }
            }
        } catch (Exception e) {
            model.addAttribute("msg", "Incorrect authorization");
            return "error";
        }

        try {
            SessionDAO sessionDAO = new SessionDAO();
            List<Session> sessions = sessionDAO.sort(Map.of("date_time", "asc"), Session.class)
                    .stream().limit(20).filter(session -> {
                        return session.getDate_time()
                                .after(new Timestamp(System.currentTimeMillis()));
                    }).collect(Collectors.toList());
            model.addAttribute("sessions", sessions);
            return "main_page/home";
        } catch (Exception e) {
            model.addAttribute("msg", "Cannot get sessions");
            return "error";
        }
    }

}
