package taskmanagement;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Email(regexp = "\\w+([.-]\\w+)*@\\w+(\\.\\w+)+")
    @NotBlank(message="The email cannot be blank")
    @NotNull(message="The email field is mandatory")
    private String email;


    @Column(nullable = false)
    @NotBlank(message="The password cannot be blank")
    @NotNull(message="The password field is mandatory")
    @Size(min=6, message= "The length of the password must be greater than 6 characters")

    private String password;

    public Account() {}

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now, returning an empty list as you haven't defined roles/authorities.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // In your case, the username is the email
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming accounts do not expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming accounts are not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials do not expire
    }

    @Override
    public boolean isEnabled() {
        return true; // Assuming accounts are always enabled
    }

}
