package site.metacoding.junitproject.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    // @BeforeAll // 테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void 데이터준비(){
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
    }// 트랜잭션 종료 했다면 말이안됨
    // 가정 1: [ 데이터준비() + 1. 책등록 (T) ], [ 데이터준비() + 2. 책목록보기 (T) ]   (O)
    // 가정 2: [ 데이터준비() + 1. 책등록 + 데이터준비() + 2. 책목록보기 (T) ]   (X)

    // 1. 책 등록
    @Test
    public void saveTest(){
        // given (데이터 준비)
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        Book bookPS = bookRepository.save(book);

        // then (검증)
        Assertions.assertEquals(title, bookPS.getTitle());
        Assertions.assertEquals(author, bookPS.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화 함)

    // 2. 책 목록보기
    @Test
    public void 책_목록보기_test(){
        // given
        String title = "junit5";
        String author = "메타코딩";

        // when
        List<Book> books = bookRepository.findAll();

        System.out.println("사이즈 : ===================== : " + books.size());

        // then
        Assertions.assertEquals(title, books.get(0).getTitle());
        Assertions.assertEquals(author, books.get(0).getAuthor());
    }

    // 3. 책 한건보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test(){
        // given
        String title = "junit5";
        String author = "메타코딩";

        // when
        Book bookPS = bookRepository.findById(1L).orElseThrow();

        // then
        Assertions.assertEquals(title, bookPS.getTitle());
        Assertions.assertEquals(author, bookPS.getAuthor());
    }

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then

        Assertions.assertFalse(bookRepository.findById(id).isPresent());

    }

    // 5. 책 수정
}
