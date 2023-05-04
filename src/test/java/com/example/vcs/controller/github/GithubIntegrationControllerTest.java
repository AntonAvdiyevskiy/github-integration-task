package com.example.vcs.controller.github;

import com.example.vcs.dto.UserRepositoriesDetailsDTO;
import com.example.vcs.service.VcsRepositoriesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubIntegrationController.class)
public class GithubIntegrationControllerTest {

    @MockBean
    private VcsRepositoriesService<UserRepositoriesDetailsDTO> vcsRepositoriesService;
    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/github/user/{username}/repos";

    private static final String USERNAME = "test";

    @Test
    void getRepositories_OK() throws Exception {
        mockMvc.perform(get(BASE_URL, USERNAME))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(vcsRepositoriesService, times(1))
                .getUsersRepositories(eq(USERNAME), eq(30), eq(1));
    }
}
