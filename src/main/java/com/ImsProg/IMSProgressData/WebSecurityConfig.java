package com.ImsProg.IMSProgressData;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public WebSecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }
    

   @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider =
            new ActiveDirectoryLdapAuthenticationProvider("ad.integratedmfg.com", "ldap://192.168.12.35:389");

        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .authenticationProvider(activeDirectoryProvider())
            .requestCache(requestCache -> requestCache
                .requestCache(customRequestCache())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/imsProg/success", true) // force redirect here
            )
            .authenticationManager(authenticationManager()) // Configure custom authentication manager
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(restAuthenticationEntryPoint) // 👈 Tie it in here
            )
            .csrf(csrf -> csrf.disable())
            ;

        return http.build();
    }
    @Bean
    public ProviderManager authenticationManager() {
        // Create a ProviderManager with the ActiveDirectoryLdapAuthenticationProvider
        return new ProviderManager(activeDirectoryProvider());
    }


    @Bean
    public RequestCache customRequestCache() {
        return new CustomRequestCache();
    }
    
}
