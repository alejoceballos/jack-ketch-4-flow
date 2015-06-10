package somossuinos.jackketch.transform;

public enum BindingSeparator {

    PACKAGE_FROM_CLASS("."),
    CLASS_FROM_METHOD("::");

    private String separator;

    BindingSeparator(final String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
