package com.fieldright.fr.swagger;

import com.fieldright.fr.security.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile(value = {"heroku", "localhost", "test"})
@AllArgsConstructor
public class SwaggerConfig {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.fieldright.fr"))
                .paths(PathSelectors.any()).build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Field Right API")
                .description("Documentação da FieldRight API")
                .version("1.0")
                .build();
    }

    @Bean
    public SecurityConfiguration security(){
        String token;
        try{
            UserDetails userDetails = this.userDetailsService.loadUserByUsername("development@swagger.user");
            token = this.jwtTokenUtil.getToken(userDetails);
        }catch (Exception e) {
            token = "";
        }

        return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER,
                "Authorization",",");
    }
}
