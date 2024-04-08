package edu.sakovich.servlet.servlet;

import edu.sakovich.servlet.service.impl.DepartmentServiceImpl;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorShortOutGoingDto;
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
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DepartmentServletTest {
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
        Set<DepartmentOutGoingDto> departmentDtoSet = new LinkedHashSet<>();
        DepartmentOutGoingDto department = new DepartmentOutGoingDto();
        department.setId(1);
        department.setName("test");
        departmentDtoSet.add(department);

        try (MockedConstruction<DepartmentServiceImpl> mockDepatmentService =
                     Mockito.mockConstruction(DepartmentServiceImpl.class, (mock, context) -> {
            when(mock.findAll()).thenReturn(departmentDtoSet);
        })) {
            DepartmentServlet servlet = new DepartmentServlet();

            doReturn(null).when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdSuccess() throws ServletException, IOException {
        FindByIdDepartmentOutGoingDto department = new FindByIdDepartmentOutGoingDto();
        department.setId(1);
        department.setName("test");
        Set<ProfessorShortOutGoingDto> professors = new LinkedHashSet<>();
        ProfessorShortOutGoingDto professorShortOutGoingDto = new ProfessorShortOutGoingDto();
        professorShortOutGoingDto.setId(1);
        professorShortOutGoingDto.setName("testName");
        professorShortOutGoingDto.setSurname("testSurname");
        professors.add(professorShortOutGoingDto);
        department.setProfessors(professors);

        try (MockedConstruction<DepartmentServiceImpl> mockDepatmentService =
                     Mockito.mockConstruction(DepartmentServiceImpl.class, (mock, context) -> {
            when(mock.findById(1)).thenReturn(department);
        })) {
            DepartmentServlet servlet = new DepartmentServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doGet(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoGetByIdFail() throws ServletException, IOException {
        doReturn("").when(mockRequest).getPathInfo();

        new DepartmentServlet().doGet(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPostSuccess() throws IOException, ServletException {
        DepartmentIncomingDto departmentRequest = new DepartmentIncomingDto();
        departmentRequest.setId(10);
        departmentRequest.setName("test");

        DepartmentOutGoingDto savedDepartment = new DepartmentOutGoingDto();
        savedDepartment.setId(1);
        savedDepartment.setName("test");

        String requestBody = """
                    {
                        "name": "test"
                    }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<DepartmentServiceImpl> mockDepatmentSer = Mockito.mockConstruction(DepartmentServiceImpl.class, (mock, contex) -> {
            when(mock.save(departmentRequest)).thenReturn(savedDepartment);
        })) {
            DepartmentServlet servlet = new DepartmentServlet();

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

        new DepartmentServlet().doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDeleteSuccess() throws ServletException, IOException {
        try (MockedConstruction<DepartmentServiceImpl> mockDepatmentSer = Mockito.mockConstruction(DepartmentServiceImpl.class, (mock, context) -> {
            when(mock.deleteById(1)).thenReturn(true);
        })) {
            DepartmentServlet servlet = new DepartmentServlet();

            doReturn("/1").when(mockRequest).getPathInfo();

            servlet.doDelete(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void testDoDeleteFail() throws ServletException, IOException {
            doReturn("").when(mockRequest).getPathInfo();

            new DepartmentServlet().doDelete(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    @Test
    void testDoPutSuccess() throws IOException, ServletException {
        DepartmentIncomingDto departmentRequest = new DepartmentIncomingDto();
        departmentRequest.setId(10);
        departmentRequest.setName("update");

        String requestBody = """
                    {
                        "id": 10,
                        "name": "update"
                    }
                    """;
        StringReader stringReader = new StringReader(requestBody);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        try (MockedConstruction<DepartmentServiceImpl> mockDepatmentSer = Mockito.mockConstruction(DepartmentServiceImpl.class, (mock, contex) -> {
            when(mock.update(departmentRequest)).thenReturn(true);
        })) {
            DepartmentServlet servlet = new DepartmentServlet();

            when(mockRequest.getReader()).thenReturn(bufferedReader);
            servlet.doPut(mockRequest, mockResponse);
            verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
            bufferedReader.close();
        }
    }
    @Test
    void testDoPutFail() throws IOException, ServletException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(mockResponse.getWriter()).thenReturn(writer);

        new DepartmentServlet().doPut(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @AfterEach
    void reset() {
        writer.close();
    }
}