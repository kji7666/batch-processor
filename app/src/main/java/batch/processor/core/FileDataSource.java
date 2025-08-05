package batch.processor.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 檔案資料來源 (Concrete Component)
 * 這是裝飾器模式中最基礎、最核心的物件。
 * 它實現了對實體檔案的直接讀寫操作。
 */
public class FileDataSource implements IDataSource {

    private final String filePath;

    public FileDataSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        File file = new File(filePath);
        // 確保父目錄存在
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
        System.out.println("-> [FileDataSource]: Successfully wrote " + data.length + " bytes to file: " + filePath);
    }

    @Override
    public byte[] readData() throws IOException {
        byte[] data = Files.readAllBytes(new File(filePath).toPath());
        System.out.println("<- [FileDataSource]: Successfully read " + data.length + " bytes from file: " + filePath);
        return data;
    }
}