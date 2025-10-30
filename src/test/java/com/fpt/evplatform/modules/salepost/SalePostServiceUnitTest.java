
package com.fpt.evplatform.modules.salepost;

import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;

import com.fpt.evplatform.modules.membership.entity.MembershipPlan;
import com.fpt.evplatform.modules.model.entity.Model;

import com.fpt.evplatform.modules.salepost.dto.request.CreatePostRequest;
import com.fpt.evplatform.modules.salepost.dto.response.PostResponse;
import com.fpt.evplatform.modules.salepost.entity.SalePost;
import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import com.fpt.evplatform.modules.salepost.service.SalePostService;
import com.fpt.evplatform.modules.salepost.service.SalePostQueryService;

import com.fpt.evplatform.modules.model.repository.ModelRepository;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SalePostServiceUnitTest {


    @InjectMocks private SalePostService service;

    @Mock private UserRepository userRepo;
    @Mock private SalePostRepository saleRepo;
    @Mock private ModelRepository modelRepo;
    @Mock private SalePostQueryService salePostQueryService;
    @Mock private com.fpt.evplatform.modules.media.service.MediaService mediaService;

    private User activeUser;
    private MembershipPlan paidPlan;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.openMocks(this);


        paidPlan = new MembershipPlan();
        paidPlan.setName("STANDARD");
        paidPlan.setPriorityLevel(2);
        paidPlan.setMaxPosts(10);

        activeUser = new User();
        activeUser.setUserId(123);
        activeUser.setPlan(paidPlan);
        activeUser.setPlanStatus("ACTIVE");
        activeUser.setStartAt(LocalDateTime.now().minusDays(1));
        activeUser.setEndAt(LocalDateTime.now().plusDays(30));

        when(userRepo.findByUsername("seller1"))
                .thenReturn(Optional.of(activeUser));

        when(saleRepo.countBySeller_UserIdAndCreatedAtBetween(eq(123), any(), any()))
                .thenReturn(0);


        when(saleRepo.save(any(SalePost.class))).thenAnswer(inv -> inv.getArgument(0));


        when(salePostQueryService.getDetail(any()))
                .thenReturn(new PostResponse());
    }

    private CreatePostRequest batteryReq(Integer soh) {
        var r = new CreatePostRequest();
        r.setProductType(ProductType.BATTERY);
        r.setAskPrice(new BigDecimal("1000000"));
        r.setDescription("OK");
        var b = new CreatePostRequest.BatteryDetail();
        b.setCapacityKwh(10.0);
        if (soh != null) b.setSohPercent(soh);
        b.setCycleCount(50);
        r.setBattery(b);
        return r;
    }

    private CreatePostRequest vehicleReq() {
        var r = new CreatePostRequest();
        r.setProductType(ProductType.VEHICLE);
        r.setAskPrice(new BigDecimal("500000000"));
        r.setDescription("Good EV");
        var v = new CreatePostRequest.VehicleDetail();
        v.setModelId(1);
        r.setVehicle(v);
        return r;
    }

    // ======= TEST 1: PASS (BATTERY) =======
    @Test
    void createPost_battery_valid_shouldPass() {
        var req = batteryReq(90);

        var resp = service.createPost("seller1", req, null);

        assertNotNull(resp);
        verify(saleRepo, times(1)).save(any(SalePost.class));

        verify(mediaService, never()).upload(anyInt(), any());
    }

    // ======= TEST 2: FAIL rule (BATTERY + vehicle) =======
    @Test
    void createPost_battery_withVehicle_shouldThrow() {
        var req = batteryReq(90);
        req.setVehicle(new CreatePostRequest.VehicleDetail());

        AppException ex = assertThrows(AppException.class,
                () -> service.createPost("seller1", req, null));

        assertEquals(ErrorCode.VEHICLE_DETAIL_MUST_BE_NULL, ex.getErrorCode());
    }

    // ======= TEST 3: FAIL quota (minh hoạ) =======
    @Test
    void createPost_quotaReached_shouldThrow() {
        when(saleRepo.countBySeller_UserIdAndCreatedAtBetween(eq(123), any(), any()))
                .thenReturn(10);

        var req = batteryReq(90);
        AppException ex = assertThrows(AppException.class,
                () -> service.createPost("seller1", req, null));

        assertEquals(ErrorCode.SALE_POST_LIMIT_REACHED, ex.getErrorCode());
    }

    // ======= TEST 4: PASS VEHICLE =======
    @Test
    void createPost_vehicle_valid_shouldPass() {
        var req = vehicleReq();
        when(modelRepo.findById(1)).thenReturn(Optional.of(new Model()));

        var resp = service.createPost("seller1", req, null);

        assertNotNull(resp);
        verify(saleRepo, times(1)).save(any(SalePost.class));
    }


    @Test
    void createPost_vehicle_withBattery_shouldThrow() {
        var req = vehicleReq();
        req.setBattery(new CreatePostRequest.BatteryDetail());
        when(modelRepo.findById(1)).thenReturn(Optional.of(new Model()));

        AppException ex = assertThrows(AppException.class,
                () -> service.createPost("seller1", req, null));

        assertEquals(ErrorCode.BATTERY_DETAIL_MUST_BE_NULL, ex.getErrorCode());
    }
}
