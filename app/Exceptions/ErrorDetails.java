package Exceptions;

public class ErrorDetails extends Throwable {

    private String tital;
    private String message;
    private Integer successCode;

    public ErrorDetails(){

    }

    public ErrorDetails(String tital, String message, Integer successCode) {
        this.tital = tital;
        this.message = message;
        this.successCode = successCode;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "tital='" + tital + '\'' +
                ", message='" + message + '\'' +
                ", successCode=" + successCode +
                '}';
    }

    public String getTital() {
        return tital;
    }

    public void setTital(String tital) {
        this.tital = tital;
    }

    public Integer getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(Integer successCode) {
        this.successCode = successCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
