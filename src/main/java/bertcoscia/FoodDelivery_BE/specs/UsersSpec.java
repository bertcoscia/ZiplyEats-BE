package bertcoscia.FoodDelivery_BE.specs;

import bertcoscia.FoodDelivery_BE.entities.User;
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
