//package com.nghianguyen.scnetwork.Utils;
//
//import com.nghianguyen.scnetwork.models.User;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.Pair;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class JwtTokenFilter extends OncePerRequestFilter {
//    @Value("${api.prefix}")
//    private String apiPrefix;
//
//    @Autowired
//    private JwtTokenUtils jwtTokenUtils;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            if(isBypassToken(request)){
//                filterChain.doFilter(request, response);
//                return;
//            }
//            final String autHeader = request.getHeader("Authorization");
//            if(autHeader == null || !autHeader.startsWith("Bearer ")){
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                return;
//            }
//            final String token = autHeader.substring(7);
//            final String email = jwtTokenUtils.extractEmail(token);
//            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                User userDetails = (User) userDetailsService.loadUserByUsername(email);
//                if(jwtTokenUtils.validateToken(token, userDetails)){
//                    UsernamePasswordAuthenticationToken authenticationToken
//                            = new UsernamePasswordAuthenticationToken
//                            (userDetails, null, userDetails.getAuthorities());
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//                filterChain.doFilter(request, response);
//            }
//        } catch (Exception e) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); //not returned
//        }
//    }
//
//    private boolean isBypassToken(@NotNull HttpServletRequest request) {
//        final List<Pair<String, String>> byPassToken =
//                Arrays.asList(Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
//                        Pair.of(String.format("%s/users/login", apiPrefix), "POST"));
//        //just login/register byass
//        //get Path, Method
//        //condition(path=="/", method = "CURD")
//        String servletPath = request.getServletPath();
//        String servletMethod = request.getMethod();
////        if(servletPath.equals(String.format("%s/viewpost", apiPrefix))
////                 && servletMethod.equals("GET")){
////            return true;
////        }
//        for(Pair<String, String> byPass : byPassToken){
//            if(servletPath.contains(byPass.getFirst())
//            && servletMethod.contains(byPass.getSecond())){
//                return  true;
//            }
//        }
//        return false;
//    }
//}
