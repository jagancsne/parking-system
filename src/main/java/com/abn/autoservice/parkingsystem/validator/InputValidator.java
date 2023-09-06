package com.abn.autoservice.parkingsystem.validator;

import com.abn.autoservice.parkingsystem.exception.InvalidInputException;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

import static com.abn.autoservice.parkingsystem.constant.Constants.INVALID_INPUT_CODE;

@Component
public class InputValidator {
    private final Validator validator;

    public InputValidator() {
        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class)
                .configure().messageInterpolator(new ResourceBundleMessageInterpolator(
                        new PlatformResourceBundleLocator("validation-messages")
                ));
        ValidatorFactory validatorFactory = configuration.buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public <T> void validateRequestParams(T request) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            Optional<ConstraintViolation<T>> constraintViolationOptional = constraintViolations.stream().findAny();
            constraintViolationOptional.ifPresent(v -> {
                String errorMessage = v.getPropertyPath().toString() + " : " + v.getMessage();
                throw new InvalidInputException(errorMessage, INVALID_INPUT_CODE);
            });
        }
    }
}
