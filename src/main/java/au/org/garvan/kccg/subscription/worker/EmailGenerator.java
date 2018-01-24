package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.constants.EmailHTML;
import au.org.garvan.kccg.subscription.worker.dto.ArticleDto;
import au.org.garvan.kccg.subscription.worker.dto.EmailNotificationRequestDto;
import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import au.org.garvan.kccg.subscription.worker.models.EmailArticle;
import au.org.garvan.kccg.subscription.worker.models.EmailContentsMain;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ahmed on 10/1/18.
 */
public class EmailGenerator {

    private static EmailHTML emailHTMLMaker = new EmailHTML();

    public static void prepareAndSendEmail(SubscriptionDto subscription, SearchResponseDto articles) {

        String emailContent;
        emailContent = prepareEmailWithArticles(subscription, articles);

        sendEmail(subscription, emailContent);
    }

    private static String prepareEmailWithArticles(SubscriptionDto subscription, SearchResponseDto articlesObject) {
        String emailHtml="";
        EmailContentsMain emailContents = new EmailContentsMain();
        emailContents.setTotalArticles(articlesObject.getPagination().getTotalArticles());

        //Get subscription information to put in the email.
        emailContents.setSearchTitle(subscription.getSearchName());
        emailContents.setUnsubscribeLink(subscription.getSubscriptionId());

        // Convert articles into items to be displayed in email.
        List<String> genesInQuery = subscription.getGenesInQuery();
        if (articlesObject.getArticles().size() > 0) {
            emailContents.setDigestArticles(articlesObject.getArticles().size());
            articlesObject.getArticles().sort(Comparator.comparing(ArticleDto::getArticleRank).reversed());
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
            //In case the search has not produced any result.
            emailContents.setDigestArticles(0);
            emailContents.setLstItems(new ArrayList<>());

        }
        // Use emailHTML class to build the email html. This should be passed to notification service.
        StringBuilder stringBuilder = emailHTMLMaker.constructEmail(emailContents);
        emailHtml = stringBuilder.toString();
        return emailHtml;
    }

    private static boolean sendEmail(SubscriptionDto subscription, String email) {
        EmailNotificationRequestDto emailObject = new EmailNotificationRequestDto();
        emailObject.setMessage(email);
        emailObject.setSender("Phenomics-Subscription-Service");
        emailObject.setSubject(subscription.getSearchName());
        emailObject.setToRecipients(Arrays.asList(subscription.getEmailId()));
        emailObject.setUniqueID(subscription.getSubscriptionId()+ ":" + LocalDateTime.now().toString());
        NotificationHandler.sendEmail(emailObject);
        return true;

    }


}
