package model.validation;

import model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ClientValidator {
    private static final String NAME_VALIDATION_REGEX = "^[\\p{L} .'-]+$";
    private static final String PNC_VALIDATION_REGEX = "[0-9]+";
    private final Client client;
    private final List<String> errors;

    public ClientValidator(Client client){
        this.client = client;
        errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean validate() {
        validateName(client.getName());
        validatePnc(client.getPnc());
        return errors.isEmpty();
    }

    private void validateName(String name) {
        if (!Pattern.compile(NAME_VALIDATION_REGEX).matcher(name).matches()) {
            errors.add("The name should contain only letters!");
        }
    }

    private void validatePnc(String pnc) {
        if (!Pattern.compile(PNC_VALIDATION_REGEX).matcher(pnc).matches()) {
            errors.add("The PNC should contain only numbers!");
        }
    }
}
