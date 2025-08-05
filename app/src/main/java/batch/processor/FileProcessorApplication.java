package batch.processor;

import batch.processor.core.FileDataSource;
import batch.processor.core.IDataSource;
import batch.processor.factory.ProcessorFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileProcessorApplication {

    public static void main(String[] args) {
        // 模擬使用者的輸入
        String steps = "zip,encrypt"; // 嘗試更改順序，例如 "encrypt,zip" 或只用 "zip"
        String inputFilePath = "src/main/resources/input/my_document.txt";
        String outputFilePath = "src/main/resources/output/my_document.processed";
        String originalContent = "This is the secret message for the decorator pattern demo. It is a very powerful pattern!";

        // 1. 建立工廠
        ProcessorFactory factory = new ProcessorFactory();

        System.out.println("================== WRITING PROCESS (" + steps + ") ==================");

        try {
            // 2. 創建一個【原始的】檔案資料來源，作為被裝飾的核心
            IDataSource fileSource = new FileDataSource(outputFilePath);

            // 3. 使用工廠，根據步驟【動態地】創建出裝飾器鏈
            IDataSource processor = factory.createProcessor(fileSource, steps);

            // 4. 對外層的處理器寫入資料，內部會自動進行所有處理
            processor.writeData(originalContent.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("\n================== READING PROCESS (" + steps + ") ==================");
        try {
            // 5. 同樣，創建一個原始的檔案資料來源來讀取檔案
            IDataSource fileSource = new FileDataSource(outputFilePath);

            // 6. 再次使用工廠，以【完全相同】的步驟創建出裝飾器鏈
            // 這是關鍵，解碼的順序必須和編碼的順序完全相反，而裝飾器模式正好能保證這一點
            IDataSource processor = factory.createProcessor(fileSource, steps);
            
            // 7. 從外層的處理器讀取資料，內部會自動進行所有解碼
            byte[] readBytes = processor.readData();
            String recoveredContent = new String(readBytes, StandardCharsets.UTF_8);

            System.out.println("\n--- FINAL VERIFICATION ---");
            System.out.println("Original content:  " + originalContent);
            System.out.println("Recovered content: " + recoveredContent);
            
            // 8. 驗證還原的內容是否和原始內容完全一樣
            if (originalContent.equals(recoveredContent)) {
                System.out.println("SUCCESS: The content was recovered perfectly!");
            } else {
                System.out.println("FAILURE: The content does not match!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}