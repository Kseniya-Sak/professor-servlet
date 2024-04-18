package edu.sakovich.servlet.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sakovich.servlet.db.ConnectionManager;
import edu.sakovich.servlet.db.ConnectionManagerImpl;
import edu.sakovich.servlet.db.DataBaseProperties;
import edu.sakovich.servlet.repository.ProfessorRepository;
import edu.sakovich.servlet.repository.impl.ProfessorRepositoryImpl;
import edu.sakovich.servlet.repository.mapper.impl.ProfessorResultSetMapperImpl;
import edu.sakovich.servlet.service.ProfessorService;
import edu.sakovich.servlet.service.impl.ProfessorServiceImpl;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.ProfessorDtoMapperImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/professor/*")
public class ProfessorServlet extends HttpServlet {
    private static final String RESPONSE_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";

    private final ProfessorService professorService;
    private final ObjectMapper objectMapper;

    public ProfessorServlet() {
        ConnectionManager connectionManager = ConnectionManagerImpl.getInstance(DataBaseProperties.dataSource);
        ProfessorRepository professorRepository = new ProfessorRepositoryImpl(connectionManager,
                new ProfessorResultSetMapperImpl());
        professorService = new ProfessorServiceImpl(professorRepository, new ProfessorDtoMapperImpl());
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
                Set<ProfessorOutGoingDto> professorDtoSet = professorService.findAll();
                response = objectMapper.writeValueAsString(professorDtoSet);
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                ProfessorWithSubjectsOutGoingDto professor = professorService.findById(id);
                response = objectMapper.writeValueAsString(professor);
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
            ProfessorWithSubjectsIncomingDto professorRequest = objectMapper.readValue(requestBody,
                    ProfessorWithSubjectsIncomingDto.class);
            response = objectMapper.writeValueAsString(professorService.save(professorRequest));
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
            professorService.deleteById(id);
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
            ProfessorWithSubjectsIncomingDto professorRequest = objectMapper.readValue(requestBody,
                    ProfessorWithSubjectsIncomingDto.class);
            professorService.update(professorRequest);
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
