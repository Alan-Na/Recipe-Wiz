package com.recipewiz.backend.mealplanning.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMealStatusRequest(@NotBlank String status) {
}
