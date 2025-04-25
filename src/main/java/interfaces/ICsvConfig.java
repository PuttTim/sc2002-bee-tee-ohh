package interfaces;

import java.util.List;

public interface ICsvConfig {
    List<String> getHeaders();
    String getFilePath();
}
