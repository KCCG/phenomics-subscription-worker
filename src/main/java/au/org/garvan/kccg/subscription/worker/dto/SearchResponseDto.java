package au.org.garvan.kccg.subscription.worker.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 11/1/18.
 */
@Data
public class SearchResponseDto {
    PaginationDto pagination = new PaginationDto();
    List<ArticleDto> articles = new ArrayList<>();
}
