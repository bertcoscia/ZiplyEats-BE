package bertcoscia.ZiplyEats_BE.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"idRole"})
public class UserRole {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private UUID idRole;
    @Column(name = "user_role")
    private String userRole;

    public UserRole(String userRole) {
        this.userRole = userRole;
    }
}
