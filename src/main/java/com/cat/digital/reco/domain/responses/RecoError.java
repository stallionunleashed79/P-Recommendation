package com.cat.digital.reco.domain.responses;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import com.cat.digital.reco.common.CustomResponseCodes;
import com.cat.digital.reco.domain.models.AdditionalInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.http.HttpStatus;


@ApiModel(description = "Represents an error code, description, and detailed error message for a given endpoint. Every HTTP status code returns a different `code` value in the response based on the nature of the error that occurred. Also, one HTTP status code can return different `code` for different use cases. The below table shows mapping of different `Code`, 'HTTP Error Message' and `Description` for each HTTP status code. HTTP 405 error will be thrown when incorrect HTTP method is used for any endpoint.  | **HTTP Status Code** | **Code** | **HTTP Error Message** | **Description** | | ------------- |:-------------:|-------------:|-------------:| | `400` | `400.001` | Bad Request | Unknown error occurred. | | `400` | `400.002` | Bad Request | Threat detected: maximum allowed limit of request is exceeded. | | `400` | `400.006` | Bad Request | Missing required field(s). | | `400` | `400.008` | Bad Request | Invalid or missing request body. | | `400` | `400.009` | Bad Request | Body not allowed. | | `400` | `400.010` | Bad Request | XML schema is not valid. | | `401` | `401.001` | Unauthorized | Invalid access token. | | `401` | `401.002` | Unauthorized | Access token expired. | | `401` | `401.003` | Unauthorized | API product mismatch for token. | | `401` | `401.004` | Unauthorized | Insufficient scope for application. | | `401` | `401.005` | Unauthorized | Invalid API key for given resource. | | `401` | `401.006` | Unauthorized | Missing access token. | | `401` | `401.007` | Unauthorized | Invalid or expired access token. | | `401` | `401.008` | Unauthorized | User not authorized. | | `403` | `403.001` | Forbidden | Request not coming from valid IP. | | `403` | `403.002` | Forbidden | There was an error getting entitlements. | | `403` | `403.003` | Forbidden | ClientId: <ClientId> does not have permission for: <http method> <ResourceURL>.| | `404` | `404.001` | Not Found | Resource not found. | | `405` | `405.001` | Method Not Allowed | Method not allowed. | | `406` | `406.001` | Not Acceptable | Request not acceptable. The target resource does not have a current representation that would be acceptable to the user agent. | | `409` | `409.001` | Conflict | Request conflicts with current state. | | `413` | `413.001` | Payload Too Large | Payload too large. | | `415` | `415.001` | Unsupported Media Type | Unsupported media type. Requested payload format is not supported by this method on the target resource. The format problem might be due to the request's indicated content-type or content-encoding, or as a result of inspecting the data directly. | | `429` | `429.001` | Too Many Requests | Throttling rate limit exceeded. | | `429` | `429.002` | Too Many Requests | Quota rate limit exceeded. | | `500` | `500.001` | Internal Server Error | Server error. | | `500` | `500.002` | Internal Server Error | Shared flow not found. | | `500` | `500.003` | Internal Server Error | Script execution failed. | | `500` | `500.004` | Internal Server Error | Error occurred while getting response from backend. Please try later. | | `500` | `500.005` | Internal Server Error | Missing mandatory configuration properties. | | `500` | `500.006` | Internal Server Error | Unable to AssumeRole for authentication. | | `503` | `503.001` | Service Unavailable | The service is temporarily unavailable. | | `504` | `504.001` | Gateway Timeout | Request timed out. | ")
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecoError {

  public RecoError(final HttpStatus status, final String message) {
    this.status = status;
    this.code = Integer.toString(status.value());
    this.description = message;
  }

  public RecoError(CustomResponseCodes code, String message) {

    this.status = HttpStatus.valueOf(Integer.parseInt(code.getCode().split("\\.")[0]));
    this.code = code.getCode();
    this.description = message;
  }

  public RecoError(final HttpStatus status, String message, List<AdditionalInfo> error) {

    this.status = status;
    this.code = Integer.toString(status.value());
    this.description = message;
    this.details = error;
  }

  public RecoError(CustomResponseCodes code, String message, List<AdditionalInfo> error) {

    this.status = HttpStatus.valueOf(Integer.parseInt(code.getCode().split("\\.")[0]));
    this.code = code.getCode();
    this.description = message;
    this.details = error;
  }

  private HttpStatus status;

  @JsonProperty("code")
  @ApiModelProperty(example = "403", required = true, value = "Error Code represents an alpha-numeric error code received from the error.")
  @NotNull
  private String code;

  @JsonProperty("description")
  @ApiModelProperty(example = "Forbidden User", required = true, value = "Message represents a textual description of a given error code.")
  @NotNull
  private String description;

  @JsonProperty("details")
  private List<AdditionalInfo> details = null;


  public HttpStatus getStatus() {
    return status;
  }
}

