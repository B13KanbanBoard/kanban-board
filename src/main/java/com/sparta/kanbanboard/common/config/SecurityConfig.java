package com.sparta.kanbanboard.common.config;


import com.sparta.kanbanboard.common.security.jwt.CustomAccessDeniedHandler;
import com.sparta.kanbanboard.common.security.jwt.CustomAuthenticationEntryPoint;
import com.sparta.kanbanboard.common.security.jwt.JwtProvider;
import com.sparta.kanbanboard.common.security.jwt.JwtAuthenticationFilter;
import com.sparta.kanbanboard.common.security.jwt.JwtAuthorizationFilter;
import com.sparta.kanbanboard.common.security.UserDetailsServiceImpl;
import com.sparta.kanbanboard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final MemberRepository memberRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, memberRepository);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, userDetailsService, memberRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // CSFF 설정
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //resource 접근 허용 설정
                .requestMatchers(HttpMethod.POST,"/api/members/signup").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/members/refresh-token").permitAll()

                .requestMatchers("/").permitAll()// 메인 페이지 요청 허가
                .requestMatchers("/api/members/login-page").permitAll() // 로그인 페이지
                .requestMatchers("/api/members/signup").permitAll() // 회원 가입 페이지
                .requestMatchers("/api/members/myInfo").permitAll() // 내 정보 페이지
            .requestMatchers(HttpMethod.POST,"/api/members/signup2").permitAll() // 로그인 페이지 form data 형식
            
                .anyRequest().authenticated()
        );

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/api/member/login-page").permitAll()
        );


        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //예외 검증
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
        );

        return http.build();
    }
}
