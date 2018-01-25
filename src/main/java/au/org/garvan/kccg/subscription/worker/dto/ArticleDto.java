package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

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
    JSONArray annotations;

    @JsonProperty
    Integer pmid;

    public List<ConceptDto> getArticleConcepts() {
        List<ConceptDto> articleConcepts = new ArrayList<>();
        if (annotations != null) {

            ArrayList<Object> geneList = (ArrayList) annotations;
            for (Object obj : geneList) {
                HashMap concept = (HashMap) obj;
                articleConcepts.add(
                        new ConceptDto(
                                concept.get("id").toString(),
                                concept.get("type").toString(),
                                concept.get("text").toString(),
                                ((ArrayList) concept.get("offsets")).size())
                );
            }
        }


        return articleConcepts;
    }


}