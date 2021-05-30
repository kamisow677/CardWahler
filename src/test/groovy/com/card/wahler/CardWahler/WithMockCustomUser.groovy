package com.card.wahler.CardWahler

import org.springframework.core.annotation.AliasFor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
@interface WithMockCustomUser {

    String value() default "user";

    String username() default "";

    String[] roles() default [ "user" ];

    String password() default "password";

    @AliasFor(annotation = WithSecurityContext.class)
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}


class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        def authorities = new ArrayList<SimpleGrantedAuthority>()
        for (String role : customUser.roles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role))
        }
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
                customUser.username(), customUser.password(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authReq);
        return SecurityContextHolder.getContext();
    }
}
