
package com.computingfacts.pooling.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author joseph
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationOperationTest {
    
    @Autowired
    private ApplicationOperation operation;
 
    /**
     * Test of doSomething method, of class ApplicationOperation.
     * @throws java.lang.Exception
     */
    @Test
    public void testDoSomething() throws Exception {
        operation.doSomething(false);
    }


    
}
