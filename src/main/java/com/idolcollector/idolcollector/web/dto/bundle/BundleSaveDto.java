package com.idolcollector.idolcollector.web.dto.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BundleSaveDto {

    private Long memberId;

    @NotBlank
    private String title;
    private String description;
}
