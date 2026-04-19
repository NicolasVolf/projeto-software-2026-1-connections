package br.insper.conexoes.connections;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/connections")
@CrossOrigin(origins = "*")
public class ConnectionController {

    private final ConnectionService service;

    public ConnectionController(ConnectionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Connection createConnection(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CreateConnectionRequest request) {
        System.out.println(token);
        return service.create(
                request.fromUserId(),
                request.toUserId()
        );
    }

    @GetMapping("/{userId}")
    public List<Connection> listConnections(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable String userId) {
        System.out.println(token);
        return service.listByUser(userId);
    }

    @DeleteMapping
    public void deleteConnection(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody DeleteConnectionRequest request) {
        List<String> roles = jwt.getClaimAsStringList("https://social-insper.com/roles");

        if (roles == null || !roles.contains("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas ADMIN pode executar esta ação"
            );
        }
        service.delete(
                request.fromUserId(),
                request.toUserId()
        );
    }
}

