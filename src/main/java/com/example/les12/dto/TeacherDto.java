// In TeacherDto komt de server-side validatie
// Een Data Transfer Object kan dus gebruikt worden om de binnenkomende datastructuur onafhankelijk te maken van de domeinenmodellen.
// Een Data Transfer Object is een soort tussenklasse. Bij een Request moet de gebruiker de gegevens aanbieden volgens de structuur van de DTO.
package com.example.les12.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class TeacherDto {
    public Long id;

    @NotBlank(message = "leeg") // Deze validatie velden werken waarschijnlijk samen met @Valid
    public String firstName;

    @Size(min=2,max=30, message = "Size must be between 2 and 30 characters") // Bericht in het Engels!
    public String lastName;

    // Het geeft geen melding als bijvoorbeeld jaartal niet goed is ingevoerd
    @Past(message = "Dates may not be in the future!")
//    @Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4}", flags = Pattern.Flag.UNICODE_CASE, message = "Date must be in form: yyyy-mm-dd")
    public LocalDate dob;

    @Min(0)
    @Max(100_000)
    public int salary;
}
