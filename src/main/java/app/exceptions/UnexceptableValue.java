package app.exceptions;

public class UnexceptableValue extends RuntimeException {
  public UnexceptableValue(String message) {
    super(message);
  }
}
