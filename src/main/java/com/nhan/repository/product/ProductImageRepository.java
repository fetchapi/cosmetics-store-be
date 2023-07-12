package com.nhan.repository.product;

import com.nhan.model.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    @Modifying
    @Query("UPDATE ProductImage pi SET pi.deleted = true where pi.id = ?1")
    void softDeleteById(UUID id);

}
