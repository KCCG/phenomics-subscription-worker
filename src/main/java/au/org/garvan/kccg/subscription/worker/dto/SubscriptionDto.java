package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.binding.ObjectExpression;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ahmed on 11/1/18.
 * Modified with generic concepts 26/1/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SubscriptionDto {

    @JsonProperty
    String subscriptionId;
    @JsonProperty
    JSONObject query;
    @JsonProperty
    String emailId;
    @JsonProperty
    String searchName;
    @JsonProperty
    Integer digestFrequencyInDays;
    @JsonProperty
    String nextRunTime;
    @JsonProperty
    Integer lastRunDate;

    public SubscriptionDto() {
    }


    //Query has been updated with generic search items.
    public List<ConceptDto> getSearchItems() {
        List<ConceptDto> searchItems = new ArrayList<>();

        if (query.containsKey("searchItems") && query.get("searchItems") != null) {
            ArrayList<Object> conceptsArray = (ArrayList<Object>) query.get("searchItems");

            for (Object obj : conceptsArray) {
                HashMap filter = (HashMap) obj;
                ConceptDto tempConcept = new ConceptDto();
                tempConcept.setType(filter.get("type").toString());
                tempConcept.setId(filter.get("id").toString());
                searchItems.add(tempConcept);
            }
        }
        return searchItems;
    }


}
