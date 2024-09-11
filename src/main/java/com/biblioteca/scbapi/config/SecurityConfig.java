package com.biblioteca.scbapi.config;

import com.biblioteca.scbapi.security.JwtAuthFilter;
import com.biblioteca.scbapi.security.JwtService;
import com.biblioteca.scbapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                // Emprestimo
                .antMatchers("/api/v1/emprestimo/**")
                .hasAnyRole("ADMIN", "USER")

                // Exemplar
                .antMatchers("/api/v1/exemplar/**")
                .hasAnyRole("ADMIN")

                //Funcionario
                .antMatchers("/api/v1/funcionario/**")
                .hasAnyRole("ADMIN")

                //Livro
                .antMatchers(HttpMethod.GET,"/api/v1/livro")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/livro")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/v1/livro")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api/v1/livro")
                .hasAnyRole("ADMIN")

                //Obra
                .antMatchers(HttpMethod.GET,"/api/v1/obra/get")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/obra")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/obra")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/obra")
                .hasAnyRole("ADMIN")

                //Reserva
                .antMatchers("/api/v1/reserva/**")
                .hasAnyRole("ADMIN", "USER")

                //Revista
                .antMatchers(HttpMethod.GET,"/api/v1/revista")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/revista")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/v1/revista")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api/v1/revista")
                .hasAnyRole("ADMIN")

                //Tomador
                .antMatchers("/api/v1/tomador/**")
                .hasAnyRole("ADMIN")

                //Usuario
                .antMatchers( "/api/v1/usuario/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
