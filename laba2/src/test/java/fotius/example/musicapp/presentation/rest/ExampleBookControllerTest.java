package fotius.example.musicapp.presentation.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleBookControllerTest {

    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void createsABook() {
        final ResponseEntity<ExampleBookController.Book> response = restTemplate.postForEntity(
            "/api/books",
            new ExampleBookController.BookCreationRequest("DDD", "Evans"),
            ExampleBookController.Book.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals(response.getBody().getAuthor(), "Evans");
        assertEquals(response.getBody().getTitle(), "DDD");
    }
}
