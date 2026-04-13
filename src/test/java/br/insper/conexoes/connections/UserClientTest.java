package br.insper.conexoes.connections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClient userClient;

    @Test
    void userExists_shouldReturnTrue_whenUserFound() {
        when(restTemplate.getForEntity(anyString(), eq(UserResponse.class)))
                .thenReturn(ResponseEntity.ok(new UserResponse("id", "email", "name")));

        assertTrue(userClient.userExists("user1"));
    }

    @Test
    void userExists_shouldReturnFalse_whenUserNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(UserResponse.class)))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatus.NOT_FOUND, "Not Found", HttpHeaders.EMPTY, null, null));

        assertFalse(userClient.userExists("user1"));
    }

    @Test
    void userExists_shouldReturnFalse_onUnexpectedException() {
        when(restTemplate.getForEntity(anyString(), eq(UserResponse.class)))
                .thenThrow(new RuntimeException("unexpected error"));

        assertFalse(userClient.userExists("user1"));
    }
}
