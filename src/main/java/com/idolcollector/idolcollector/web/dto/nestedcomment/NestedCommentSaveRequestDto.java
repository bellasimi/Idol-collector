package com.idolcollector.idolcollector.web.dto.nestedcomment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NestedCommentSaveRequestDto {

    @NotNull
    private Long authorId;

    @NotNull
    private Long commentId;

    @NotBlank
    private String content;

}
