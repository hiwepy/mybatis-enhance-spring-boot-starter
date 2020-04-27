package org.apache.mybatis.dbperms;

import org.apache.ibatis.plugin.Plugin;
import org.apache.mybatis.dbperms.interceptor.DefaultDataPermissionStatementInterceptor;
import org.apache.mybatis.dbperms.parser.DefaultTablePermissionAutowireHandler;
import org.apache.mybatis.dbperms.parser.DefaultTablePermissionScriptHandler;
import org.apache.mybatis.dbperms.parser.ITablePermissionAutowireHandler;
import org.apache.mybatis.dbperms.parser.ITablePermissionScriptHandler;
import org.apache.mybatis.dbperms.parser.def.TablePermissionAnnotationParser;
import org.apache.mybatis.dbperms.parser.def.TablePermissionAutowireParser;
import org.apache.mybatis.dbperms.parser.def.TablePermissionScriptParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ Plugin.class, DefaultDataPermissionStatementInterceptor.class })
@EnableConfigurationProperties(MybatisDbpermsProperties.class)
public class MybatisDbpermsAutoConfiguration implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
    @Bean
    @ConditionalOnMissingBean
    public TablePermissionAutowireParser autowirePermissionParser() {
    	
    	TablePermissionAutowireParser autowirePermissionParser = new TablePermissionAutowireParser() {
    		
    		@Override
    		protected void internalInit() {
    			
    			super.internalInit();
    			
    			ITablePermissionAutowireHandler tablePermissionHandler = super.getTablePermissionHandler();
    			
    			try {
					if(null == tablePermissionHandler) {
						tablePermissionHandler = getApplicationContext().getBean(ITablePermissionAutowireHandler.class);
					}
				} catch (BeansException e) {
					System.err.println("需要自己实现ITablePermissionAutowireHandler接口");
					e.printStackTrace();
				}
    			if(null == tablePermissionHandler) {
    				tablePermissionHandler = new DefaultTablePermissionAutowireHandler((metaHandler, tableName) -> {
	            		return null;
	            	});
				}
    			
    			this.setTablePermissionHandler(tablePermissionHandler);
    		}
    		
    	};
    	 
    	return autowirePermissionParser;
    }
    
    @Bean
    public TablePermissionAnnotationParser annotationPermissionParser() {
    	return new TablePermissionAnnotationParser();
    }
    
    @Bean
    public TablePermissionScriptParser scriptPermissionParser() {
    	TablePermissionScriptParser scriptPermissionParser = new TablePermissionScriptParser() {
    		
    		@Override
    		protected void internalInit() {
    			
    			super.internalInit();
    			
    			ITablePermissionScriptHandler tablePermissionHandler = super.getTablePermissionHandler();
    			
    			try {
					if(null == tablePermissionHandler) {
						tablePermissionHandler = getApplicationContext().getBean(ITablePermissionScriptHandler.class);
					}
				} catch (BeansException e) {
					System.err.println("需要自己实现ITablePermissionScriptHandler接口");
					e.printStackTrace();
				}
    			if(null == tablePermissionHandler) {
    				tablePermissionHandler = new DefaultTablePermissionScriptHandler((metaHandler, tableName) -> {
	            		return null;
	            	});
				}
    			
    			this.setTablePermissionHandler(tablePermissionHandler);
    		}
    		
    	};
    	 
    	return scriptPermissionParser;
    }
    
    @Bean
    public DefaultDataPermissionStatementInterceptor dataPermissionStatementInterceptor(
    		TablePermissionAutowireParser autowirePermissionParser,
    		TablePermissionAnnotationParser annotationPermissionParser, 
    		@Autowired(required = false) TablePermissionScriptParser scriptPermissionParser) {
        return new DefaultDataPermissionStatementInterceptor(autowirePermissionParser, annotationPermissionParser, scriptPermissionParser);
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
}
