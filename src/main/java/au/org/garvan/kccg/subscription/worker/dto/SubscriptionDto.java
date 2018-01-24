package au.org.garvan.kccg.subscription.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.binding.ObjectExpression;
import lombok.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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


    public List<String> getGenesInQuery(){
        List<String> geneSymbols = new ArrayList<>();

        if(query.get("gene")!=null){
            geneSymbols = ((ArrayList)((LinkedHashMap) query.get("gene")).get("symbols")) ;
        }
        return geneSymbols;
    }


}
