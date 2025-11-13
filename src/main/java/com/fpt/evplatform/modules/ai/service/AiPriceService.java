package com.fpt.evplatform.modules.ai.service;

import com.fpt.evplatform.common.enums.ProductType;
import com.fpt.evplatform.modules.ai.dto.request.AiPriceRequest;
import com.fpt.evplatform.modules.ai.dto.response.AiPriceResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiPriceService {

    private final ChatClient chatClient;

    public AiPriceService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public AiPriceResponse suggest(AiPriceRequest r) {
        // 1) System ràng buộc format + bối cảnh
        SystemMessage systemMessage = new SystemMessage("""
            You are a pricing assistant for the Vietnamese second-hand EV market.
            Return ONLY these four lines, exactly in this order (no extra text, no units, no commas/dots in numbers):
            priceMinVND: <number>
            priceMaxVND: <number>
            suggestedPriceVND: <number>
            note: <fully reason in Vietnamese>
            Use realistic, conservative VND amounts.
            If you deviate from the format, your answer is invalid.
            """);

        // 2) User content theo loại sản phẩm
        String userBlock = (r.getProductType() == ProductType.BATTERY)
                ? """
                    ProductType: BATTERY
                    Chemistry: %s
                    Capacity(kWh): %s
                    SOH(%%): %s
                    CycleCount: %s
                    ProvinceCode: %s
                    Task: Provide a realistic price range (min/max) and one suggested price within that range for a used EV battery pack in Vietnam.
                    """.formatted(nvl(r.getChemistryName()),
                nvl(r.getCapacityKwh()),
                nvl(r.getSohPercent()),
                nvl(r.getCycleCount()),
                nvl(r.getProvinceCode()))
                : """
                    ProductType: VEHICLE
                    Brand: %s
                    Model: %s
                    Year: %s
                    Odo(km): %s
                    ProvinceCode: %s
                    Task: Provide a realistic price range (min/max) and one suggested price within that range for a used EV/car in Vietnam.
                    """.formatted(nvl(r.getBrand()),
                nvl(r.getModel()),
                nvl(r.getYear()),
                nvl(r.getOdoKm()),
                nvl(r.getProvinceCode()));

        UserMessage userMessage = new UserMessage(userBlock);

        // 3) Prompt & gọi Gemini qua ChatClient
        String out = chatClient
                .prompt(new Prompt(systemMessage, userMessage))
                .call()
                .content();

        // 4) Parse & chuẩn hoá kết quả
        return parseToResponse(out);
    }

    // =================== Helpers ===================

    private static AiPriceResponse parseToResponse(String out) {
        BigDecimal min = extractNumber(out, "priceMinVND");
        BigDecimal max = extractNumber(out, "priceMaxVND");
        BigDecimal sug = extractNumber(out, "suggestedPriceVND");
        String note = extractNote(out);

        if (min.compareTo(max) > 0) { var t = min; min = max; max = t; }
        if (sug.compareTo(min) < 0) sug = min;
        if (sug.compareTo(max) > 0) sug = max;

        min = roundTo100k(min);
        max = roundTo100k(max);
        sug = roundTo100k(sug);

        AiPriceResponse resp = new AiPriceResponse();
        resp.setPriceMinVND(min);
        resp.setPriceMaxVND(max);
        resp.setSuggestedPriceVND(sug);
        resp.setNote(note != null ? note : "Gợi ý tự động.");
        return resp;
    }

    private static BigDecimal extractNumber(String content, String key) {
        Matcher m = Pattern.compile(key + ":\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(content);
        if (m.find()) return new BigDecimal(m.group(1));

        // fallback: nhặt số dài đầu tiên nếu thiếu key
        Matcher any = Pattern.compile("(\\d{6,})").matcher(content);
        if (any.find()) return new BigDecimal(any.group(1));

        return BigDecimal.ZERO;
    }

    private static String extractNote(String content) {
        Matcher m = Pattern.compile("note:\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content);
        return m.find() ? m.group(1).trim() : null;
    }

    private static BigDecimal roundTo100k(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO;
        return v.divide(new BigDecimal("100000"))
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100000"));
    }

    private static String nvl(Object o) {
        return o == null ? "" : String.valueOf(o);
    }
}
