package controllers;

import DAO.TheatreDAO;
import com.google.common.collect.Lists;
import entities.Theatre;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public class TheatreController {
    private static int firstResult = 1;
    private static boolean lastPage = false;

    private static int firstResultFilter = 1;
    private static boolean lastPageFilter = false;

    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getTheatres(Model model) {
        TheatreDAO theatreDAO = new TheatreDAO();
        firstResult = 1;
        List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
        if (theatres.size() < 10) {
            lastPage = true;
        }
        model.addAttribute("theatres", theatres);
        return "theatres";
    }
    // order to getAll
    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getNextTheatres(Model model) {
        TheatreDAO theatreDAO = new TheatreDAO();
        if (!lastPage) {
            firstResult += 10;
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
            if (theatres.size() < 10) {
                lastPage = true;
            } else if (theatres.size() == 0) {
                firstResult -= 10;
                theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
            }
            model.addAttribute("theatres", theatres);
        } else {
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
            model.addAttribute("theatres", theatres);
        }
        return "theatres";
    }

    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getPrevTheatres(Model model) {
        TheatreDAO theatreDAO = new TheatreDAO();
        if (firstResult - 10 >= 1) {
            firstResult -= 10;
            lastPage = false;
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
            model.addAttribute("theatres", theatres);
        } else {
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResult, 10);
            model.addAttribute("theatres", theatres);
        }
        return "theatres";
    }

    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String filterTheatres(@RequestParam(name = "name", required = true) String name, Model model) {
        firstResultFilter = 1;
        TheatreDAO theatreDAO = new TheatreDAO();
        List<Theatre> theatres = theatreDAO.filter(Map.of("theatreFilter",
                Lists.newArrayList("%" + name + "%")), Theatre.class, firstResultFilter, 10);
        if (theatres.size() < 10) {
            lastPageFilter = true;
        }
        model.addAttribute("theatres", theatres);
        return "theatres";
    }

    // getNextTheatresFilter, getPrevTheatresFilter
    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getNextTheatresFilter(Model model) {
        TheatreDAO theatreDAO = new TheatreDAO();
        if (!lastPageFilter) {
            firstResultFilter += 10;
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResultFilter, 10);
            if (theatres.size() < 10) {
                lastPageFilter = true;
            } else if (theatres.size() == 0) {
                firstResultFilter -= 10;
                theatres = theatreDAO.getAll(Theatre.class, firstResultFilter, 10);
            }
            model.addAttribute("theatres", theatres);
        } else {
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResultFilter, 10);
            model.addAttribute("theatres", theatres);
        }
        return "theatres";
    }

    @RequestMapping(value = "/theatres", method = RequestMethod.GET)
    public String getPrevTheatresFilter(Model model) {
        TheatreDAO theatreDAO = new TheatreDAO();
        if (firstResultFilter - 10 >= 1) {
            firstResultFilter -= 10;
            lastPageFilter = false;
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResultFilter, 10);
            model.addAttribute("theatres", theatres);
        } else {
            List<Theatre> theatres = theatreDAO.getAll(Theatre.class, firstResultFilter, 10);
            model.addAttribute("theatres", theatres);
        }
        return "theatres";
    }
}
