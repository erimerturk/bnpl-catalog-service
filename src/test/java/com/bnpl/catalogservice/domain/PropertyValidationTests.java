package com.bnpl.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var property = Property.of(1l, "Title", "seller", 9.90);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIdNotDefinedThenValidationFails() {
        var property = Property.of(null, "Title", "seller", 9.90);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The property id must be defined.");
    }


    @Test
    void whenTitleIsNotDefinedThenValidationFails() {
        var property = Property.of(1l, "", "seller", 9.90);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The property title must be defined.");
    }

    @Test
    void whenSellerIsNotDefinedThenValidationFails() {
        var property = Property.of(1l, "title", "", 9.90);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The property seller must be defined.");
    }

    @Test
    void whenPriceIsNotDefinedThenValidationFails() {
        var property = Property.of(1l, "title", "seller", null);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The property price must be defined.");
    }

    @Test
    void whenPriceDefinedButZeroThenValidationFails() {
        var property = Property.of(1l, "title", "seller", 0.0);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The property price must be greater than zero");
    }

    @Test
    void whenPriceDefinedButNegativeThenValidationFails() {
        var property = Property.of(1l, "title", "seller", -9.90);
        Set<ConstraintViolation<Property>> violations = validator.validate(property);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The property price must be greater than zero");
    }
}
