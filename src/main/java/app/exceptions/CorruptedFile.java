package app.exceptions;

public class CorruptedFile extends RuntimeException {
  public CorruptedFile(String message) {
    super(message);
  }
}
