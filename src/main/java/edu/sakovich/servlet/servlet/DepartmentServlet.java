package edu.sakovich.servlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.db.ConnectionManagerImpl;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.repository.impl.DepartmentRepositoryImpl;
import edu.sakovich.servlet.repository.mapper.impl.DepartmentResultSetMapperImpl;
import edu.sakovich.servlet.service.DepartmentService;
import edu.sakovich.servlet.service.impl.DepartmentServiceImpl;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapperImpl;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/department/*")
public class DepartmentServlet extends HttpServlet {
    private static final String RESPONSE_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;

    public DepartmentServlet() {
        ConnectionManager connectionManager = new ConnectionManagerImpl();
        DepartmentRepository departmentRepository = new DepartmentRepositoryImpl(connectionManager, new DepartmentResultSetMapperImpl());
        departmentService = new DepartmentServiceImpl(departmentRepository, new DepartmentDtoMapperImpl());
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String response = "";
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                Set<DepartmentOutGoingDto> departmentDtoSet = departmentService.findAll();
                response = objectMapper.writeValueAsString(departmentDtoSet);
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                FindByIdDepartmentOutGoingDto department = departmentService.findById(id);
                response = objectMapper.writeValueAsString(department);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        DepartmentIncomingDto departmentRequest = objectMapper.readValue(requestBody, DepartmentIncomingDto.class);
        String response = "";
        try {
            response = objectMapper.writeValueAsString(departmentService.save(departmentRequest));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String response = "";
        try {
            String pathInfo = req.getPathInfo();
            int id = Integer.parseInt(pathInfo.substring(1));
            departmentService.deleteById(id);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        DepartmentIncomingDto departmentRequest = objectMapper.readValue(requestBody, DepartmentIncomingDto.class);
        String response = "";

        try {
            departmentService.update(departmentRequest);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        }
    }
}
