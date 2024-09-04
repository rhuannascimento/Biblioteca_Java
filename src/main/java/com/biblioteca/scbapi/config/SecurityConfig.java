package com.biblioteca.scbapi.config;

import com.biblioteca.scbapi.security.JwtAuthFilter;
import com.biblioteca.scbapi.security.JwtService;
import com.biblioteca.scbapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
                .antMatchers("/api/v1/livro/get")
                .permitAll()
                .antMatchers("/api/v1/livro/post")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/livro/delete")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/livro/atualizar")
                .hasAnyRole("ADMIN")

                //Obra
                .antMatchers("/api/v1/obra/get")
                .permitAll()
                .antMatchers("/api/v1/obra/post")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/obra/delete")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/obra/atualizar")
                .hasAnyRole("ADMIN")

                //Reserva
                .antMatchers("/api/v1/reserva/**")
                .hasAnyRole("ADMIN", "USER")

                //Revista
                .antMatchers("/api/v1/revista/get")
                .permitAll()
                .antMatchers("/api/v1/revista/post")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/revista/delete")
                .hasAnyRole("ADMIN")
                .antMatchers("/api/v1/revista/atualizar")
                .hasAnyRole("ADMIN")
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
