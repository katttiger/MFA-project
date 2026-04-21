package se.iths.cecilia.mfaproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email is invalid"
    )
    @NotBlank(message = "You must enter an email-address")
    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @NotBlank(message = "You must enter a password")
    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "consent_given")
    private boolean consent;

    @Column(nullable = false, name = "role")
    @NotBlank(message = "You must select a role")
    private String role;

    @Column(nullable = false, name = "allowsMFA")
    private boolean allowsMFA;

    @Column(nullable = true, name = "mfasecret")
    private String secret;

    @Override
    public String toString() {
        return
                "id: " + id + "\n" +
                        " username: " + username + "\n" +
                        " cookies: " + consent + "\n" +
                        " role: " + role + "\n";
    }
}
