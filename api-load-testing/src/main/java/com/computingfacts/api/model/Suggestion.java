package com.computingfacts.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Joseph
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Suggestion {

    @Json(name = "suggestion")
    @JsonProperty("suggestion")
    private String suggestedKeyword;

}
