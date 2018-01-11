package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.json.simple.JSONObject;

/**
 * Created by ahmed on 11/1/18.
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

    public SubscriptionDto(){}


}
