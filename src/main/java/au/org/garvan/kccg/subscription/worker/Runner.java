package au.org.garvan.kccg.subscription.worker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by ahmed on 20/12/17.
 */
public class Runner {
    private static int subscriptionTime = 2;

    public static void main(String[] args) {
        JSONArray subscriptions = PipelineHandler.getSubscriptions();

        //Filter subscription based on scheduling criteria
        JSONArray toBeProcessedSubscription = filterSubscriptions(subscriptions);
        //Process subscription in loop
        processSubscriptions(toBeProcessedSubscription);

    }

    private static JSONArray filterSubscriptions(JSONArray subscriptions) {
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

        return filteredSubscription;
    }

    private static void processSubscriptions(JSONArray filteredSubscriptions) {

        for (Object obj : filteredSubscriptions) {
            JSONObject jsonDoc = (JSONObject) obj;

            if (jsonDoc.containsKey("query")) {
                JSONArray articles = PipelineHandler.getArticles(prepareQuery(jsonDoc));
                prepareEmail(jsonDoc, articles);
//                updateSubscription(jsonDoc);
            }

        }

    }

    private static void prepareEmail(JSONObject subscription, JSONArray articles) {

    }

    private static void updateSubscription(JSONObject subscription) {
        long frequency = Long.parseLong(subscription.get("digestFrequencyInDays").toString());
        LocalDate nextRunDate = LocalDate.now().plusDays(frequency);
        LocalDateTime processTime = LocalDateTime.of(nextRunDate, LocalTime.of(subscriptionTime, 0, 0));

        String subscriptionId = subscription.get("subscriptionId").toString();
        String nextRunTime = processTime.atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME);
        long runDate = LocalDate.now().toEpochDay();
        PipelineHandler.updateSubscription(subscriptionId, runDate, nextRunTime);


    }

    private static JSONObject prepareQuery(JSONObject subscription) {

        long runDate = (long) (Double.parseDouble(subscription.get("lastRunDate").toString()));
        long today = LocalDate.now().toEpochDay();
        JSONObject dateRange = new JSONObject();
        dateRange.put("startDate", runDate);
        dateRange.put("endDate", today);


        JSONObject query = (JSONObject) subscription.get("query");
        query.put("dateRange", dateRange);
        return query;
    }

    private void init() {

    }


}
