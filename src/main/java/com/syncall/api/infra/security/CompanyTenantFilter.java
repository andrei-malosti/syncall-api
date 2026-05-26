package com.syncall.api.infra.security;

import com.nimbusds.jose.proc.SecurityContext;
import com.syncall.api.infra.multitenancy.CompanyContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CompanyTenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt){
            String companyId = jwt.getClaimAsString("companyId");
            CompanyContext.setCompanyId(Long.valueOf(companyId));
        }

        try{
            filterChain.doFilter(request, response);
        } finally {
            CompanyContext.clear();
        }
    }
}
