package com.ltfullstack.bookservice.query.projection;

import com.ltfullstack.bookservice.command.data.Book;
import com.ltfullstack.bookservice.command.data.BookRepository;
import com.ltfullstack.bookservice.query.model.BookResponseModel;
import com.ltfullstack.bookservice.query.queries.GetAllBookQuery;
import com.ltfullstack.bookservice.query.queries.GetBookDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookProjection {

    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query) {
        List<Book> list = bookRepository.findAll();
// Way 1
//        List<BookResponseModel> bookResponseModelList = new ArrayList<>();
//
//        list.forEach(book -> {
//            BookResponseModel model = new BookResponseModel();
//            BeanUtils.copyProperties(book, model);
//            bookResponseModelList.add(model);
//        });

// Way 2
        return list.stream().map(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            return model;
        }).toList();
    }

    @QueryHandler
    public BookResponseModel handle(GetBookDetailQuery query) throws Exception {
        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new Exception("Not found Book with bookId = " + query.getId()));
        BookResponseModel bookResponseModel = new BookResponseModel();
        BeanUtils.copyProperties(book, bookResponseModel);
        return bookResponseModel;
    }
}
