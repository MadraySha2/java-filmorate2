package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.CorrectDate;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@ValidateOnExecution
public class Film {

    int id;
    @NotBlank(message = "Empty name!")
    private String name;
    @Size(min = 2, max = 200, message = "Description should be not empty & less then 200")
    private String description;
    @PastOrPresent(message = "Incorrect date!")
    @CorrectDate
    private LocalDate releaseDate;
    @Positive
    private Long duration;
}
