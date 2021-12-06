package com.idolcollector.idolcollector.web.dto.member;

import com.idolcollector.idolcollector.domain.rank.Ranks;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveRequestDto {

    @NotBlank
    private String nickName;

    @NotBlank
    private String email;

    @NotBlank
    private String pwd;

    @NotBlank
    private String name;

    @Nullable
    private String picture;

    @NotBlank
    private LocalDateTime dateOfBirth;

}
