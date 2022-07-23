package ccc.keeweapi.document.utils;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keewedomain.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@Deprecated
@Disabled
@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)
@ImportAutoConfiguration(FeignAutoConfiguration.class)
public abstract class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    protected JwtUtils jwtUtils;
}
