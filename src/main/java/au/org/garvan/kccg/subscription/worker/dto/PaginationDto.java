package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by ahmed on 11/1/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PaginationDto {
        @JsonProperty
        Integer pageNo;
        @JsonProperty
        Integer pageSize;
        @JsonProperty
        Integer totalArticles;
        @JsonProperty
        Integer totalPages;

}

