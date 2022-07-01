package com.zeroway.user.dto;

<<<<<<< Updated upstream
=======
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
>>>>>>> Stashed changes
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpReq {
    private String email;
    private String nickname;
    private String password;
}
