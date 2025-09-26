package com.valcare.parking.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // optional: disable CSRF for Swagger/testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html"
                ).permitAll()  // allow Swagger endpoints
                .anyRequest().permitAll() // all other APIs public (remove if you want security)
            )
            .formLogin(form -> form.disable()) // disable Spring Security login page
            .httpBasic(httpBasic -> httpBasic.disable()); // disable basic auth

        return http.build();
    }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//      logger.debug("🔒 Building SecurityFilterChain...");
//      
//      return http
//          .csrf(csrf -> {
//              logger.debug("CSRF disabled for stateless API");
//              csrf.disable();
//          })
//          .sessionManagement(sm -> {
//              logger.debug("Session management set to STATELESS");
//              sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//          })
//          .authorizeHttpRequests(auth -> {
//              logger.debug("Configuring endpoint access rules...");
//              auth
//                  .requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
//                  .requestMatchers(HttpMethod.GET, "/**").authenticated()
//                  .anyRequest().authenticated();
//          })
//          .oauth2ResourceServer(oauth2 -> oauth2
//              .jwt(jwt -> {
//                  logger.debug("Configuring JWT authentication converter...");
//                  jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
//              })
//          )
//          .build();
//  }
//
//  @Bean
//  public JwtAuthenticationConverter jwtAuthenticationConverter() {
//      logger.debug("Creating JwtAuthenticationConverter...");
//      var converter = new JwtAuthenticationConverter();
//      converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
//      
//      return converter;
//  }
//
//  private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
//      logger.debug("Extracting authorities from JWT token...");
//      Set<String> roles = new HashSet<>();
//
//      Object realmAccess = jwt.getClaims().get("realm_access");
//      if (realmAccess instanceof Map<?, ?> m) {
//          Object rs = m.get("roles");
//          if (rs instanceof Collection<?> c) {
//              for (Object r : c) {
//                  String role = "ROLE_" + String.valueOf(r).toUpperCase();
//                  logger.debug("Adding realm role: {}", role);
//                  roles.add(role);
//              }
//          }
//      }
//
//      Object direct = jwt.getClaims().get("roles");
//      if (direct instanceof Collection<?> c) {
//          for (Object r : c) {
//              String role = "ROLE_" + String.valueOf(r).toUpperCase();
//              logger.debug("Adding top-level role: {}", role);
//              roles.add(role);
//          }
//      }
//
//      Set<GrantedAuthority> authorities = roles.stream()
//              .map(SimpleGrantedAuthority::new)
//              .collect(Collectors.toSet());
//
//      logger.debug("Final authorities: {}", authorities);
//      return authorities;
//  }
}
