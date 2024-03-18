package com.example.kotrip.jwt;

import com.example.kotrip.service.useDetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);

        // 토큰 유효성 검사
        jwtTokenProvider.validateToken(accessToken);

        // 유저 이름 추출
        String username = jwtTokenProvider.getUsername(accessToken);

        // UserDetails 추출
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // authenticationToken 발급
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        GrantedAuthority grantedAuthority = userDetails.getAuthorities().stream().findFirst().get();
        String authority = grantedAuthority.getAuthority();
        request.setAttribute("role",authority.split("_")[1]);

        filterChain.doFilter(request,response);
    }
}
