//package com.nghianguyen.scnetwork.Utils;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.constraints.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.Pair;
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
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        if(isBypassToken(request)){
//            filterChain.doFilter(request, response);
//            return;
//        }
//    }
//
//    private boolean isBypassToken(@NotNull HttpServletRequest request) {
//        final List<Pair<String, String>> byPassToken =
//                Arrays.asList(Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
//                        Pair.of(String.format("%s/users/login", apiPrefix), "POST"));
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
