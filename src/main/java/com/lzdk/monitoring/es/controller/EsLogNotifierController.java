package com.lzdk.monitoring.es.controller;

import com.lzdk.monitoring.es.service.EsLogNotifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("eslog")
public class EsLogNotifierController {
    private final EsLogNotifierService esLogNotifierService;

    @PostMapping("notify")
    public void alert() {
        esLogNotifierService.esLogNotify();
    }
}
