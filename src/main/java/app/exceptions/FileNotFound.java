package app.exceptions;

public class FileNotFound extends RuntimeException {
    private String filePath;

    public FileNotFound(String filePath) {
        super("");
        this.filePath = filePath;
    }

    @Override
    public String getMessage() {
        return "Не найден файл сохранения: " + filePath;
    }
}
