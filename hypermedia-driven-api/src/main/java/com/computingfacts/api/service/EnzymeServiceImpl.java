package com.computingfacts.api.service;

import com.computingfacts.api.model.EnzymeModel;
import com.computingfacts.api.model.ProteinModel;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.indexservice.helper.IndexFields;
import uk.ac.ebi.ep.indexservice.helper.IndexQueryType;
import uk.ac.ebi.ep.indexservice.helper.QueryBuilder;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.indexservice.service.ApiIndexService;

/**
 *
 * @author joseph
 */
@Service
class EnzymeServiceImpl implements EnzymeService {

    private static final String RELEVANCE = "_relevance";
    private static final String FORMAT = "json";
    private static final String DOMAIN_QUERY = "domain_source:enzymeportal_enzymes";
    // private static final String ENTRY_TYPE_ASC = "entry_type:ascending";

    private final ApiIndexService apiIndexService;

    public EnzymeServiceImpl(ApiIndexService apiIndexService) {
        this.apiIndexService = apiIndexService;
    }

    private EnzymeEntry getEnzymeEntry(String ecNumber) {
        String query = String.format("%s%s", IndexQueryType.ID.getQueryType(), ecNumber);
        int startPage = 0;
        int pageSize = 1;
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(), IndexFields.alt_names.name(),
                IndexFields.description.name(), IndexFields.enzyme_family.name(), IndexFields.intenz_cofactors.name(), IndexFields.catalytic_activity.name());

        QueryBuilder queryBuilder = getQueryBuilder(startPage, pageSize, query, fieldList);

        return apiIndexService.getEnzymeEntry(queryBuilder);
    }

    @Override
    public Optional<EnzymeEntry> getEnzyme(String ecNumber) {

        return Optional.ofNullable(getEnzymeEntry(ecNumber));

    }

    @Override
    public EnzymeModel getEnzymeModel(String ecNumber) {

        return enzymeEntryToEnzymeModel(getEnzymeEntry(ecNumber));
    }

    @Override
    public Page<EnzymeModel> allEnzymes(Pageable pageable) {

        String query = DOMAIN_QUERY;
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name());

        QueryBuilder queryBuilder = getQueryBuilder(pageable.getPageNumber(), pageable.getPageSize(), query, fieldList);
        int hitCount = apiIndexService.getEnzymeHitCount(queryBuilder);
        List<EnzymeModel> enzymes = apiIndexService.getEnzymeEntries(queryBuilder)
                .stream()
                .map(enzyme -> enzymeEntryToEnzymeModel(enzyme))
                .collect(Collectors.toList());

        return new PageImpl(enzymes, pageable, hitCount);

    }

    @Override
    public Page<EnzymeEntry> getEnzymeEntries(Pageable pageable) {

        String query = DOMAIN_QUERY;
        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name());

        QueryBuilder queryBuilder = getQueryBuilder(pageable.getPageNumber(), pageable.getPageSize(), query, fieldList);
        int hitCount = apiIndexService.getEnzymeHitCount(queryBuilder);
        List<EnzymeEntry> enzymes = apiIndexService.getEnzymeEntries(queryBuilder);
        return new PageImpl(enzymes, pageable, hitCount);

    }

    @Override
    public List<ProteinGroupEntry> getProteinsByEc(String ec, int limit) {
        int startPage = 0;
        String query = String.format("%s%s", IndexQueryType.EC.getQueryType(), ec);

        List<String> fieldList = Arrays.asList(IndexFields.id.name(), IndexFields.name.name(),
                IndexFields.primary_accession.name(), IndexFields.primary_organism.name(),
                IndexFields.primary_image.name(), IndexFields.function.name(), IndexFields.disease_name.name(), IndexFields.catalytic_activity.name(),
                IndexFields.alt_names.name(), IndexFields.gene_name.name(), IndexFields.ec.name());

        QueryBuilder queryBuilder = getQueryBuilder(startPage, limit, query, fieldList);
        return apiIndexService.getProteinGroupEntry(queryBuilder);

    }

    private QueryBuilder getQueryBuilder(int startPage, int pageSize, String query, List<String> fieldList) {
        return QueryBuilder
                .builder()
                .query(query)
                .start(startPage * pageSize)
                .size(pageSize)
                .fields(fieldList)
                .sort(RELEVANCE)
                .reverse(Boolean.TRUE)
                .format(FORMAT)
                .build();
    }

    private EnzymeModel enzymeEntryToEnzymeModel(EnzymeEntry enzymeEntry) {

        return EnzymeModel.builder()
                .alternativeNames(enzymeEntry.getFields().getAltNames())
                .catalyticActivities(enzymeEntry.getFields().getCatalyticActivity())
                .ecNumber(enzymeEntry.getId())
                .enzymeName(enzymeEntry.getEnzymeName())
                .cofactors(enzymeEntry.getIntenzCofactors())
                .associatedProteins(toProteinCollectionModel(enzymeEntry.getProteinGroupEntry()))
                .build();
    }

    private ProteinModel toProteinModel(ProteinGroupEntry protein) {

        return ProteinModel.builder()
                .accession(protein.getPrimaryAccession())
                .proteinName(protein.getProteinName())
                .organismName(protein.getPrimaryOrganism())
                .build();
    }

    private CollectionModel<ProteinModel> toProteinCollectionModel(List<ProteinGroupEntry> proteinList) {

        return proteinList.stream()
                .map(protein -> toProteinModel(protein))
                .collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));

    }

}
