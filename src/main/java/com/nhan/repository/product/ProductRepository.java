package com.nhan.repository.product;

import com.nhan.model.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = """
            SELECT p FROM Product p 
            INNER JOIN ProductCategory as pc on p.productCategory.id = pc.id
            WHERE (
            lower(unaccent(p.name)) LIKE  lower(concat('%', unaccent(:keyword), '%')) OR
            lower(unaccent(pc.name)) LIKE  lower(concat('%', unaccent(:keyword), '%'))
            )
            """)
    Page<Product> findAll(String keyword, Pageable pageable);

    @Query(value = """
            SELECT p FROM Product p 
            WHERE p.productCategory.id in (?1)
            """)
    Page<Product> filterByCategoryList(List<UUID> uuids, Pageable pageable);

    @Query(value = """
            SELECT p FROM Product p 
            INNER JOIN ProductCategory as pc on p.productCategory.id = pc.id
            INNER JOIN ProductBrand as pb on p.productBrand.id = pb.id
            WHERE (
            lower(unaccent(p.name)) LIKE  lower(concat('%', unaccent(:keyword), '%')) OR
            lower(unaccent(pc.name)) LIKE  lower(concat('%', unaccent(:keyword), '%')) OR
            lower(unaccent(pb.name)) LIKE  lower(concat('%', unaccent(:keyword), '%'))
            )
            """)
    Page<Product> searchProduct(String keyword, Pageable pageable);

    boolean existsBySlug(String slug);

    Optional<Product> findBySlug(String slug);

    @Modifying
    @Query("UPDATE Product p SET p.deleted = true where p.id = ?1")
    void softDeleteById(UUID id);

}
