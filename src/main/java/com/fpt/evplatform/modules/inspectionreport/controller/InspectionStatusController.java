package com.fpt.evplatform.modules.inspectionreport.controller;

import com.fpt.evplatform.modules.inspectionreport.dto.response.InspectionStatusResponse;
import com.fpt.evplatform.modules.inspectionreport.service.InspectionStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inspection-status")
@RequiredArgsConstructor
public class InspectionStatusController {

    private final InspectionStatusService statusService;

    @GetMapping("/{listingId}")
    public ResponseEntity<InspectionStatusResponse> getStatus(@PathVariable Integer listingId) {
        InspectionStatusResponse resp = statusService.getStatusByListing(listingId);
        return ResponseEntity.ok(resp);
    }
}