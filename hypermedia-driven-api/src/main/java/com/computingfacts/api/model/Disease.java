package com.computingfacts.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Disease {

    private String diseaseId;

    private String diseaseName;

    private String description;

    private String url;

    private List<String> diseaseEvidences;

}
