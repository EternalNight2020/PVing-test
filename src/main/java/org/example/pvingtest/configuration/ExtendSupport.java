package org.example.pvingtest.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @package: org.example.pvingtest.configuration
 * @className: ExtendSupport
 * @author: alexwang
 * @description: TODO
 * @date: 2024/5/26 17:23
 */
@Configuration
public class ExtendSupport extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
