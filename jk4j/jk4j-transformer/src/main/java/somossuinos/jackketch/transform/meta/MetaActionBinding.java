package somossuinos.jackketch.transform.meta;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;
import somossuinos.jackketch.transform.BindingSeparator;

public class MetaActionBinding {

    @SerializedName("package")
    private String pkg;

    public String getPackageName() {
        return pkg;
    }

    @SerializedName("class")
    private String cls;

    public String getClassName() {
        return cls;
    }

    @SerializedName("method")
    private String method;

    public String getMethodName() {
        return method;
    }

    public String toBinding() {
        final String aPackage = (StringUtils.isBlank(this.pkg)) ? StringUtils.EMPTY : this.pkg.trim();
        final String aClass = (StringUtils.isBlank(this.cls)) ? StringUtils.EMPTY : this.cls.trim();
        final String aMethod = (StringUtils.isBlank(this.method)) ? StringUtils.EMPTY : this.method.trim();

        final StringBuilder builder = new StringBuilder()
                .append(aPackage)
                .append(StringUtils.isNotBlank(aPackage) ? BindingSeparator.PACKAGE_FROM_CLASS.getSeparator() : StringUtils.EMPTY)
                .append(aClass)
                .append(BindingSeparator.CLASS_FROM_METHOD.getSeparator())
                .append(aMethod);

        return builder.toString();
    }

}
