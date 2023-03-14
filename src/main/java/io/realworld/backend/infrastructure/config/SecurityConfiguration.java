package io.realworld.backend.infrastructure.config;

import io.realworld.backend.infrastructure.security.JwtTokenFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private final JwtTokenFilter jwtTokenFilter;

  @Autowired
  public SecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
    this.jwtTokenFilter = jwtTokenFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Enable CORS and disable CSRF
    http =
        http.cors()
            .configurationSource(
                request -> {
                  var cors = new CorsConfiguration();
                  cors.setAllowedOrigins(
                      List.of("http://localhost:8080", "https://editor.swagger.io"));
                  cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                  cors.setAllowedHeaders(List.of("*"));
                  return cors;
                })
            .and()
            .csrf()
            .disable();

    // Set session management to stateless
    http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

    // Set unauthorized requests exception handler
    http =
        http.exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and();

    // Set permissions on endpoints
    http.authorizeRequests()
        .antMatchers("/", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/api-docs")
        .permitAll()
        // Our public endpoints
        .antMatchers(HttpMethod.POST, "/api/users", "/api/users/login")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/articles/**", "/api/profiles/**", "/api/tags")
        .permitAll()
            // Our private endpoints
            // All logged-in users
            .antMatchers(
                    HttpMethod.GET,
                    "/api/articles/favourites",
                    "/api/articles/feed",
                    "/api/articles/*/comments",
                    "/api/user")
            .authenticated()
            .antMatchers(HttpMethod.PUT, "/api/user")
            .authenticated()
            .antMatchers(
                    HttpMethod.POST,
                    "/api/articles/*/comments",
                    "/api/articles/*/favorite",
                    "/api/profiles/*/follow")
            .authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/articles/*/favorite", "/api/profiles/*/follow")
            .authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/articles/{slug}/comments/{id}")
            .access("@userSecurity.checkAuthority(authentication,#slug,#id)")

            // By admin and writer only
            .antMatchers(HttpMethod.POST, "/api/articles")
            .hasAuthority("hasWriterAccess")
            .antMatchers(HttpMethod.DELETE, "/api/articles/{slug}")
            .access("@userSecurity.checkAuthority(authentication,#slug,-1)")
            .antMatchers(HttpMethod.PUT, "/api/articles/{slug}")
            .access("@userSecurity.checkAuthority(authentication,#slug,-1)")

            // By Admin only
            .antMatchers(HttpMethod.GET, "/api/users")
            .hasAuthority("hasAdminAccess")
            .antMatchers(HttpMethod.POST, "/api/notifications")
            .hasAuthority("hasAdminAccess")
            .anyRequest()
            .authenticated();
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
