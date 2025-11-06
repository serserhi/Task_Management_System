package taskmanagement;

public record  TokenDTO (
    String token
){
    public static TokenDTO fromToken(String token){
    return new TokenDTO(token);
    }
}
