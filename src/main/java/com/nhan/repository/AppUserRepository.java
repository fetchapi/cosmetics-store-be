package com.nhan.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nhan.model.entity.user.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

	@Query(value = """
			SELECT u FROM AppUser u WHERE 	
			LOWER(unaccent(u.firstname)) LIKE LOWER(CONCAT('%', unaccent(:keyword), '%')) or
			LOWER(unaccent(u.lastname)) LIKE LOWER(CONCAT('%', unaccent(:keyword), '%'))
			""")
	Page<AppUser> findAll(String keyword, Pageable pageable);

	Optional<AppUser> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<AppUser> findByVerificationCode(String code);

	Optional<AppUser> findByResetPasswordCode(String code);

}
