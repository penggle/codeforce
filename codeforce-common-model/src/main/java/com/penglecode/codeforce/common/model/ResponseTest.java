package com.penglecode.codeforce.common.model;

/**
 * @author peng2.peng
 * @version 1.0.0
 */
public class ResponseTest {

    public DefaultResponse<Example1> getExample1ById(Long id) {
        return DefaultResponse.<Example1>ok().data(new Example1()).build();
    }

    public QueryExample1Response getExample1ById(QueryExample1Request request) {
        return new QueryExample1Response();
    }

    public DefaultResponse<Example1> getExample1ById2(QueryExample1Request request) {
        return DefaultResponse.<Example1>ok().data(new Example1()).build();
    }

    public static class Example1 {

    }

    public static class Example2 {

    }

    public static class QueryExample1Response extends BaseResponse<Example1> {

    }

    public static class QueryExample1Request extends BaseRequest {

        private String id;

        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
