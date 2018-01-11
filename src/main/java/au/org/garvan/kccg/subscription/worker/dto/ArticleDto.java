package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

/**
 * Created by ahmed on 11/1/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class ArticleDto {
    @JsonProperty
    String articleTitle;

    @JsonProperty
    Integer articleRank;

    @JsonProperty
    String datePublished;

    @JsonProperty
    JSONObject annotations;

    @JsonProperty
    Integer pmid;




}