package batch.processor.factory;

import batch.processor.core.DataSourceDecorator;
import batch.processor.core.EncryptingDecorator;
import batch.processor.core.IDataSource;
import batch.processor.core.ZippingDecorator;

/**
 * 處理器工廠
 * 負責解析使用者輸入的處理步驟字串，並動態地將各種裝飾器組合起來，
 * 創建出一個最終的、功能完備的 IDataSource 處理器物件。
 */
public class ProcessorFactory {

    /**
     * 根據指定的步驟，創建一個由裝飾器層層包裹的處理器。
     *
     * @param source 原始的、最核心的資料來源 (例如 FileDataSource)
     * @param steps  以逗號分隔的處理步驟字串，例如 "zip,encrypt"
     * @return 一個組合了所有指定功能的 IDataSource 物件
     */
    public IDataSource createProcessor(IDataSource source, String steps) {
        // 如果步驟字串為空或 null，直接返回原始的 source，不做任何裝飾
        if (steps == null || steps.trim().isEmpty()) {
            return source;
        }

        // 關鍵：將裝飾器鏈的當前最外層物件保存在一個變數中
        IDataSource decoratedSource = source;
        
        // 按照使用者指定的順序，分割步驟
        String[] stepArray = steps.split(",");

        // 遍歷每個步驟，並包裹上對應的裝飾器
        for (String step : stepArray) {
            String trimmedStep = step.trim().toLowerCase(); // 轉換為小寫並去除空格

            switch (trimmedStep) {
                case "zip":
                    System.out.println("Factory: Wrapping with ZippingDecorator...");
                    decoratedSource = new ZippingDecorator(decoratedSource);
                    break;
                case "encrypt":
                    System.out.println("Factory: Wrapping with EncryptingDecorator...");
                    decoratedSource = new EncryptingDecorator(decoratedSource);
                    break;
                // case "upload":
                //     System.out.println("Factory: Wrapping with UploadingDecorator...");
                //     decoratedSource = new UploadingDecorator(decoratedSource);
                //     break;
                default:
                    System.err.println("Warning: Unknown processing step '" + step + "'. Skipping.");
                    break;
            }
        }

        return decoratedSource;
    }
}