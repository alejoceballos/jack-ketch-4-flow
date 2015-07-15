package somossuinos.jackketch.webclient.api;

public class ApiResult {
    private String status;
    private Object data;

    private ApiResult(final ApiResultStatusType status) {
        this.status = status.name();
    }

    public ApiResultStatusType getStatus() {
        return ApiResultStatusType.valueOf(this.status);
    }

    public Object getData() {
        return data;
    }

    public static ApiResultBuilder build(final ApiResultStatusType status) {
        return new ApiResultBuilder(new ApiResult(status));
    }

    public static class ApiResultBuilder {
        private ApiResult result;

        protected ApiResultBuilder(final ApiResult result) {
            this.result = result;
        }

        public ApiResult withData(final Object data) {
            this.result.data = data;
            return this.result;
        }
    }

}
