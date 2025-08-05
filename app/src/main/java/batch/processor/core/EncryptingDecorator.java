package batch.processor.core;

import java.io.IOException;

/**
 * 加密裝飾器 (Concrete Decorator)
 * 負責在寫入資料前對其進行加密，在讀取資料後對其進行解密。
 * 我們使用簡單的 XOR 加密來演示。
 */
public class EncryptingDecorator extends DataSourceDecorator {

    // 一個簡單的 XOR 加密金鑰
    private static final byte ENCRYPTION_KEY = (byte) 0xAB;

    public EncryptingDecorator(IDataSource source) {
        super(source);
    }

    /**
     * 覆寫 writeData 方法。
     * 在將資料傳遞給下一層（wrappee）之前，先對其進行加密。
     */
    @Override
    public void writeData(byte[] data) throws IOException {
        System.out.println("-> [EncryptingDecorator]: Encrypting data...");
        byte[] encryptedData = xorEncryptDecrypt(data);
        // 將【加密後】的資料，寫入到被它包裹的物件中
        super.writeData(encryptedData);
    }

    /**
     * 覆寫 readData 方法。
     * 先從下一層（wrappee）讀取資料（這些資料是已經被加密過的），然後對其進行解密。
     */
    @Override
    public byte[] readData() throws IOException {
        // 從被它包裹的物件中，讀取【加密過】的資料
        byte[] encryptedData = super.readData();
        System.out.println("<- [EncryptingDecorator]: Decrypting data...");
        byte[] decryptedData = xorEncryptDecrypt(encryptedData);
        return decryptedData;
    }

    /**
     * 簡單的 XOR 加密/解密演算法。
     * 將資料中的每個位元組與金鑰進行 XOR (異或) 運算。
     * XOR 的特性是，對一個資料進行兩次相同的 XOR 運算，會還原成原始資料。
     * (A XOR B) XOR B = A
     * @param data 要處理的資料
     * @return 處理後的資料
     */
    private byte[] xorEncryptDecrypt(byte[] data) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ ENCRYPTION_KEY);
        }
        return result;
    }
}