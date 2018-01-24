package au.org.garvan.kccg.subscription.worker.dto;

/**
 * Created by ahmed on 15/1/18.
 */


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
@Data
public class EmailNotificationRequestDto implements Serializable {

    private static final long serialVersionUID = -1528423248799532018L;

    @JsonProperty("toRecipients")
    private List<String> toRecipients;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("uniqueID")
    private String uniqueID;




}
