package ru.tokmakov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
