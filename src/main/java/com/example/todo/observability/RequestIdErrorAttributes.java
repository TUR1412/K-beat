package com.example.todo.observability;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Component
public class RequestIdErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);
        Object requestId =
                webRequest.getAttribute(RequestIdFilter.REQUEST_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (requestId != null) {
            attributes.put("requestId", requestId.toString());
        }
        return attributes;
    }
}
