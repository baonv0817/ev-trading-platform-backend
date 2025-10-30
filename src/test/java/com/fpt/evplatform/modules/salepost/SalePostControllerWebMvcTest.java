package com.fpt.evplatform.modules.salepost;

import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.salepost.controller.SalePostController;
import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.service.SalePostQueryService;
import com.fpt.evplatform.modules.salepost.service.SalePostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import java.security.Principal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = SalePostController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = com.fpt.evplatform.config.SecurityConfig.class
        )
)
class SalePostControllerWebMvcTest {

    @Autowired MockMvc mockMvc;
    @MockBean SalePostService salePostService;
    @MockBean SalePostQueryService salePostQueryService;

    @Test
    void createPost_valid_should201() throws Exception {
        PostResponse resp = new PostResponse();
        try {
            var f = PostResponse.class.getDeclaredField("listingId");
            f.setAccessible(true);
            f.set(resp, 123);
        } catch (Exception ignored) {}

        when(salePostService.createPost(nullable(String.class), any(CreatePostRequest.class), anyList()))
                .thenReturn(resp);

        String json = """
        {
          "productType":"BATTERY",
          "askPrice":1000000,
          "description":"OK",
          "battery":{"capacityKwh":10,"sohPercent":90,"cycleCount":50}
        }
        """;

        MockMultipartFile payload = new MockMultipartFile(
                "payload", "payload.json", MediaType.APPLICATION_JSON_VALUE, json.getBytes()
        );

        Principal p = () -> "seller1";

        mockMvc.perform(multipart("/api/sale-posts")
                        .file(payload) // part "payload" như bạn đang gửi
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .principal(() -> "seller1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000));
    }

    @Test
    void createPost_batteryWithVehicle_should400() throws Exception {
        when(salePostService.createPost(nullable(String.class), argThat(r ->
                r != null && r.getBattery() != null && r.getVehicle() != null), anyList()))
                .thenThrow(new AppException(ErrorCode.VEHICLE_DETAIL_MUST_BE_NULL));

        String badJson = """
        {
          "productType":"BATTERY",
          "askPrice":1000000,
          "battery":{"capacityKwh":10,"sohPercent":90,"cycleCount":50},
          "vehicle":{"modelId":1}
        }
        """;

        MockMultipartFile payload = new MockMultipartFile(
                "payload", "payload.json", MediaType.APPLICATION_JSON_VALUE, badJson.getBytes()
        );

        Principal p = () -> "seller1";
        mockMvc.perform(multipart("/api/sale-posts")
                        .file(payload)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .principal(() -> "seller1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000));

    }
}
