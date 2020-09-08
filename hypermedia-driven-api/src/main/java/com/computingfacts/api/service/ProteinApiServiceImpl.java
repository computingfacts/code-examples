package com.computingfacts.api.service;

import com.computingfacts.api.model.Disease;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.dataservice.dto.DiseaseView;
import uk.ac.ebi.ep.dataservice.dto.ProteinData;
import uk.ac.ebi.ep.dataservice.service.DataService;
import uk.ac.ebi.ep.indexservice.model.protein.Fields;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
@Service
public class ProteinApiServiceImpl implements ProteinApiService {

    private final DataService dataService;

    public ProteinApiServiceImpl(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public ProteinGroupEntry proteinByAccession(String accession) {
        ProteinData protein = dataService.findProteinByAccession(accession);

        ProteinGroupEntry entry = new ProteinGroupEntry();
        Fields field = new Fields();
        field.setPrimaryAccession(Arrays.asList(protein.getAccession()));
        field.setPrimaryOrganism(Arrays.asList(protein.getCommonName()));
        field.setName(Arrays.asList(protein.getProteinName()));

        entry.setFields(field);

        return entry;
    }

    @Override
    public List<Disease> diseasesByAccession(String accession) {

        List<DiseaseView> diseases = dataService.findDiseaseViewByAccession(accession);
        return diseases.stream()
                .map(this::toEnzymeDisease)
                .collect(Collectors.toList());

    }

    private Disease toEnzymeDisease(DiseaseView disease) {

        return Disease.builder()
                .diseaseId(disease.getOmimNumber())
                .diseaseName(disease.getDiseaseName())
                .description(disease.getDescription())
                .url(disease.getUrl())
                .diseaseEvidences(disease.getEvidences())
                .build();
    }
}
