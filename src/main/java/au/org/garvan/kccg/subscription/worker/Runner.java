package au.org.garvan.kccg.subscription.worker;

import au.org.garvan.kccg.subscription.worker.dto.SearchResponseDto;
import au.org.garvan.kccg.subscription.worker.dto.SubscriptionDto;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 20/12/17.
 */
public class Runner {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(Runtime.class);

    private static int subscriptionTime = 3;
    @Getter
    private static AppConfig config;

    static {
        init();
    }

    public static void main(String[] args) {
        slf4jLogger.info(String.format("Initializing Runner. TimeStamp: %s", LocalDateTime.now().toString()));
        List<SubscriptionDto> subscriptions = PipelineHandler.getSubscriptions();
        slf4jLogger.info(String.format("Fetched Subscriptions. Count: %d", subscriptions.size()));

        //Filter subscription based on scheduling criteria
        if(subscriptions.size()>0)
        {

            List<SubscriptionDto> toBeProcessedSubscription = filterSubscriptions(subscriptions);
            //Process subscription in loop
            processSubscriptions(toBeProcessedSubscription);
        }
        else
        {
            slf4jLogger.info(String.format("There is not registered subscription. "));

        }


    }

    private static List<SubscriptionDto> filterSubscriptions(List<SubscriptionDto> subscriptions) {
        slf4jLogger.info(String.format("Filtering Subscriptions. Count: %d", subscriptions.size()));
        List<SubscriptionDto> filteredSubscription = new ArrayList<>();
        for (SubscriptionDto obj : subscriptions) {
                long runDate = (long) (Double.parseDouble(obj.getLastRunDate().toString()));
                long frequency = Long.parseLong(obj.getDigestFrequencyInDays().toString());
                long today = LocalDate.now().toEpochDay();
                if (today - runDate >= frequency)
                    filteredSubscription.add(obj);

        }

        slf4jLogger.info(String.format("Filtering Subscriptions done. Final Count: %d", filteredSubscription.size()));
        return filteredSubscription;
    }

    private static void processSubscriptions(List<SubscriptionDto> filteredSubscriptions) {

        int index = 1; int total = filteredSubscriptions.size();
        slf4jLogger.info(String.format("Processing Subscriptions. Count: %d", total));
        for (SubscriptionDto currentSubscription : filteredSubscriptions) {
            String subscriptionId = currentSubscription.getSubscriptionId();
            slf4jLogger.info(String.format("Processing Subscription. Id: %s, Progress:%d/%d", subscriptionId,index,total));
            if (currentSubscription.getQuery()!=null) {
                SearchResponseDto paginatedArticles = PipelineHandler.getArticles(prepareQuery(subscriptionId, currentSubscription.getQuery()));
                slf4jLogger.info(String.format("Articles fetched for Subscription. Id: %s, Articles:%d", subscriptionId,paginatedArticles.getArticles().size()));
                prepareEmail(currentSubscription, paginatedArticles);
                updateSubscription(currentSubscription);
            }

        }

    }

    private static void prepareEmail(SubscriptionDto subscription, SearchResponseDto paginatedArticles) {
        slf4jLogger.info(String.format("Preparing email for Subscription. Id: %s. ",subscription.getSubscriptionId()));

        slf4jLogger.info(String.format("Email processed for Subscription. Id: %s. ",subscription.getSubscriptionId()));

    }

    private static void updateSubscription(SubscriptionDto subscription) {
        slf4jLogger.info(String.format("Updating running information for Subscription. Id: %s. ",subscription.getSubscriptionId()));

        long frequency = Long.parseLong(subscription.getDigestFrequencyInDays().toString());
        LocalDate nextRunDate = LocalDate.now().plusDays(frequency);
        LocalDateTime processTime = LocalDateTime.of(nextRunDate, LocalTime.of(subscriptionTime, 0, 0));

        String nextRunTime = processTime.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME);
        long runDate = LocalDate.now().toEpochDay();
        PipelineHandler.updateSubscription(subscription.getSubscriptionId(), runDate, nextRunTime);
        slf4jLogger.info(String.format("Updated Subscription. Id: %s. ",subscription.getSubscriptionId()));

    }

    private static JSONObject prepareQuery(String subscriptionId, JSONObject subscription) {

        long runDate = (long) (Double.parseDouble(subscription.get("lastRunDate").toString()));
        long today = LocalDate.now().toEpochDay();
        JSONObject dateRange = new JSONObject();
        dateRange.put("startDate", runDate);
        dateRange.put("endDate", today);


        JSONObject query = (JSONObject) subscription.get("query");
        query.put("dateRange", dateRange);
        slf4jLogger.info(String.format("Injecting date range %s in query for Subscription. Id: %s. ",dateRange.toJSONString(), subscriptionId));

        return query;
    }


    public static void init(){
        config = null;
        Yaml yaml = new Yaml();

        String configFile="";
        String ENV = System.getenv("ENV");
        if (ENV==null || ENV.isEmpty())
            ENV = "Local";

        switch(ENV) {
            case "Local":
                configFile =  "application.yml";
                break;
            case "DEV":
                configFile =  "application-dev.yml";
                break;
            case "PROD":
                configFile =  "application-prod.yml";
                break;
        }
        try(InputStream in = ClassLoader.getSystemResourceAsStream("config/" + configFile)) {
            config = yaml.loadAs(in, AppConfig.class);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
