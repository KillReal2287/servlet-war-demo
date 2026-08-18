package dev.bronnikov.servletdemo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("HelloServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        if (name == null || name.isBlank()) {
            name = "Tomcat";
        }

        System.out.println("GET /hello, name=" + name);

        response.setContentType("text/plain; charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println("Hello, " + name + "!");
        writer.println("Time: " + LocalDateTime.now());
    }

    @Override
    public void destroy() {
        System.out.println("HelloServlet destroyed");
    }
}
