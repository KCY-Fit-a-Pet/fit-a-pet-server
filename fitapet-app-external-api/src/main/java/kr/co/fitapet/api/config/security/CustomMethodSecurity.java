package kr.co.fitapet.api.config.security;

import kr.co.fitapet.api.common.security.authorization.ManagerPermissionExpression;
import kr.co.fitapet.domain.domains.care.service.CareSearchService;
import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import kr.co.fitapet.domain.domains.memo.service.MemoSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class CustomMethodSecurity {
    private final ManagerSearchService managerSearchService;
    private final CareSearchService careSearchService;
//    private final CareDateSearchService careDateSearchService;
    private final MemoSearchService memoSearchService;
//    private final MemoCategorySearchService memoCategorySearchService;

    @Bean
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new ManagerPermissionExpression(managerSearchService));
        return expressionHandler;
    }
}
