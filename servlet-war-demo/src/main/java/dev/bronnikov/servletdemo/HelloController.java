package dev.bronnikov.servletdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        System.out.println("Spring MVC controller: GET /mvc/hello");
        return "Hello from Spring MVC";
    }
}
