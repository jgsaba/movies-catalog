package test.raven.moviescatalog.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import test.raven.moviescatalog.model.CreateUserDTO;
import test.raven.moviescatalog.security.data.AuthenticationDTO;
import test.raven.moviescatalog.security.data.LoginResponseDTO;
import test.raven.moviescatalog.security.infra.TokenService;
import test.raven.moviescatalog.utils.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestConfig.class)
public class AuthIntegrationTests extends BaseIntegrationTests{

    @Value("${test.valid-credentials.admin-email}")
    private String validAdminEmail;

    @Value("${test.valid-credentials.password}")
    private String validPassword;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    @Test
    public void loginTest(){

        AuthenticationDTO authenticationDTO = new AuthenticationDTO(validAdminEmail, validPassword);

        ResponseEntity<LoginResponseDTO> loginResponseDTOResponseEntity = restTemplate.postForEntity("/auth/login", authenticationDTO, LoginResponseDTO.class);
        String token = loginResponseDTOResponseEntity.getBody().getToken();

        String s = tokenService.validateToken(token);
        assertFalse(s.isEmpty());
    }

    private String loginAndGetToken(String email, String password){
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(email, password);

        ResponseEntity<LoginResponseDTO> loginResponseDTOResponseEntity = restTemplate.postForEntity("/auth/login", authenticationDTO, LoginResponseDTO.class);
        return loginResponseDTOResponseEntity.getBody().getToken();
    }

    @Test
    public void registerTest(){

        String newEmail = "newlogin@mail.com";
        String newPassword = "new-pass!@";

        CreateUserDTO registerDTO = new CreateUserDTO(newEmail, newPassword, UserRole.USER);

        String token = loginAndGetToken(validAdminEmail, validPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CreateUserDTO> request = new HttpEntity<>(registerDTO, headers);

        ResponseEntity<Void> exchange = restTemplate.exchange("/users", HttpMethod.POST, request, Void.class);
        String s = loginAndGetToken(newEmail, newPassword);

        String newToken = tokenService.validateToken(token);

        assertFalse(newToken.isEmpty());
        assertEquals(201, exchange.getStatusCodeValue());
    }
}
