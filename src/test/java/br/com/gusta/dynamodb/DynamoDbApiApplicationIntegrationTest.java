package br.com.gusta.dynamodb;

import br.com.gusta.dynamodb.mocks.MockUtils;
import java.util.UUID;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Profile("test")
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DynamoDbApiApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void saveEmployeeTest(CapturedOutput output) throws Exception {
        var employeeInput = MockUtils.getEmployeeInput();

        mockMvc.perform(post("/employee")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isOk());

        assertThat(output.getOut()).contains("Salving employee in dynamoDB");
    }

    @Test
    @Order(2)
    void saveEmployeeWithNullIdTest(CapturedOutput output) throws Exception {
        var employeeInput = MockUtils.getEmployeeInput();
        employeeInput.setEmployeeId(null);

        mockMvc.perform(post("/employee")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isOk());

        assertThat(output.getOut()).contains("Salving employee in dynamoDB");
    }

    @Test
    @Order(3)
    void saveEmployeeWithBlankFieldTest(CapturedOutput output) throws Exception {
        var employeeInput = MockUtils.getEmployeeInput();
        employeeInput.setFirstName("");

        mockMvc.perform(post("/employee")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields[0].name").value("firstName"))
                .andExpect(jsonPath("$.fields[0].message").value("must not be blank"));

        assertThat(output.getOut()).contains("Error processing the request");
    }

    @Test
    @Order(4)
    void saveEmployeeWithInvalidEmailTest(CapturedOutput output) throws Exception {
        var employeeInput = MockUtils.getEmployeeInput();
        employeeInput.setEmail("application/json");

        mockMvc.perform(post("/employee")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields[0].name").value("email"))
                .andExpect(jsonPath("$.fields[0].message").value("must be a well-formed email address"));

        assertThat(output.getOut()).contains("Error processing the request");
    }

    @Test
    @Order(5)
    void saveEmployeeWithNullObjectTest(CapturedOutput output) throws Exception {
        var employeeInput = MockUtils.getEmployeeInput();
        employeeInput.setDepartment(null);

        mockMvc.perform(post("/employee")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields[0].name").value("department"))
                .andExpect(jsonPath("$.fields[0].message").value("must not be null"));

        assertThat(output.getOut()).contains("Error processing the request");
    }

    @Test
    @Order(6)
    void getEmployeeTest(CapturedOutput output) throws Exception {
        var employee = MockUtils.getEmployeeEntity();
        var employeeId = employee.getEmployeeId();

        mockMvc.perform(get("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(employee.getEmail()));

        assertThat(output.getOut()).contains(String.format("Getting info of employee %s in dynamoDB", employeeId));
    }

    @Test
    @Order(7)
    void getEmployeeThrowingExceptionTest(CapturedOutput output) throws Exception {
        var employeeId = UUID.randomUUID().toString();

        mockMvc.perform(get("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("This employee doesn't exists"));

        assertThat(output.getOut()).contains(String.format("Getting info of employee %s in dynamoDB", employeeId));
    }

    @Test
    @Order(8)
    void updateEmployeeTest(CapturedOutput output) throws Exception {
        var employeeId = MockUtils.getEmployeeEntity().getEmployeeId();
        var newEmail = "hecawok634@letpays.com";
        var employeeInput = MockUtils.getEmployeeInput();
        employeeInput.setEmail(newEmail);

        mockMvc.perform(put("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail));

        assertThat(output.getOut()).contains(String.format("Update info of employee %s in dynamoDB", employeeId));
    }

    @Test
    @Order(9)
    void updateThrowingExceptionTest(CapturedOutput output) throws Exception {
        var employeeId = UUID.randomUUID().toString();
        var employeeInput = MockUtils.getEmployeeInput();

        mockMvc.perform(put("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .content(MockUtils.getJson(employeeInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("This employee can't update his data"));

        assertThat(output.getOut()).contains("Error processing the request");
    }

    @Test
    @Order(10)
    void deleteEmployeeTest(CapturedOutput output) throws Exception {
        var employeeId = MockUtils.getEmployeeEntity().getEmployeeId();

        mockMvc.perform(delete("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(output.getOut()).contains(String.format("Deleting info of employee %s in dynamoDB", employeeId));
    }

    @Test
    @Order(11)
    void deleteThrowingExceptionTest(CapturedOutput output) throws Exception {
        var employeeId = UUID.randomUUID().toString();

        mockMvc.perform(delete("/employee/{id}", employeeId)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("This employee can't be deleted"));

        assertThat(output.getOut()).contains("Error processing the request");
    }

}