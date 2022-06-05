package com.db.articledirect.filter;

import com.db.articledirect.Repository.UserRepository;
import com.db.articledirect.constants.Constants;
import com.db.articledirect.model.User;
import com.db.articledirect.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtility jwtUtility;

//    @Autowired
//    UserServiceJWT userServiceJWT;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Called!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String authorization = request.getHeader("Authorization");
        String token = null;
        Integer userId = null;

        if(null!=authorization && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            userId = jwtUtility.getUserIdFromToken(token);
        }

        if(null != userId && SecurityContextHolder.getContext().getAuthentication() == null){
            System.out.println("----------------- checking ------------------");
            User user = userRepository.getUserById(userId);

            if(jwtUtility.validateToken(token,user)){
                List<SimpleGrantedAuthority> authorites = new ArrayList<>();
                authorites.add(new SimpleGrantedAuthority(Constants.ADMIN));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(user,user.getPassword(), authorites);

                usernamePasswordAuthenticationToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        filterChain.doFilter(request,response);
        System.out.println("doFilterInternal userId:" + userId);
    }
}
