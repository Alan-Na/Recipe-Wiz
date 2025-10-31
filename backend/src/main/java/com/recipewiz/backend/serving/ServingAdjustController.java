package com.recipewiz.backend.serving;

import com.recipewiz.backend.recipe.dto.RecipeDto;
import com.recipewiz.backend.serving.dto.ServingAdjustRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servings")
public class ServingAdjustController {

    private final ServingAdjustService servingAdjustService;

    public ServingAdjustController(ServingAdjustService servingAdjustService) {
        this.servingAdjustService = servingAdjustService;
    }

    @PostMapping("/adjust")
    public List<RecipeDto> adjustServings(@Valid @RequestBody ServingAdjustRequest request) {
        return servingAdjustService.adjustServings(request);
    }
}
