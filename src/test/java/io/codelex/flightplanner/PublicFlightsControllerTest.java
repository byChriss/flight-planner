package io.codelex.flightplanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.codelex.flightplanner.api.Airport;
import io.codelex.flightplanner.api.FindFlightRequest;
import io.codelex.flightplanner.api.Flight;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PublicFlightsController.class)
public class PublicFlightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService service;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        builder.modules(javaTimeModule);
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.registerModule(javaTimeModule);
    }


    @Test
    public void should_get_400_when_from_and_to_are_equal() throws Exception {
        //given
        FindFlightRequest request = new FindFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Latvia", "Riga", "RIX"),
                LocalDate.now(),
                LocalDate.now()
        );
        String json = MAPPER.writeValueAsString(request);
        //expect
        mockMvc.perform(MockMvcRequestBuilders.post("/api/flights")
                .content(json)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_200_when_from_and_to_equals() throws Exception {
        //given
        FindFlightRequest request = new FindFlightRequest(
                new Airport("Latvia", "Riga", "RIX"),
                new Airport("Sweden", "Stockholm", "SDA"),
                LocalDate.now(),
                LocalDate.now()
        );
        String jsonResponse = MAPPER.writeValueAsString(request);
        Flight flight = new Flight(1L,
                request.getFrom(),
                request.getTo(),
                "Rynair",
                request.getDeparture().atStartOfDay(),
                request.getArrival().atStartOfDay());

        Mockito.lenient()
                .when(service.findFlights(any()))
                .thenReturn(Collections.singletonList(flight));
        //expect
        String json = mockMvc.perform(MockMvcRequestBuilders.post("/api/flights")
                .content(jsonResponse)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Flight> flights = MAPPER.readValue(json, new TypeReference<List<Flight>>() {
                }
        );
        //then
        assertFalse(flights.isEmpty());
    }
}