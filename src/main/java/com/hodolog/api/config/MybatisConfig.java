package com.hodolog.api.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages = {"com.hodolog.mapper"})
public class MybatisConfig {
	@Value("${spring.datasource.driver-class-name}") private String driverClassName;
	@Value("${spring.datasource.url}") private String dbUrl;
	@Value("${spring.datasource.username}") private String username;
	@Value("${spring.datasource.password}") private String password;

    @Bean
    DataSource batisDataSource() {
		HikariConfig dataSourceConfig = new HikariConfig();
		dataSourceConfig.setDriverClassName(driverClassName);
		dataSourceConfig.setJdbcUrl(dbUrl);
		dataSourceConfig.setUsername(username);
		dataSourceConfig.setPassword(password);
		dataSourceConfig.setMaximumPoolSize(10);
		dataSourceConfig.setMinimumIdle(5);
		dataSourceConfig.setMaxLifetime(1200000);
		dataSourceConfig.setConnectionTimeout(20000);
		dataSourceConfig.setIdleTimeout(300000);
		
		return new HikariDataSource(dataSourceConfig);
	}
	
	@Bean(name = "batisSqlSessionFactory")
	SqlSessionFactory sqlSessionFactory(@Qualifier("batisDataSource") DataSource batisDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(batisDataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*Mapper.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "batisSqlSessionTemplate")
	SqlSessionTemplate batisSqlSessionTemplate(SqlSessionFactory batisSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(batisSqlSessionFactory);

	}
}