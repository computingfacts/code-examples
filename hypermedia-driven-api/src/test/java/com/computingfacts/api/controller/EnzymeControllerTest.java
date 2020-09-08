package com.computingfacts.api.controller;

import static org.hamcrest.CoreMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author joseph
 */
@SpringBootTest
@AutoConfigureMockMvc
public class EnzymeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void enzymes() throws Exception {
       
        
        mvc.perform(get("/enzymes/1.1.1.1").accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$.enzymeName", is("Alcohol dehydrogenase")))
                .andExpect(jsonPath("$.ecNumber", is("1.1.1.1")))
                .andExpect(jsonPath("$.enzymeFamily", is("Oxidoreductases")))
                .andExpect(jsonPath("$.alternativeNames[0]", is("Aldehyde reductase.")))
                .andExpect(jsonPath("$.cofactors[0]", is("Zn(2+) or Fe cation.")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/enzymes/1.1.1.1")))
                .andExpect(jsonPath("$._links.enzymes.href", is("http://localhost/enzymes/?page=0&size=10")))
                .andExpect(jsonPath("$._links.['associated Proteins'].href", is("http://localhost/enzymes/1.1.1.1/proteins?limit=10")))
                .andReturn();
    }

}
