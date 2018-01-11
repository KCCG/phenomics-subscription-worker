package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import au.org.garvan.kccg.subscription.worker.models.EmailContentsMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by ahmed on 10/1/18.
 */
public class EmailGenerator {

    public static void prepapreAndSendEmail(SubscriptionDto subscription, SearchResponseDto articles) {
        String email = prepareEmail(subscription, articles);
        sendEmail(subscription, email);
    }

    private static String prepareEmail(SubscriptionDto subscription, SearchResponseDto articlesObject) {
        String emailHtml="";
        EmailContentsMain emailContents = new EmailContentsMain();

//        if(articles.size()>0)
//        {
//
//        }
//



        return emailHtml;
    }

    private static boolean sendEmail(SubscriptionDto subscription, String email) {



        return true;

    }


}
