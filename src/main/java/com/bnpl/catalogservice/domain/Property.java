package com.bnpl.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

public record Property(
        @Id
        @NotNull(message = "The property id must be defined.")
        @Positive(message = "The property id must be greater than zero")
        Long id,
        @NotBlank(message = "The property title must be defined.")
        String title,
        @NotBlank(message = "The property seller must be defined.")
        String seller,
        @NotNull(message = "The property price must be defined.")
        @Positive(message = "The property price must be greater than zero")
        Double price,

        @Version
        int version,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        String category

)
{

        public static Property of(Long id, String title, String seller, Double price){
                return new Property(id, title, seller, price, 0, null, null, null);
        }

}
