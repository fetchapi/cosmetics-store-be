package com.nhan.repository;

import com.nhan.model.entity.rolePermission.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    boolean existsByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

    List<RolePermission> findByRoleId(UUID roleId);

    @Modifying
    @Query("UPDATE RolePermission rp SET rp.deleted = true where rp.id = ?1")
    void softDeleteById(UUID id);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = ?1")
    void deleteRolePermissionByRoleId(UUID roleId);

}
