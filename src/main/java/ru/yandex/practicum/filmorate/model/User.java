package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@ValidateOnExecution
public class User {
    private int id;
    @Email(message = "Incorrect Email!")
    private String email;
    @NotBlank(message = "Incorrect login!")
    @Pattern(regexp = "^\\S*$", message = "Incorrect login!")
    private String login;

    private String name;

    @Past(message = "Incorrect date!")
    private LocalDate birthday;


}
