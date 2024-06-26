package sigma.chackcheck.domain.book.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sigma.chackcheck.common.pagination.PagePolicy;
import sigma.chackcheck.common.pagination.service.Pagination;
import sigma.chackcheck.domain.book.domain.Book;
import sigma.chackcheck.domain.book.domain.BookApprove;
import sigma.chackcheck.domain.book.domain.BookCategory;
import sigma.chackcheck.domain.book.dto.response.BookDTO;
import sigma.chackcheck.domain.book.dto.response.BookPageResponse;
import sigma.chackcheck.domain.book.repository.BookApproveRepository;
import sigma.chackcheck.domain.book.repository.BookCategoryRepository;
import sigma.chackcheck.domain.book.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class GetBook implements GetBookUsecase, Pagination<Book> {

    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookApproveRepository bookApproveRepository;

    @Override
    public Book getOneBook(Long id) {
        return bookRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("책이 없어용"));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> getBookPage(int page) {
        Pageable pageable = createDefaultPageRequest(page, PagePolicy.DEFAULT_PAGE);
        return bookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> getBookPageByCategoryName(String categoryName, int page) {
        Pageable pageable = createDefaultPageRequest(page, PagePolicy.DEFAULT_PAGE);

        Page<BookCategory> bookCategoryListByCategoryName =
            bookCategoryRepository
                .findAllByCategoryName(categoryName, pageable);

        return bookCategoryListByCategoryName
            .map(BookCategory::getBook);
    }

    @Override
    public Page<Book> getBookPageBySearch(String keyword, int page) {
        Pageable pageable = createDefaultPageRequest(page, PagePolicy.DEFAULT_PAGE);

        return bookRepository.findByKeyword(keyword, pageable);
    }

    @Override
    public Page<BookApprove> getBookApprovePage(int page) {
        Pageable pageable = createDefaultPageRequest(page, PagePolicy.DEFAULT_PAGE);
        return bookApproveRepository.findAll(pageable);
    }
}
