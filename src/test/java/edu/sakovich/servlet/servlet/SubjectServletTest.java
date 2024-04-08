package edu.sakovich.servlet.servlet;

import edu.sakovich.servlet.service.impl.SubjectServiceImpl;
import edu.sakovich.servlet.servlet.dto.ProfessorOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectIncomingDto;
import edu.sakovich.servlet.servlet.dto.SubjectOutGoingDto;
import edu.sakovich.servlet.servlet.dto.SubjectWithProfessorsOutGoingDto;
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

class SubjectServletTest {
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
        Set<SubjectOutGoingDto> subjects = new LinkedHashSet<>();
        SubjectOutGoingDto subject = new SubjectOutGoingDto();
        subject.setId(1);
        subject.setName("test");
        subject.setValueOfHours(72);
        subjects.add(subject);

        try (MockedConstruction<SubjectServiceImpl> mockDepatmentSer = Mockito.mockConstruction(SubjectServiceImpl.class, (mock, context) -> {
            when(mock.findAll()).thenReturn(subjects);
        })) {
            SubjectServlet servlet = new SubjectServlet();

            doReturn(null).when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdSuccess() throws ServletException, IOException {
        ProfessorOutGoingDto professor = new ProfessorOutGoingDto();
        professor.setId(1);
        professor.setName("testName");
        professor.setSurname("testSurname");

        SubjectWithProfessorsOutGoingDto subject = new SubjectWithProfessorsOutGoingDto();
        subject.setId(1);
        subject.setName("testName");
        subject.setValueOfHours(72);
        subject.setProfessors(new LinkedHashSet<>(Collections.singleton(professor)));

        try (MockedConstruction<SubjectServiceImpl> mockDepatmentSer = Mockito.mockConstruction(SubjectServiceImpl.class, (mock, context) -> {
            when(mock.findById(1)).thenReturn(subject);
        })) {
            SubjectServlet servlet = new SubjectServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdFail() throws ServletException, IOException {
        doReturn("").when(mockRequest).getPathInfo();

        new SubjectServlet().doGet(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPostSuccess() throws IOException, ServletException {
        SubjectIncomingDto subjectRequest = new SubjectIncomingDto();
        subjectRequest.setName("test");
        subjectRequest.setValueOfHours(72);

        SubjectOutGoingDto savedSubject = new SubjectOutGoingDto();
        savedSubject.setId(1);
        savedSubject.setName("test");
        savedSubject.setValueOfHours(72);

        String requestBody = """
                    {
                        "name": "test"
                    }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<SubjectServiceImpl> mockDepatmentSer = Mockito.mockConstruction(SubjectServiceImpl.class, (mock, contex) -> {
            when(mock.save(subjectRequest)).thenReturn(savedSubject);
        })) {
            SubjectServlet servlet = new SubjectServlet();

            when(mockRequest.getReader()).thenReturn(bufferedReader);
            servlet.doPost(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
            bufferedReader.close();
        }
    }

    @Test
    void testDoPostFail() throws IOException, ServletException {
        StringReader stringReader = new StringReader("{}");
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        when(mockRequest.getReader()).thenReturn(bufferedReader);

        new SubjectServlet().doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDeleteSuccess() throws ServletException, IOException {
        try (MockedConstruction<SubjectServiceImpl> mockDepatmentSer = Mockito.mockConstruction(SubjectServiceImpl.class, (mock, context) -> {
            when(mock.deleteById(1)).thenReturn(true);
        })) {
            SubjectServlet servlet = new SubjectServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doDelete(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoDeleteFail() throws ServletException, IOException {
        doReturn("").when(mockRequest).getPathInfo();

        new SubjectServlet().doDelete(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    @Test
    void testDoPutSuccess() throws IOException, ServletException {
        SubjectIncomingDto subjectIncomingDto = new SubjectIncomingDto();
        subjectIncomingDto.setId(1);
        subjectIncomingDto.setName("updateName");
        subjectIncomingDto.setValueOfHours(72);

        String requestBody = """
                {
                    "id": 1,
                    "name": "updateName",
                    "valueOfHours": 72
                }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<SubjectServiceImpl> mockDepatmentSer = Mockito.mockConstruction(SubjectServiceImpl.class, (mock, contex) -> {
            when(mock.update(subjectIncomingDto)).thenReturn(true);
        })) {
            SubjectServlet servlet = new SubjectServlet();

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

        new SubjectServlet().doPut(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @AfterEach
    void reset() {
        writer.close();
    }

}