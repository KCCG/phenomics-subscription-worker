package au.org.garvan.kccg.subscription.worker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by ahmed on 20/12/17.
 */
public class Runner {
    private static final Logger slf4jLogger = LoggerFactory.getLogger(Runtime.class);

    private static int subscriptionTime = 2;

    public static void main(String[] args) {
        slf4jLogger.info(String.format("Initializing Runner. TimeStamp: %s", LocalDateTime.now().toString()));
        JSONArray subscriptions = PipelineHandler.getSubscriptions();
        slf4jLogger.info(String.format("Fetched Subscriptions. Count: %d", subscriptions.size()));

        //Filter subscription based on scheduling criteria
        if(subscriptions.size()>0)
        {

            JSONArray toBeProcessedSubscription = filterSubscriptions(subscriptions);
            //Process subscription in loop
            processSubscriptions(toBeProcessedSubscription);
        }
        else
        {
            slf4jLogger.info(String.format("There is not registered subscription. "));

        }


    }

    private static JSONArray filterSubscriptions(JSONArray subscriptions) {
        slf4jLogger.info(String.format("Filtering Subscriptions. Count: %d", subscriptions.size()));
        JSONArray filteredSubscription = new JSONArray();
        for (Object obj : subscriptions) {
            JSONObject jsonDoc = (JSONObject) obj;
            if (jsonDoc.containsKey("lastRunDate")) {
                long runDate = (long) (Double.parseDouble(jsonDoc.get("lastRunDate").toString()));
                long frequency = Long.parseLong(jsonDoc.get("digestFrequencyInDays").toString());
                long today = LocalDate.now().toEpochDay();
                if (today - runDate >= frequency)
                    filteredSubscription.add(jsonDoc);

            }
        }

        slf4jLogger.info(String.format("Filtering Subscriptions done. Final Count: %d", filteredSubscription.size()));

        return filteredSubscription;
    }

    private static void processSubscriptions(JSONArray filteredSubscriptions) {

        int index = 1; int total = filteredSubscriptions.size();
        slf4jLogger.info(String.format("Processing Subscriptions. Count: %d", total));
        for (Object obj : filteredSubscriptions) {
            JSONObject jsonDoc = (JSONObject) obj;
            String subscriptionId = jsonDoc.get("subscriptionId").toString();
            slf4jLogger.info(String.format("Processing Subscription. Id: %s, Progress:%d/%d", subscriptionId,index,total));
            if (jsonDoc.containsKey("query")) {
                JSONArray articles = PipelineHandler.getArticles(prepareQuery(subscriptionId, jsonDoc));
                slf4jLogger.info(String.format("Articles fetched for Subscription. Id: %s, Articles:%d", subscriptionId,articles.size()));
                prepareEmail(subscriptionId, jsonDoc, articles);
                updateSubscription(subscriptionId, jsonDoc);
            }

        }

    }

    private static void prepareEmail(String subscriptionId, JSONObject subscription, JSONArray articles) {
        slf4jLogger.info(String.format("Preparing email for Subscription. Id: %s. ",subscriptionId));
        slf4jLogger.info(String.format("Email processed for Subscription. Id: %s. ",subscriptionId));

    }

    private static void updateSubscription(String subscriptionId, JSONObject subscription) {
        slf4jLogger.info(String.format("Updating running information for Subscription. Id: %s. ",subscriptionId));

        long frequency = Long.parseLong(subscription.get("digestFrequencyInDays").toString());
        LocalDate nextRunDate = LocalDate.now().plusDays(frequency);
        LocalDateTime processTime = LocalDateTime.of(nextRunDate, LocalTime.of(subscriptionTime, 0, 0));

        String nextRunTime = processTime.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME);
        long runDate = LocalDate.now().toEpochDay();
        PipelineHandler.updateSubscription(subscriptionId, runDate, nextRunTime);
        slf4jLogger.info(String.format("Updated Subscription. Id: %s. ",subscriptionId));

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

    private void init() {

    }


}
