package com.bnpl.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.*;

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

        @CreatedBy
        String createdBy,

        @LastModifiedBy
        String lastModifiedBy,

        String category

)
{

        public static Property of(Long id, String title, String seller, Double price){
                return new Property(id, title, seller, price, 0, null, null, null, null, null);
        }

}
