package com.example.maatjes.dtos.inputDtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInputDto {
    @NotNull(message = "Beoordeling moet worden ingevuld")
    @Min(value = 0, message = "Beoordeling moet een positief getal zijn")
    @Max(value = 5, message = "Beoordeling kan maximaal een 5 zijn")
    public double rating;
    public String description;
    public boolean verified = false;
    public Long matchId;
    //todo private String feedbackAdmin?;
}




