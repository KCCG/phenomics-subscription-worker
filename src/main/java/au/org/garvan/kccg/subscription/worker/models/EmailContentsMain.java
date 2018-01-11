package au.org.garvan.kccg.subscription.worker.models;

import lombok.Data;

import java.util.List;

/**
 * Created by ahmed on 11/1/18.
 */
@Data
public class EmailContentsMain {

    Integer totalArticles;
    Integer digestArticles;
    List<EmailArticle> lstItems;


}
