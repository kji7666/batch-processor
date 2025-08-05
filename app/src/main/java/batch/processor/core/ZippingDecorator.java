package batch.processor.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * 壓縮裝飾器 (Concrete Decorator)
 * 負責在寫入資料前對其進行壓縮，在讀取資料後對其進行解壓縮。
 */
public class ZippingDecorator extends DataSourceDecorator {

    public ZippingDecorator(IDataSource source) {
        super(source);
    }

    /**
     * 覆寫 writeData 方法。
     * 在將資料傳遞給下一層（wrappee）之前，先對其進行壓縮。
     */
    @Override
    public void writeData(byte[] data) throws IOException {
        System.out.println("-> [ZippingDecorator]: Compressing data...");
        byte[] compressedData = compress(data);
        System.out.println("-> [ZippingDecorator]: Original size: " + data.length + ", Compressed size: " + compressedData.length);
        // 將【壓縮後】的資料，寫入到被它包裹的物件中
        super.writeData(compressedData);
    }

    /**
     * 覆寫 readData 方法。
     * 先從下一層（wrappee）讀取資料（這些資料是已經被壓縮過的），然後對其進行解壓縮。
     */
    @Override
    public byte[] readData() throws IOException {
        // 從被它包裹的物件中，讀取【壓縮過】的資料
        byte[] compressedData = super.readData();
        System.out.println("<- [ZippingDecorator]: Decompressing data...");
        byte[] decompressedData = decompress(compressedData);
        System.out.println("<- [ZippingDecorator]: Compressed size: " + compressedData.length + ", Decompressed size: " + decompressedData.length);
        return decompressedData;
    }

    // --- 私有輔助方法 ---

    private byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
            dos.write(data);
        }
        return baos.toByteArray();
    }

    private byte[] decompress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(data))) {
            baos.writeBytes(iis.readAllBytes());
        }
        return baos.toByteArray();
    }
}