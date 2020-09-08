package com.computingfacts.api.config;

import com.computingfacts.api.controller.ProteinController;
import com.computingfacts.api.hateoas.ProteinModelAssembler;
import com.computingfacts.api.model.ProteinModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.COLLECTION_JSON;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL_FORMS;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.UBER;

/**
 *
 * @author joseph
 */
@EnableHypermediaSupport(type = {HAL, HAL_FORMS, COLLECTION_JSON, UBER})
@Configuration
public class HypermediaConfig {

    @Bean
    public ProteinModelAssembler proteinModelAssembler() {
        return new ProteinModelAssembler(ProteinController.class, ProteinModel.class);
    }

}
