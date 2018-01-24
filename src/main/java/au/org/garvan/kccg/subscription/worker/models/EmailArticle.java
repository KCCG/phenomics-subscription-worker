package au.org.garvan.kccg.subscription.worker.models;

import lombok.Data;

import java.util.Map;

/**
 * Created by ahmed on 11/1/18.
 */
@Data
public class EmailArticle {

    Integer id;
    String title;
    String PMID;
    Map<String, Integer> queriedGenes;
    Map<String, Integer> otherGenes;

}
