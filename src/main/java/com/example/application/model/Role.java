package com.example.application.model;


import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table("role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    @Id
    @NotNull
    @Nonnull
    private Long id;

    @NotBlank
    @Nonnull
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
