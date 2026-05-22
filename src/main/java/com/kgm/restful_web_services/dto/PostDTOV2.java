package com.kgm.restful_web_services.dto;


import com.kgm.restful_web_services.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "PostDTO Model Information"
)
public class PostDTOV2 {

    private Long id;

    @Schema(
            description = "Blog Post Title"
    )
    @NotEmpty
    @Size(min=2,message = "Post title should have at least 2 characters")
    private String title;

    @Schema(
            description = "Blog Post Description"
    )
    @NotEmpty
    @Size(min=10,message = "Post description should have at least 10 characters")
    private String description;

    @Schema(
            description = "Blog Post Comments"
    )
    private Set<Comment> comments;

    @Schema(
            description = "Blog Post Category"
    )
    private Long categoryId;

    private List<String> tags;
}