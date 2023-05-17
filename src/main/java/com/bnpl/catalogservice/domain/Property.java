package com.bnpl.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record Property(
        @NotNull(message = "The property id must be defined.")
        @Positive(message = "The property id must be greater than zero")
        Long id,
        @NotBlank(message = "The property title must be defined.")
        String title,
        @NotBlank(message = "The property seller must be defined.")
        String seller,
        @NotNull(message = "The property price must be defined.")
        @Positive(message = "The property price must be greater than zero")
        Double price)
{ }
