package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.CorrectDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.validation.executable.ValidateOnExecution;
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
