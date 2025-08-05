package batch.processor.core;

import java.io.IOException;

/**
 * 抽象裝飾器 (Decorator)
 * 這是所有具體裝飾器（壓縮、加密等）的父類別。
 */
public abstract class DataSourceDecorator implements IDataSource {

    // 關鍵設計：持有一個被包裹的 IDataSource 物件的引用
    protected IDataSource wrappee;

    public DataSourceDecorator(IDataSource source) {
        this.wrappee = source;
    }

    /**
     * 預設行為：將 writeData 操作直接委派給被包裹的物件。
     * 具體的裝飾器會覆寫此方法，在委派之前或之後添加自己的處理邏輯。
     * @param data 要寫入的位元組陣列
     * @throws IOException 當寫入失敗時拋出
     */
    @Override
    public void writeData(byte[] data) throws IOException {
        wrappee.writeData(data);
    }

    /**
     * 預設行為：將 readData 操作直接委派給被包裹的物件。
     * 具體的裝飾器會覆寫此方法，對讀取到的資料進行加工處理。
     * @return 讀取到的位元組陣列
     * @throws IOException 當讀取失敗時拋出
     */
    @Override
    public byte[] readData() throws IOException {
        return wrappee.readData();
    }
}