package uz.security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import uz.security.AbstractTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractTest {

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void userWithUserRoleSuccess() throws Exception{
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Username: user - User role: ROLE_USER"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void userWithAdminRoleSuccess() throws Exception{
        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Username: admin - User role: ROLE_ADMIN"));
    }

    @Test
    @WithUserDetails(
            userDetailsServiceBeanName = "userDetailsServiceImpl",
            value = "manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    public void userDetailsServiceImplSuccess() throws UsernameNotFoundException {
        try {
            mockMvc.perform(get("/api/user"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Username: manager - User role: ROLE_USER"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
