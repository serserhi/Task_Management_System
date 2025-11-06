package taskmanagement;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RestController
@Validated
public class AccountController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final JwtEncoder jwtEncoder;

    public AccountController(AccountRepository accountRepository, PasswordEncoder encoder, JwtEncoder jwtEncoder) {
        this.accountRepository = accountRepository;
        this.encoder = encoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Void> registerAccount(@Valid @RequestBody Account account) {
        account.setEmail(account.getEmail().toLowerCase());
        if (accountRepository.existsByEmail(account.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        account.setPassword(encoder.encode(account.getPassword()));
        Account acc = accountRepository.save(account);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/auth/token")
    public ResponseEntity<TokenDTO> token(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")); // Collects authorities into a space-separated string

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(300, ChronoUnit.SECONDS))
                .claim("scope", authorities)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO(jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue()));
    }

}