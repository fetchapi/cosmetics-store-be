package com.nhan.utils;

import com.nhan.model.entity.rolePermission.RolePermission;
import com.nhan.model.entity.user.SessionUser;
import com.nhan.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        UUID roleId = ((SessionUser) authentication.getPrincipal()).getUser().getRole().getId();

        String key = "permissions:" + roleId;

        if (redisTemplate.hasKey(key)) {
            String permissionChain = (String) redisTemplate.opsForValue().get(key);
            return permissionChain.contains((String) permission);
        }

        List<RolePermission> rolePermissionList = rolePermissionRepository.findByRoleId(roleId);

        List<String> permissionStrings = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissionList) {
            permissionStrings.add(rolePermission.getPermission().getCode());
        }

        redisTemplate.opsForValue().set(key, String.join(":", permissionStrings));

        return permissionStrings.contains((String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
