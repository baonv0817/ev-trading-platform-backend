package com.fpt.evplatform.modules.ai.controller;


import com.fpt.evplatform.modules.ai.dto.request.AiPriceRequest;
import com.fpt.evplatform.modules.ai.dto.response.AiPriceResponse;
import com.fpt.evplatform.modules.ai.service.AiPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiPriceController {

    private final AiPriceService service;

    @PostMapping("/price")
    public AiPriceResponse suggest(@RequestBody AiPriceRequest req) {
        return service.suggest(req);
    }
}
