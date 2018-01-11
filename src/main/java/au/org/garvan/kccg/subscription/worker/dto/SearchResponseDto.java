package au.org.garvan.kccg.subscription.worker.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by ahmed on 11/1/18.
 */
@Data
public class SearchResponseDto {
    PaginationDto pagination;
    List<ArticleDto> articles;
}
