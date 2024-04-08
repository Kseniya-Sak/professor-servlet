package edu.sakovich.servlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.db.ConnectionManagerImpl;
import edu.sakovich.servlet.repository.SubjectRepository;
import edu.sakovich.servlet.repository.impl.SubjectRepositoryImpl;
import edu.sakovich.servlet.repository.mapper.impl.SubjectResultSetMapperImpl;
import edu.sakovich.servlet.service.SubjectService;
import edu.sakovich.servlet.service.impl.SubjectServiceImpl;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.SubjectDtoMapperImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/subject/*")
public class SubjectServlet extends HttpServlet {
    private static final String RESPONSE_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    private final SubjectService subjectService;
    private final ObjectMapper objectMapper;

    public SubjectServlet() {
        ConnectionManager connectionManager = new ConnectionManagerImpl();
        SubjectRepository subjectRepository = new SubjectRepositoryImpl(connectionManager, new SubjectResultSetMapperImpl());
        subjectService = new SubjectServiceImpl(subjectRepository, new SubjectDtoMapperImpl());
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
                Set<SubjectOutGoingDto> departmentDtoSet = subjectService.findAll();
                response = objectMapper.writeValueAsString(departmentDtoSet);
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                SubjectWithProfessorsOutGoingDto subject = subjectService.findById(id);
                response = objectMapper.writeValueAsString(subject);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String response = "";
        try {
            String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            SubjectIncomingDto subjectRequest = objectMapper.readValue(requestBody, SubjectIncomingDto.class);
            response = objectMapper.writeValueAsString(subjectService.save(subjectRequest));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
            subjectService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(RESPONSE_TYPE);
        resp.setCharacterEncoding(CHARACTER_ENCODING);

        String response = "";

        try {
            String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            SubjectIncomingDto subjectRequest = objectMapper.readValue(requestBody, SubjectIncomingDto.class);
            subjectService.update(subjectRequest);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = e.getMessage();
        }

        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(response);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
