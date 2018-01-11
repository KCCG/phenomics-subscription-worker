package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<String, Integer> getGenesWithCount(){
        Map<String, Integer> countedGenes = new HashMap<>();
        if(annotations!=null)
        {
            if (annotations.containsKey("genes"))
            {
                ArrayList<Object> geneList = (ArrayList)annotations.get("genes");
                for(Object obj:geneList)
                {
                    HashMap gene = (HashMap)obj;
                    countedGenes.put(gene.get("geneSymbol").toString(), ((ArrayList)gene.get("offsets")).size());
                }
            }
        }

        return countedGenes;
    }


}