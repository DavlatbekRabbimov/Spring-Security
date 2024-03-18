package uz.security;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import uz.security.dto.UserDto;
import uz.security.model.entity.Role;
import uz.security.model.entity.User;
import uz.security.model.entity.type.RoleType;
import uz.security.model.repo.UserRepo;
import uz.security.service.UserService;

import java.util.HashSet;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public class AbstractTest {

    protected static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:15.5");
        postgreSQLContainer = new PostgreSQLContainer<>(postgres).withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);

    }

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepo userRepo;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {

        UserDto.builder().username("user").password("user").roleType(RoleType.ROLE_USER)
                .build();

        UserDto.builder()
                .username("admin").password("admin").roleType(RoleType.ROLE_ADMIN)
                .build();

        User user = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(Role.setRole(RoleType.ROLE_USER));
        user.setUsername("manager");
        user.setPassword("manager");
        user.setRoles(roles);
        userRepo.save(user);
    }

    @AfterEach
    public void afterEach() {
        userRepo.deleteAll();
    }

}
