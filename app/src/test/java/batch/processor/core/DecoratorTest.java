package batch.processor.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Decorator Core Logic Test")
class DecoratorTest {

    private final String originalContent = "Hello, Decorator Pattern!";
    private final byte[] originalBytes = originalContent.getBytes(StandardCharsets.UTF_8);
    private MockDataSource mockDataSource;

    @BeforeEach
    void setUp() {
        mockDataSource = new MockDataSource();
    }

    @Test
    @DisplayName("ZippingDecorator 應能正確壓縮和解壓縮資料")
    void zippingDecoratorShouldCompressAndDecompressCorrectly() throws IOException {
        // Arrange
        // 將 MockDataSource 包裹在 ZippingDecorator 中
        IDataSource zipper = new ZippingDecorator(mockDataSource);

        // Act
        // 1. 寫入時，zipper 應壓縮資料，然後將【壓縮後】的資料存入 mockDataSource
        zipper.writeData(originalBytes);
        
        // 2. 讀取時，zipper 應從 mockDataSource 讀取【壓縮後】的資料，然後解壓縮返回
        byte[] recoveredBytes = zipper.readData();
        String recoveredContent = new String(recoveredBytes, StandardCharsets.UTF_8);

        // Assert
        // 1. 驗證 mockDataSource 中儲存的是被壓縮過的資料，它不應該等於原始資料
        assertThat(mockDataSource.readData()).isNotEqualTo(originalBytes);
        
        // 2. 驗證經過裝飾器完整讀寫流程後，資料能被完美還原
        assertThat(recoveredContent).isEqualTo(originalContent);
    }

    @Test
    @DisplayName("EncryptingDecorator 應能正確加密和解密資料")
    void encryptingDecoratorShouldEncryptAndDecryptCorrectly() throws IOException {
        // Arrange
        IDataSource encrypter = new EncryptingDecorator(mockDataSource);

        // Act
        encrypter.writeData(originalBytes);
        byte[] recoveredBytes = encrypter.readData();
        String recoveredContent = new String(recoveredBytes, StandardCharsets.UTF_8);

        // Assert
        assertThat(mockDataSource.readData()).isNotEqualTo(originalBytes); // 驗證儲存的是加密資料
        assertThat(recoveredContent).isEqualTo(originalContent); // 驗證資料被還原
    }

    @Test
    @DisplayName("組合裝飾器 (壓縮->加密) 應能正確處理資料")
    void combinedDecoratorsShouldProcessDataCorrectly() throws IOException {
        // Arrange
        // 層層嵌套：將 MockDataSource 先用 ZippingDecorator 包裹，再用 EncryptingDecorator 包裹
        IDataSource processor = new EncryptingDecorator(new ZippingDecorator(mockDataSource));

        // Act
        processor.writeData(originalBytes);
        byte[] recoveredBytes = processor.readData();
        String recoveredContent = new String(recoveredBytes, StandardCharsets.UTF_8);

        // Assert
        // 驗證 mockDataSource 中儲存的是被處理過的資料
        assertThat(mockDataSource.readData()).isNotNull();
        assertThat(mockDataSource.readData()).isNotEqualTo(originalBytes);
        
        // 最重要的驗證：無論經過多少層裝飾，最終資料都應能被完美還原
        assertThat(recoveredContent).isEqualTo(originalContent);
    }
}