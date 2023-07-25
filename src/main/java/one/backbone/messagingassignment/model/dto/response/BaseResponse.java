package one.backbone.messagingassignment.model.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * The type Base response.
 */
@ToString
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"responseStatus", "responseMessage", "responseDescription", "responseTimestamp"})
public class BaseResponse {

    private int responseStatus;
    private String responseMessage;
    private String responseDescription;
    private Instant responseTimestamp;

}