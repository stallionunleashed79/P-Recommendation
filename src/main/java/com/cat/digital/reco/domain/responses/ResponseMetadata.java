package com.cat.digital.reco.domain.responses;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

/**
 * The response metadata object contains the next cursor token for paginating the result set.
 */
@ApiModel(description = "The response metadata object contains the next cursor token for paginating the result set.")
@Validated
@Builder
public class ResponseMetadata {
  @JsonProperty("nextCursor")
  private String nextCursor = null;

  public ResponseMetadata nextCursor(String nextCursor) {
    this.nextCursor = nextCursor;
    return this;
  }

  /**
   * Next Cursor - string token that can be used to retrieve next query results page. No value is returned if current page is the last one.
   *
   * @return nextCursor
   **/
  @ApiModelProperty(example = "ewogICJyYW5kb20iOiAib2JqZWN0IiwKICAiZW5jb2RlZCI6ICJ0byIKICAiYmFzZSI6IDY0Cn0=", value = "Next Cursor - string token that can be used to retrieve next query results page. No value is returned if current page is the last one. ")

  public String getNextCursor() {
    return nextCursor;
  }

  public void setNextCursor(String nextCursor) {
    this.nextCursor = nextCursor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseMetadata responseMetadata = (ResponseMetadata) o;
    return Objects.equals(this.nextCursor, responseMetadata.nextCursor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nextCursor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseMetadata {\n");

    sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

