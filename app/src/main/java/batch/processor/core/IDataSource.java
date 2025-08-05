package batch.processor.core;

import java.io.IOException;

/**
 * 資料來源介面 (Component)
 * 這是裝飾器模式中的核心抽象元件。
 * 它定義了所有資料來源物件（無論是否被裝飾）都必須具備的行為。
 */
public interface IDataSource {

    /**
     * 將資料寫入到目標。
     * @param data 要寫入的位元組陣列
     * @throws IOException 當寫入失敗時拋出
     */
    void writeData(byte[] data) throws IOException;

    /**
     * 從來源讀取資料。
     * @return 讀取到的位元組陣列
     * @throws IOException 當讀取失敗時拋出
     */
    byte[] readData() throws IOException;
}