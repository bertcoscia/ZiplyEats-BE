package bertcoscia.ZiplyEats_BE.repositories;

import bertcoscia.ZiplyEats_BE.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRole, UUID> {

    Optional<UserRole> findById(UUID id);

    Optional<UserRole> findByUserRoleIgnoreCase(String userRole);

    boolean existsByUserRole(String userRole);
}
