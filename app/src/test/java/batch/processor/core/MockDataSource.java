package batch.processor.core;

import java.io.IOException;

/**
 * 用於測試的 Mock 資料來源
 * 它不進行任何真實的檔案 I/O，只在記憶體中儲存資料。
 */
public class MockDataSource implements IDataSource {

    private byte[] data = null;

    @Override
    public void writeData(byte[] data) throws IOException {
        this.data = data;
        System.out.println("MockDataSource: Stored " + (data == null ? 0 : data.length) + " bytes in memory.");
    }

    @Override
    public byte[] readData() throws IOException {
        System.out.println("MockDataSource: Retrieved " + (data == null ? 0 : data.length) + " bytes from memory.");
        return this.data;
    }
}