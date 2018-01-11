package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.constants.EmailHTML;
import au.org.garvan.kccg.subscription.worker.dto.ArticleDto;
import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import au.org.garvan.kccg.subscription.worker.models.EmailArticle;
import au.org.garvan.kccg.subscription.worker.models.EmailContentsMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by ahmed on 10/1/18.
 */
public class EmailGenerator {

    private static EmailHTML emailHTMLMaker = new EmailHTML();

    public static void prepapreAndSendEmail(SubscriptionDto subscription, SearchResponseDto articles) {
        String email = prepareEmail(subscription, articles);
        sendEmail(subscription, email);
    }

    private static String prepareEmail(SubscriptionDto subscription, SearchResponseDto articlesObject) {
        String emailHtml = "";
        EmailContentsMain emailContents = new EmailContentsMain();
        emailContents.setTotalArticles(articlesObject.getPagination().getTotalArticles());


        List<String> genesInQuery = subscription.getGenesInQuery();
        if (articlesObject.getArticles().size() > 0) {
            emailContents.setDigestArticles(articlesObject.getArticles().size());
            articlesObject.getArticles().sort(Comparator.comparing(ArticleDto::getArticleRank));
            List<EmailArticle> lstItems = new ArrayList<>();
            Integer id = 1;
            for (ArticleDto articleDto : articlesObject.getArticles()) {
                EmailArticle emailArticle = new EmailArticle();
                emailArticle.setId(id);
                emailArticle.setPMID(articleDto.getPmid().toString());
                emailArticle.setTitle(articleDto.getArticleTitle());

                Map<String, Integer> queriedGenes = new HashMap<>();
                Map<String, Integer> otherGenes = new HashMap<>();
                Map<String, Integer> articleGenes = articleDto.getGenesWithCount();

                if(articleGenes.size()>0) {
                    for (Map.Entry<String, Integer> entry : articleGenes.entrySet()) {
                        if (genesInQuery.contains(entry.getKey())) {
                            queriedGenes.put(entry.getKey(), entry.getValue());
                        } else {
                            otherGenes.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                emailArticle.setQueriedGenes(queriedGenes);
                emailArticle.setOtherGenes(otherGenes);

                lstItems.add(emailArticle);
                id++;
            }

            emailContents.setLstItems(lstItems);
        } else {
            emailContents.setDigestArticles(0);
            emailContents.setLstItems(new ArrayList<>());

        }

        StringBuilder stringBuilder = emailHTMLMaker.constructEmail(emailContents);
        emailHtml = stringBuilder.toString();
        return emailHtml;
    }

    private static boolean sendEmail(SubscriptionDto subscription, String email) {


        return true;

    }


}
