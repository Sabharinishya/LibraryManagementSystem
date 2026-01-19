package com.ey.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.dto.request.UpdateBookRequest;
import com.ey.entity.Book;
import com.ey.repository.BookRepository;
@SpringBootTest
class BookServiceTest {
   @Autowired
   private BookService bookService;
   @MockBean
   private BookRepository bookRepository;
   @Test
   void createBook_shouldSaveBook() {
       Book book = new Book();
       book.setTitle("Clean Code");
       when(bookRepository.save(any(Book.class))).thenReturn(book);
       Book saved = bookService.createBook(book);
       assertNotNull(saved);
       assertEquals("Clean Code", saved.getTitle());
   }
   @Test
   void updateBook_shouldUpdateCategory() {
      Book existing = new Book();
      existing.setId(1L);
      existing.setCategory("Old");
      when(bookRepository.findById(1L))
              .thenReturn(java.util.Optional.of(existing));
      when(bookRepository.save(any(Book.class)))
              .thenReturn(existing);
      UpdateBookRequest request = new UpdateBookRequest();
      request.setCategory("New");
      Book updated = bookService.updateBook(1L, request);
      assertEquals("New", updated.getCategory());
   }
}