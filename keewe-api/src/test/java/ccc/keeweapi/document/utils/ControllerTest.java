package ccc.keeweapi.document.utils;

import ccc.keeweapi.api.user.ProfileController;
import ccc.keeweapi.api.user.UserController;
import ccc.keeweapi.config.security.SecurityConfig;
import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.service.user.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@Disabled
@WebMvcTest(controllers = {
        ProfileController.class,
        UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
) // Controller.class 들 등록
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JwtUtils jwtUtils;
}
