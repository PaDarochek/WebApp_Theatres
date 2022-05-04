package DAO;

import entities.Admin;

import java.util.List;
import java.util.stream.Collectors;

public class AdminDAO implements DAO<Admin, Integer> {
    public Admin getByLogin(String login) {
        if (login == null) {
            return null;
        }
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> admins = adminDAO.getAll(Admin.class).stream()
                .filter(a -> a.getLogin().equals(login)).collect(Collectors.toList());
        if (admins.size() == 1) {
            return admins.get(0);
        }
        return null;
    }
}
