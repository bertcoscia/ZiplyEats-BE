package bertcoscia.ZiplyEats_BE.payloads.edit.editUser;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditUsersPasswordDTO(
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
        @Pattern(regexp = ".*[@#$%^&+=!].*", message = "Password must contain at least one special character (@#$%^&+=!)")
        String currentPassword,
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
        @Pattern(regexp = ".*[@#$%^&+=!].*", message = "Password must contain at least one special character (@#$%^&+=!)")
        String newPassword
) {
        @AssertTrue(message = "New password cannot be the same as the current password")
        public boolean isDifferentPasswords() {
                return !currentPassword.equals(newPassword);
        }
}