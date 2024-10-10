package bertcoscia.ZiplyEats_BE.specs;

import bertcoscia.ZiplyEats_BE.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UsersSpec {

    public static Specification<User> isRider(String role) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("userRole").get("userRole")),
                    role.toLowerCase()
            );
        };
    }


}
