package com.computingfacts.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Singular;

/**
 *
 * @author Joseph
 */
@Data
public class Autocomplete {
    @Json(name = "suggestions")
    @Singular
    @JsonProperty("suggestions")
    private final List<Suggestion> suggestions = new ArrayList<>();

}
