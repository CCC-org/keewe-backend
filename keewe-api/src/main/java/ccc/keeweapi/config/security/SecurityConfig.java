package ccc.keeweapi.config.security;

import ccc.keeweapi.config.security.jwt.JwtAuthenticationEntryPoint;
import ccc.keeweapi.config.security.jwt.JwtAuthenticationFilter;
import ccc.keeweapi.config.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final String[] SWAGGER_URL = {"/", "/docs/openapi3.yaml", "/favicon.ico"};
    private final String SIGNUP_URL = "/api/v1/user/**";
    private final String HEALTH_CHECK_URL = "/api/health-check";

    private final UserDetailsService userService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUtils jwtUtils;

    //FIXME 보안 무시 URL
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(HttpMethod.GET, SWAGGER_URL)
                .antMatchers(HttpMethod.GET, SIGNUP_URL)
                .antMatchers(HttpMethod.GET, HEALTH_CHECK_URL)
                .antMatchers(HttpMethod.GET, "/api/v1/oauth/**")
                .antMatchers("/h2-console/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(http)
                                , jwtAuthenticationEntryPoint
                                , jwtUtils)
                        , BasicAuthenticationFilter.class)
                .build();
    }

}
