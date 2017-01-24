package omc;

import java.util.List;

public class LoadLibraryException extends Exception {
    public final List<String> errors;

    public LoadLibraryException(String cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }
}
