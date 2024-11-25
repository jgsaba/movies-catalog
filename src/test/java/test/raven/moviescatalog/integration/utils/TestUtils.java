package test.raven.moviescatalog.integration.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import test.raven.moviescatalog.security.data.AuthenticationDTO;
import test.raven.moviescatalog.security.data.LoginResponseDTO;

@Component
public class TestUtils {

    @Value("${test.valid-credentials.user-email}")
    private String validUserEmail;

    @Value("${test.valid-credentials.admin-email}")
    private String validAdminEmail;

    @Value("${test.valid-credentials.password}")
    private String validPassword;

    @Autowired
    private RestTemplate restTemplate;

    private String getToken(String email, String password){
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail(email);
        authenticationDTO.setPassword(password);

        ResponseEntity<LoginResponseDTO> loginResponseDTOResponseEntity = restTemplate.postForEntity("/auth/login", authenticationDTO, LoginResponseDTO.class);
        return loginResponseDTOResponseEntity.getBody().getToken();
    }

    public HttpHeaders getValidAdminRoleSecurityHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(validAdminEmail, validPassword));

        return headers;
    }

    public HttpHeaders getValidUserRoleSecurityHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken(validUserEmail, validPassword));

        return headers;
    }
}
