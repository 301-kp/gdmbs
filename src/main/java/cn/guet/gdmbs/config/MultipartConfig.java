package cn.guet.gdmbs.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {
    @Value("${projectPath:}")
    private String projectPath;

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = StringUtils.isNotBlank(projectPath) ? projectPath + "/data/tmp/" : "/data/tmp/";
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
