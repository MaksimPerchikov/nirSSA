package ru.nir.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//тип краткое описания пользователя
public class UserSumm {

    private String id;
    private String username;
    private String name;
    private String profilePicture;
}
