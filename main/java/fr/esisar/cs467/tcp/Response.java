package fr.esisar.cs467.tcp;

import java.util.Map;

public class Response {
	
	private String httpVersion = "HTTP/1.1";
	private ResponseHttpCode responseHttpCode;
	private Map<String, String> headers;
    private String body;
    
    
	public Response(ResponseHttpCode responseHttpCode, Map<String, String> headers, String body) {
		this.responseHttpCode = responseHttpCode;
		this.headers = headers;
		this.body = body;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public ResponseHttpCode getResponseHttpCode() {
		return responseHttpCode;
	}

	public void setResponseHttpCode(ResponseHttpCode responseHttpCode) {
		this.responseHttpCode = responseHttpCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
    @Override
    public String toString() {
        StringBuilder responseBuilder = new StringBuilder();

        responseBuilder.append(httpVersion)
                       .append(" ")
                       .append(responseHttpCode.getCode())
                       .append(" ")
                       .append(responseHttpCode.getDescription())
                       .append("\r\n");

        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                responseBuilder.append(entry.getKey())
                               .append(": ")
                               .append(entry.getValue())
                               .append("\r\n");
            }
        }

        responseBuilder.append("\r\n");

        if (body != null && !body.isEmpty()) {
            responseBuilder.append(body);
        }

        return responseBuilder.toString();
    }
}