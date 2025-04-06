package com.zoho.config;

import com.zoho.entity.SignUpUser;
import com.zoho.repository.SignUpUserRepository;
import com.zoho.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private SignUpUserRepository signUpUserRepository;

    public JWTFilter(JWTService jwtService, SignUpUserRepository signUpUserRepository) {
        this.jwtService = jwtService;
        this.signUpUserRepository = signUpUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {

            // Remove "Bearer "
            String tokenVal = token.substring(7).trim();
            // Remove starting and ending quotes (if present)
            if (tokenVal.startsWith("\"") && tokenVal.endsWith("\"")) {
                tokenVal = tokenVal.substring(1, tokenVal.length() - 1);
                System.out.println("✅ Final Clean Token: " + tokenVal);
            }


            String username = jwtService.getUserName(tokenVal);
            System.out.println(username);
            System.out.println("✅ Username from token: " + username);
            Optional<SignUpUser> opUsername = signUpUserRepository.findByUsername(username);
            if (opUsername.isPresent()) {
                SignUpUser signUpUser = opUsername.get();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signUpUser,null,null);
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            }


        }
        filterChain.doFilter(request, response);

          /*you should tell the spring security that don't send all the URLs here, only the URLs that
        comes with the tokens let it come here,and the URL comes without tokens, don't send here
        login url won't come here with the token because it will generate the token */

}
    }
