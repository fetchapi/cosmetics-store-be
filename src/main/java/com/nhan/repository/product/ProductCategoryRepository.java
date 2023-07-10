package com.nhan.repository.product;

import com.nhan.model.entity.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    boolean existsByCode(String code);

    Optional<ProductCategory> findByCode(String code);


    @Query("SELECT pc FROM ProductCategory pc where pc.productCategory.id = ?1")
    List<ProductCategory> findAllByParentId(UUID id);

    @Modifying
    @Query("UPDATE ProductCategory pc SET pc.deleted = true where pc.id = ?1")
    void softDeleteById(UUID id);

}
