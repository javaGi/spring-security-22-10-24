package com.zoho.config;



import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;



/* after adding the spring security configuration, if you don't want to get the unauthorised problem,you can configure these codes

*/
@Configuration
public class SecurityConfig {

private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    Exception {

        //h(cd)2
        http.csrf().disable().cors().disable();

        /* if you want to get the Urls go through the do-filter method, you have to add this line.
         you are telling that Jwt filter should run first then Authentication should run.
         */
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        //haap
        /* this line of code is used to get opened for all the URLs  */
     //   http.authorizeHttpRequests().anyRequest().permitAll();
        http.authorizeHttpRequests().
                requestMatchers("/api/v/user/login","/api/v/user/signup")
                .permitAll()
                .anyRequest().authenticated();


        return http.build();
    }
}
