package com.zzq.beauty.conf;

import com.zzq.beauty.beetl.CareBuyGoodsFunction;
import com.zzq.beauty.beetl.GoodsJSONFunction;
import com.zzq.beauty.beetl.LastCareDateFunction;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;

/**
 * beetlConf模板引擎配置文件
 */
@Configuration
public class BeetlConf {
	@Autowired ApplicationContext applicationContext;
	@Bean(name = "beetlConfig")
	public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
		BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
		ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
		try {
			// WebAppResourceLoader 配置root路径是关键
			WebAppResourceLoader webAppResourceLoader =
					new WebAppResourceLoader(patternResolver.getResource("classpath:/").getFile().getPath());
			beetlGroupUtilConfiguration.setResourceLoader(webAppResourceLoader);
			beetlGroupUtilConfiguration.init();

		} catch (IOException e) {
			e.printStackTrace();
		}
		//读取配置文件信息
		return beetlGroupUtilConfiguration;
	}
	@Bean(name = "beetlViewResolver")
	public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
		BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
		//beetlSpringViewResolver.setPrefix("WEB-INF/views/");
		beetlSpringViewResolver.setSuffix(".html");
		beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
		beetlSpringViewResolver.setOrder(0);
		beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
		/**
		 * 注册 beetl function tag
		 */
		GroupTemplate groupTemplate = new GroupTemplate();
		groupTemplate.registerFunction("goodsJSONFormat",applicationContext.getBean(GoodsJSONFunction.class));//
		groupTemplate.registerFunction("careBuyGoodsFormat",applicationContext.getBean(CareBuyGoodsFunction.class));
		groupTemplate.registerFunction("lastCareDate",applicationContext.getBean(LastCareDateFunction.class));

		beetlSpringViewResolver.setGroupTemplate(groupTemplate);
		return beetlSpringViewResolver;
	}


}
