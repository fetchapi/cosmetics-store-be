package com.nhan.repository.product;

import com.nhan.model.entity.product.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    boolean existsByCode(String code);

    Optional<ProductBrand> findByCode(String code);

    @Modifying
    @Query("UPDATE ProductBrand pb SET pb.deleted = true where pb.id = ?1")
    void softDeleteById(UUID id);

}
