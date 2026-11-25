package hu.laci.learning.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A JSONPlaceholder "post" erőforrásának Java modellje.
 * A Jackson ezt a recordot alakítja JSON-ná és vissza.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Post(Integer userId, Integer id, String title, String body) {}
