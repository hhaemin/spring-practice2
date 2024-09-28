package com.github.supercoding.web.dto.items;

import io.swagger.v3.oas.annotations.media.Schema;

public class Spec {

    @Schema(name = "cpu", description = "Item cpu", example = "Google Tensor")private String cpu;
    @Schema(name = "capacity", description = "Item 용량 Spec", example = "25G")private String capacity;

    public Spec() {
    }

    public Spec(String cpu, String capacity) {
        this.cpu = cpu;
        this.capacity = capacity;
    }

    public String getCpu() {
        return cpu;
    }

    public String getCapacity() {
        return capacity;
    }
}
