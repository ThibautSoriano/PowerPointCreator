package main.java.datasdownloading;

public class HttpMessage {

    private boolean ok;

    private String errorMessage;
    
    private String content;

    public boolean isOk() {
        return ok;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    
    
    public String getContent() {
        return content;
    }

    public HttpMessage(boolean ok, String errorMessage, String content) {
        super();
        this.ok = ok;
        this.errorMessage =errorMessage;
        this.content = content;
    }

}
