package controllers;

import DAO.PerformanceDAO;
import DAO.SessionDAO;
import entities.Performance;
import entities.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getMainPage(Model model) {
        SessionDAO sessionDAO = new SessionDAO();
        List<Session> sessions = sessionDAO.sort(Map.of("date_time", "asc"), Session.class)
                .stream().limit(20).filter(session -> {return session.getDate_time()
                        .after(new Timestamp(System.currentTimeMillis()));}).collect(Collectors.toList());
        model.addAttribute("sessions", sessions);
        return "main_page/home";
    }

}
