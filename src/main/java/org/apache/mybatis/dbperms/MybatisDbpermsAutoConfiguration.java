package org.apache.mybatis.dbperms;

import org.apache.ibatis.plugin.Plugin;
import org.apache.mybatis.dbperms.interceptor.DataPermissionStatementInterceptor;
import org.apache.mybatis.dbperms.parser.DefaultTablePermissionAnnotationHandler;
import org.apache.mybatis.dbperms.parser.DefaultTablePermissionAutowireHandler;
import org.apache.mybatis.dbperms.parser.ITablePermissionAnnotationHandler;
import org.apache.mybatis.dbperms.parser.ITablePermissionAutowireHandler;
import org.apache.mybatis.dbperms.parser.TablePermissionAnnotationParser;
import org.apache.mybatis.dbperms.parser.TablePermissionAutowireParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ Plugin.class, DataPermissionStatementInterceptor.class })
@ConditionalOnProperty(prefix = MybatisDbpermsProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties(MybatisDbpermsProperties.class)
public class MybatisDbpermsAutoConfiguration {
	
    @Bean
    @ConditionalOnMissingBean
    public ITablePermissionAutowireHandler tablePermissionAutowireHandler() {
    	return new DefaultTablePermissionAutowireHandler((metaHandler, tableName) -> {
    		return null;
    	});
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ITablePermissionAnnotationHandler tablePermissionAnnotationHandler() {
    	return new DefaultTablePermissionAnnotationHandler();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public TablePermissionAutowireParser autowirePermissionParser(ITablePermissionAutowireHandler tablePermissionHandler) {
    	TablePermissionAutowireParser autowirePermissionParser = new TablePermissionAutowireParser();
    	autowirePermissionParser.setTablePermissionHandler(tablePermissionHandler);
    	return autowirePermissionParser;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public TablePermissionAnnotationParser annotationPermissionParser(ITablePermissionAnnotationHandler tablePermissionHandler) {
    	TablePermissionAnnotationParser annotationPermissionParser = new TablePermissionAnnotationParser();
    	annotationPermissionParser.setTablePermissionHandler(tablePermissionHandler);
    	return annotationPermissionParser;
    }
    
    @Bean
    public DataPermissionStatementInterceptor dataPermissionStatementInterceptor(
    		TablePermissionAutowireParser autowirePermissionParser,
    		TablePermissionAnnotationParser annotationPermissionParser) {
        return new DataPermissionStatementInterceptor(autowirePermissionParser, annotationPermissionParser);
    }
	
}
