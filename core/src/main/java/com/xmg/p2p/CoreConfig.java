package com.xmg.p2p;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
@EnableTransactionManagement//开启事务相当于xml中的<annotation driven/>
@SpringBootApplication
@MapperScan({"com.xmg.p2p.base.mapper","com.xmg.p2p.business.mapper"})
@ImportResource("classpath:applicationContext-tx.xml")
@PropertySources({
        @PropertySource("classpath:email.properties")
})
public class CoreConfig {
    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource){//方法名txManager对应xml中的事务id
        return  new DataSourceTransactionManager(dataSource);
    }
    public static void main(String[] args) {

    }
}
