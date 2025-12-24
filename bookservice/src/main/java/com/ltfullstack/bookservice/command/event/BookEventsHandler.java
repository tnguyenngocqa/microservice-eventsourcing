package com.ltfullstack.bookservice.command.event;

import com.ltfullstack.bookservice.command.data.Book;
import com.ltfullstack.bookservice.command.data.BookRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookEventsHandler {
    @Autowired
    private BookRepository bookRepository;

    @EventHandler
    public void on(BookCreateEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event, book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdateEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());

        if (oldBook.isPresent()) {
            Book book = oldBook.get();
            book.setName(event.getName());
            book.setAuthor(event.getAuthor());
            book.setIsReady(event.getIsReady());

            bookRepository.save(book);
        }
    }

    @EventHandler
    public void on(BookDeleteEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());

        oldBook.ifPresent(book -> bookRepository.delete(book));
    }
}
