package controllers;

import DAO.AdminDAO;
import entities.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public String authorize(Model model) {
        return "main_page/authorize";
    }

    @RequestMapping(value = "/authorize_check", method = RequestMethod.GET)
    public String authorize(HttpServletResponse response,
                            @RequestParam(name = "login", required = true) String login,
                            @RequestParam(name = "password", required = true) String password,
                            Model model) {
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getByLogin(login);
        if (admin != null) {
            if (admin.getPassword().equals(password)) {
                Cookie loginCookie = new Cookie("login", login);
                Cookie passwordCookie = new Cookie("password", password);
                response.addCookie(loginCookie);
                response.addCookie(passwordCookie);
                model.addAttribute("admin", true);
                MainPageController mainPageController = new MainPageController();
                return mainPageController.getMainPage(login, password, model);
            }
            else {
                model.addAttribute("msg", "Incorrect password");
            }
        }
        model.addAttribute("msg", "Incorrect login");
        return "error";
    }
}
