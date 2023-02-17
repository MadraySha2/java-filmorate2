package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.executable.ValidateOnExecution;
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
