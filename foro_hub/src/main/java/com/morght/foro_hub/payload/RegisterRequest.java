package com.morght.foro_hub.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
                @NotBlank String name,
                @NotBlank @Email String email,
                @NotBlank @Size(min = 8, max = 255) String password) {

}
