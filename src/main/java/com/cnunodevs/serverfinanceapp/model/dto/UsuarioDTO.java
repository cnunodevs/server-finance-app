package com.cnunodevs.serverfinanceapp.model.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "username", "password", "authority"})
public class UsuarioDTO {

    @JsonProperty("id")
    private UUID id;

    @NotEmpty
    @JsonProperty("username")
    private String username;

    @NotEmpty
    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonProperty("authority")
	private String authority;

    private Boolean enabled;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean accountNonExpired;
    
}
