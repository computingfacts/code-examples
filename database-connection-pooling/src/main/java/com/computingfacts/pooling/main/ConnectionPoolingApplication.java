package com.computingfacts.pooling.main;

import com.computingfacts.pooling.config.ConfigProperties;
import com.computingfacts.pooling.dataconfig.PoolConfig;
import com.computingfacts.pooling.service.ApplicationOperation;
import com.computingfacts.pooling.service.ApplicationOperationImpl;
import java.sql.SQLException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConnectionPoolingApplication {

    public static void main(String[] args) throws SQLException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(ConfigProperties.class);
        context.register(ApplicationOperationImpl.class);
        context.register(PoolConfig.class);

        context.scan("com.computingfacts.pooling");
        context.refresh();

        PoolConfig pool = context.getBean(PoolConfig.class);

        ApplicationOperation service = context.getBean(ApplicationOperation.class);

        service.doSomething(true);

    }

}
