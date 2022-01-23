package com.book.manager.presentation.config


import com.book.manager.application.service.AuthenticationService
import com.book.manager.application.service.security.BookManagerUserDetailsService
import com.book.manager.domain.enum.RoleType
import com.book.manager.presentation.handler.BookManagerAccessDeniedHandler
import com.book.manager.presentation.handler.BookManagerAuthenticationEntryPoint
import com.book.manager.presentation.handler.BookManagerAuthenticationFailureHandler
import com.book.manager.presentation.handler.BookManagerAuthenticationSuccessHandler
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
class SecurityConfig(private val authenticationService: AuthenticationService) : WebSecurityConfigurerAdapter() {
    // 認証、認可に関する設定
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .mvcMatchers("/login").permitAll() //mvcMatchersで対象のAPIのパスを指定。permitAllで全てのアクセスを許可
            .mvcMatchers("/admin/**").hasAuthority(RoleType.ADMIN.toString()) //hasAuthorityでアクセス権限を指定
            .anyRequest().authenticated() //上記以外のAPIは認証済のユーザーのみアクセスを許可
            .and()
            .csrf().disable()
            .formLogin() //フォームログインを有効化
            .loginProcessingUrl("/login") //ログインAPIのパスを指定
            .usernameParameter("email") //ログインAPIに渡すユーザー名のパラメータを指定
            .passwordParameter("pass") // ログインAPIに渡すパスワードのパラメータを指定
            .successHandler(BookManagerAuthenticationSuccessHandler()) //認証成功時に実行するハンドラーを指定
            .failureHandler(BookManagerAuthenticationFailureHandler()) //認証失敗時に実行するハンドラーを指定
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(BookManagerAuthenticationEntryPoint()) //未認証時のハンドラーを指定
            .accessDeniedHandler(BookManagerAccessDeniedHandler()) //認可失敗時のハンドラーを指定
            .and()
            .cors()
            .configurationSource(corsConfigurationSource())
    }
    //認証処理に関する設定
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(BookManagerUserDetailsService(authenticationService)) //認証処理を実行するクラスを指定
            .passwordEncoder(BCryptPasswordEncoder()) //パスワードの暗号化アルゴリズムを指定(ここではBCrypt)
    }
    //CORSに関する各種許可設定
    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL) //全てのHTTPメソッドを許可
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL) //全てのヘッダを許可
        corsConfiguration.addAllowedOrigin("http://localhost:8081") //アクセス元のドメインを指定
        corsConfiguration.allowCredentials = true

        val corsConfigurationSource = UrlBasedCorsConfigurationSource()
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration)

        return corsConfigurationSource
    }
}