package com.computingfacts.api.service;

import com.computingfacts.api.model.EnzymeModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
public interface EnzymeService {

    Optional<EnzymeEntry> getEnzyme(String ecNumber);

    EnzymeModel getEnzymeModel(String ecNumber);

    Page<EnzymeEntry> getEnzymeEntries(Pageable pageable);

    Page<EnzymeModel> allEnzymes(Pageable pageable);

    List<ProteinGroupEntry> getProteinsByEc(String ec, int limit);
}
