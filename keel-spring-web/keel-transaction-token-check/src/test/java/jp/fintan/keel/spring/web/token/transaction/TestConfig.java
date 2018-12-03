package jp.fintan.keel.spring.web.token.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

@Configuration
public class TestConfig implements WebMvcConfigurer {

    @Bean
    public RequestDataValueProcessor requestDataValueProcessor() {
        return new TransactionTokenRequestDataValueProcessor();
    }

    @Bean
    public TransactionTokenInterceptor transactionTokenInterceptor() {
        return new TransactionTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(transactionTokenInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**");
    }
}
