package com.fabiogomesrocha.dto;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "LoginDTO", description = "Credentials for User login")
public class LoginDTO {
    @NotBlank
    @Schema(description = "User Name", example = "username")
    public String username;

    @NotBlank
    @Schema(description = "User Password", example = "password")
    public String password;
}
