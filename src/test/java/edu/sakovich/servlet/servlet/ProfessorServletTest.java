package edu.sakovich.servlet.servlet;

import edu.sakovich.servlet.service.impl.ProfessorServiceImpl;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsIncomingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorWithSubjectsOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfessorServletTest {
    HttpServletRequest mockRequest;
    HttpServletResponse mockResponse;
    PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGetAllSuccess() throws ServletException, IOException {
        Set<ProfessorOutGoingDto> professors = new LinkedHashSet<>();
        ProfessorOutGoingDto professor = new ProfessorOutGoingDto();
        professor.setId(1);
        professor.setName("testName");
        professor.setSurname("testSurname");
        professors.add(professor);

        try (MockedConstruction<ProfessorServiceImpl> mockDepatmentSer = Mockito.mockConstruction(ProfessorServiceImpl.class, (mock, context) -> {
            when(mock.findAll()).thenReturn(professors);
        })) {
            ProfessorServlet servlet = new ProfessorServlet();

            doReturn(null).when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdSuccess() throws ServletException, IOException {
        ProfessorWithSubjectsOutGoingDto professor = new ProfessorWithSubjectsOutGoingDto();
        SubjectOutGoingDto subject = new SubjectOutGoingDto();
        subject.setId(1);
        subject.setName("testSubject");
        subject.setValueOfHours(46);

        DepartmentOutGoingDto department = new DepartmentOutGoingDto();
        department.setId(1);
        department.setName("testDepartment");

        professor.setName("updateName");
        professor.setSurname("updateSurname");
        professor.setDepartment(department);
        professor.setSubjects(new LinkedHashSet<>(Collections.singleton(subject)));

        try (MockedConstruction<ProfessorServiceImpl> mockDepatmentSer = Mockito.mockConstruction(ProfessorServiceImpl.class, (mock, context) -> {
            when(mock.findById(1)).thenReturn(professor);
        })) {
            ProfessorServlet servlet = new ProfessorServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdFail() throws ServletException, IOException {
        doReturn("").when(mockRequest).getPathInfo();

        new ProfessorServlet().doGet(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPostSuccess() throws IOException, ServletException {
        ProfessorWithSubjectsIncomingDto professorRequest = new ProfessorWithSubjectsIncomingDto();
        SubjectIncomingDto subject = new SubjectIncomingDto();
        subject.setId(1);
        subject.setName("testSubject");
        subject.setValueOfHours(46);

        DepartmentIncomingDto department = new DepartmentIncomingDto();
        department.setId(1);
        department.setName("testDepartment");

        professorRequest.setName("updateName");
        professorRequest.setSurname("updateSurname");
        professorRequest.setDepartment(department);
        professorRequest.setSubjects(new LinkedHashSet<>(Collections.singleton(subject)));

        String requestBody = """
                {
                    "name": "updateName",
                    "surname": "updateSurname",
                     "department": {
                        "id": 1,
                        "name": "testDepartment"
                    },
                    "subjects": [
                        {
                            "id": 1,
                            "name": "testSubject",
                            "valueOfHours": 46
                        }
                    ]
                }
                    """;
        ProfessorWithSubjectsOutGoingDto savedProfessor = new ProfessorWithSubjectsOutGoingDto();
        savedProfessor.setId(1);
        savedProfessor.setName(professorRequest.getName());
        savedProfessor.setSurname(professorRequest.getSurname());

        DepartmentOutGoingDto departmentOutGoingDto = new DepartmentOutGoingDto();
        departmentOutGoingDto.setId(1);
        departmentOutGoingDto.setName(department.getName());
        savedProfessor.setDepartment(departmentOutGoingDto);

        SubjectOutGoingDto subjectOutGoingDto = new SubjectOutGoingDto();
        subjectOutGoingDto.setId(subject.getId());
        subjectOutGoingDto.setName(subject.getName());
        subjectOutGoingDto.setValueOfHours(subject.getValueOfHours());
        savedProfessor.setSubjects(new LinkedHashSet<>(Collections.singleton(subjectOutGoingDto)));

        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<ProfessorServiceImpl> mockDepatmentSer = Mockito.mockConstruction(ProfessorServiceImpl.class, (mock, contex) -> {
            when(mock.save(professorRequest)).thenReturn(savedProfessor);
        })) {
            ProfessorServlet servlet = new ProfessorServlet();

            when(mockRequest.getReader()).thenReturn(bufferedReader);
            servlet.doPost(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
            bufferedReader.close();
        }
    }

    @Test
    void testDoPostFail() throws IOException, ServletException {
        String requestBody = """
                    {
                    }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(mockRequest.getReader()).thenReturn(bufferedReader);

        new ProfessorServlet().doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDeleteSuccess() throws ServletException, IOException {
        try (MockedConstruction<ProfessorServiceImpl> mockDepatmentSer = Mockito.mockConstruction(ProfessorServiceImpl.class, (mock, context) -> {
            when(mock.deleteById(1)).thenReturn(true);
        })) {
            ProfessorServlet servlet = new ProfessorServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doDelete(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoDeleteFail() throws ServletException, IOException {
        doReturn("").when(mockRequest).getPathInfo();

        new ProfessorServlet().doDelete(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPutSuccess() throws IOException, ServletException {
        ProfessorWithSubjectsIncomingDto professorRequest = new ProfessorWithSubjectsIncomingDto();
        SubjectIncomingDto subject = new SubjectIncomingDto();
        subject.setId(1);
        subject.setName("testSubject");
        subject.setValueOfHours(46);

        DepartmentIncomingDto department = new DepartmentIncomingDto();
        department.setId(1);
        department.setName("testDepartment");

        professorRequest.setId(1);
        professorRequest.setName("updateName");
        professorRequest.setSurname("updateSurname");
        professorRequest.setDepartment(department);
        professorRequest.setSubjects(new LinkedHashSet<>(Collections.singleton(subject)));

        String requestBody = """
                {
                    "id": 1,
                    "name": "updateName",
                    "surname": "updateSurname",
                     "department": {
                        "id": 1,
                        "name": "testDepartment"
                    },
                    "subjects": [
                        {
                            "id": 1,
                            "name": "testSubject",
                            "valueOfHours": 46
                        }
                    ]
                }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<ProfessorServiceImpl> mockDepatmentSer = Mockito.mockConstruction(ProfessorServiceImpl.class, (mock, contex) -> {
            when(mock.update(professorRequest)).thenReturn(true);
        })) {
            ProfessorServlet servlet = new ProfessorServlet();

            when(mockRequest.getReader()).thenReturn(bufferedReader);
            servlet.doPut(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
            bufferedReader.close();
        }
    }
    @Test
    void testDoPutFail() throws IOException, ServletException {
        StringReader stringReader = new StringReader("{}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(mockRequest.getReader()).thenReturn(bufferedReader);

        new ProfessorServlet().doPut(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @AfterEach
    void reset() {
        writer.close();
    }
}