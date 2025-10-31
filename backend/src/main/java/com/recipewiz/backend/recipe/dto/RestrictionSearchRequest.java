package com.recipewiz.backend.recipe.dto;

import java.util.List;

public record RestrictionSearchRequest(
        String foodName,
        List<String> dietLabels,
        List<String> healthLabels,
        List<String> cuisineTypes
) {
}
