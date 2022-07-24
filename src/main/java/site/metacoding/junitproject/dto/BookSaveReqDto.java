package site.metacoding.junitproject.dto;


import lombok.Getter;
import site.metacoding.junitproject.domain.Book;

@Getter
public class BookSaveReqDto {
    private String title;
    private String author;

    public Book toEntity(){
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
