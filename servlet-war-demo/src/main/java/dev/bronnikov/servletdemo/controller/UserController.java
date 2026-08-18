package dev.bronnikov.servletdemo.controller;

import dev.bronnikov.servletdemo.User;
import dev.bronnikov.servletdemo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping // GET запрос /users
    public String users(Model model) {
        List<User> users = service.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());

        return "users"; // Возвращаем название шаблона — users.html
    }

    @PostMapping
    public String save(@ModelAttribute User user) {
        service.save(user);

        return "redirect:/mvc/users"; // Возвращаем страницу, чтобы она перезагрузилась
    }


    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        service.deleteById(id);

        return "redirect:/mvc/users";
    }


}
