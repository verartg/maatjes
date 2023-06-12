package com.example.maatjes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInputDto {
    public Long id;
    public float rating;
    @NotBlank
    public String description;
    //    private account writtenBy; <-- dit wordt een relatie
//    private account writtenAbout; <-- dit wordt een relatie
    public boolean verified;
}
