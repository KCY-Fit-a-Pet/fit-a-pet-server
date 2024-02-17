package kr.co.fitapet.api.common.security.authorization;

import kr.co.fitapet.domain.domains.manager.service.ManagerSearchService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class ManagerPermissionExpression implements PermissionEvaluator {
    private final ManagerSearchService managerSearchService;

    public ManagerPermissionExpression(ManagerSearchService managerSearchService) {
        this.managerSearchService = managerSearchService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
