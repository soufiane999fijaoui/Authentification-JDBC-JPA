package ma.fstg.security.web;

import ma.fstg.security.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"/admin", "/admin/dashboard"})
    public String adminDashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }
}
