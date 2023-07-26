package com.target.ready.library.system.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.target.ready.library.system.Entity.Book;
import com.target.ready.library.system.Dto.BookDto;
import com.target.ready.library.system.Service.LibrarySystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("library_system/v1")
public class LibraryController {
    private final LibrarySystemService librarySystemService;

    LibraryController(LibrarySystemService librarySystemService) {
        this.librarySystemService = librarySystemService;
    }

    @GetMapping("books_directory")
    @Operation(
            description = "Get all the books on the given range of pages (range given by the backend)",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public List<Book> getAllBooks(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber) {
        List<Book> books;
        int pageSize = 5;
        try {
            if (pageNumber < 0) {
                return Collections.emptyList();
            }
            books = librarySystemService.getAllBooks(pageNumber, pageSize);
            return books;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @PostMapping("inventory/books")
    @Operation(
            description = "Addition of books and its details",
            responses = { @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public String addBook(@RequestBody BookDto bookDto) throws JsonProcessingException {

        return librarySystemService.addBook(bookDto);
    }

    @GetMapping("book/{bookId}")
    @Operation(
            description = "Get book according to its id",
            responses = { @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json"
                    ))})
    public Book findByBookId(@PathVariable int bookId) {
        return librarySystemService.findByBookId(bookId);
    }

    @GetMapping("book/category/{categoryName}")
    public List<Book> findBookByCategoryName(@PathVariable String categoryName){
        return librarySystemService.findBookByCategoryName(categoryName);
    }

    @DeleteMapping("book/{bookId}")
    public String deleteBook(@PathVariable("bookId") int bookId) {

        return librarySystemService.deleteBook(bookId);
    }

    @PutMapping("inventory/book_update/{id}")
    public ResponseEntity<?> updateBookDetails(@PathVariable("id") int id, @RequestBody Book book) {
        Book existingBook = librarySystemService.findByBookId(id);
        if (existingBook == null) {
            return new ResponseEntity<>("Book with id: " + id + ", does not exist in database", HttpStatus.NOT_FOUND);
        } else {
            Book updatedBook = librarySystemService.updateBookDetails(id, book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        }
    }


}
