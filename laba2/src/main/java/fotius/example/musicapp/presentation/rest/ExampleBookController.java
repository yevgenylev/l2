package fotius.example.musicapp.presentation.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/books")
public class ExampleBookController {

    // Can be used as:
    // curl -X POST localhost:8080/api/books -H 'Content-Type: application/json' -d '{"title":"DDD", "author": "Evans"}'
    @RequestMapping(
        method = RequestMethod.POST, // <- Method
        consumes = MediaType.APPLICATION_JSON_VALUE, // Expect  application/json request body 
        produces = MediaType.APPLICATION_JSON_VALUE //  Produce application/json response body
    )
    @ResponseBody // will ensure that book gets serialised to json
    public Book createBook(@RequestBody BookCreationRequest request) { // @RequestBody will ensure that request is deserialized to BookCreationRequest instance 
        // some random book to be returned
        return new Book(
            ThreadLocalRandom.current().nextLong(0, 1000),
            request.title,
            request.author
        );
    }

    // Can be used as:
    // curl -X GET localhost:8080/api/books/123
    // curl localhost:8080/api/books/123
    @RequestMapping(
        method = RequestMethod.GET, // <- Method
        path = "{id}", // <- additionally to /api/books also receive id of the book, so /api/books/{id}
        produces = MediaType.APPLICATION_JSON_VALUE //  Produce application/json response body
    )
    @ResponseBody // will ensure that book gets serialised to json
    public Book getBook(@PathVariable("id") Long id) { // @PathVariable extracts actual value from path placeholder {id} 
        return new Book(id, "title", "author");
    }

    // Can be used as:
    // curl localhost:8080/api/books?author=Evans
    // curl localhost:8080/api/books
    @RequestMapping(
        method = RequestMethod.GET, // <- Method
        produces = MediaType.APPLICATION_JSON_VALUE //  Produce application/json response body
    )
    @ResponseBody // will ensure that book gets serialised to json
    public ResponseEntity<List<Book>> getBook(@RequestParam(value = "author", required = false) String author) { // @RequestParam extracts actual value from query parameter 
        return ResponseEntity.ok()
            .header("MyCustomHeader", "header-value")
            .body(
                List.of(
                    new Book(1L, "title", author == null ? "author" : author),
                    new Book(2L, "title", author == null ? "author2" : author)
                )
            );
    }

    @Data
    @AllArgsConstructor
    public static class Book {
        private Long id;
        private String title;
        private String author;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookCreationRequest {
        private String title;
        private String author;
    }
}
