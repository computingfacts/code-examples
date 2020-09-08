
package com.computingfacts.api.service;

import com.computingfacts.api.model.Disease;
import java.util.List;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;

/**
 *
 * @author joseph
 */
public interface ProteinApiService {
    
     ProteinGroupEntry proteinByAccession(String accession);
     List<Disease> diseasesByAccession(String accession);
}
