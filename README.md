# File Batch Processor (可擴充的檔案批次處理工具)

## 專案簡介 (Project Introduction)

這是一個以實踐**裝飾器模式 (Decorator Pattern)** 為核心的 Java 專案。

本專案旨在建構一個輕量級但高度可擴充的檔案批次處理工具。該工具能夠接收一個資料夾路徑和一系列處理步驟指令（如「壓縮」、「加密」），然後按照使用者指定的順序，對資料夾中的檔案執行這些處理。

其核心價值在於展示如何利用裝飾器模式，將獨立的功能（如壓縮、加密、上傳等）像積木一樣**動態地、以任意順序組合**起來，而無需為每種功能組合建立一個新的子類別，從而完美地遵循了**開閉原則 (Open-Closed Principle)**。

## 核心場景 (Core Scenario)

模擬一個命令列工具的執行流程：

> 一位使用者希望將一個重要檔案安全地備份到雲端。他希望的處理流程是：**先將檔案進行 ZIP 壓縮以減小體積，然後再對壓縮後的檔案進行加密以保證安全。**

如果另一位使用者有不同的需求，例如「先加密，再壓縮」，本工具的架構也必須能夠輕鬆應對，而無需修改任何既有的處理邏輯程式碼。

## 設計模式應用 (Design Patterns Used)

### 1. 裝飾器模式 (Decorator Pattern) - 專案的靈魂

裝飾器模式是整個專案的架構核心，它優雅地解決了功能動態組合的問題。

- **抽象元件 (Component)**: `IDataSource` 介面。它定義了所有資料來源的共通行為（`writeData`, `readData`），是所有「洋蔥層」的統一介面。
- **具體元件 (Concrete Component)**: `FileDataSource`。這是「洋蔥」的最核心，代表了最基礎的功能：直接對檔案進行讀寫。
- **抽象裝飾器 (Decorator)**: `DataSourceDecorator`。它是一個抽象類別，遵循了與 `IDataSource` 相同的介面，並且內部持有一個 `IDataSource` 的引用（指向被它包裹的物件）。
- **具體裝飾器 (Concrete Decorator)**: `ZippingDecorator`, `EncryptingDecorator`。它們是真正的「功能外衣」。每個具體裝飾器都覆寫了父類別的方法，在將任務委派給內部被包裹的物件之前或之後，加上了自己的處理邏輯（壓縮/解壓縮，加密/解密）。

**帶來的好處**：極致的靈活性。我們可以透過簡單的物件嵌套，如 `new EncryptingDecorator(new ZippingDecorator(new FileDataSource(...)))`，來在執行時動態地建立出功能強大的處理鏈。

### 2. 工廠模式 (Factory Pattern) / 命令模式 (Command Pattern) 的融合

- **應用**: `ProcessorFactory` 類別。
- **職責**: 它扮演了一個「組裝工廠」的角色。它解析使用者輸入的指令字串（如 `"zip,encrypt"`），並將其翻譯成一系列的裝飾器嵌套操作，最終回傳一個功能完備的 `IDataSource` 物件。這將「使用者指令」與「物件的創建和組合過程」進行了有效的解耦。

## 專案結構 (Project Structure)

```
src/main/java/com/batch/processor/
├── core/         # 裝飾器模式的核心實現
│   ├── IDataSource.java           # 抽象元件 (Component)
│   ├── FileDataSource.java        # 具體元件 (Concrete Component)
│   ├── DataSourceDecorator.java   # 抽象裝飾器 (Decorator)
│   ├── ZippingDecorator.java      # 具體裝飾器 - 壓縮
│   └── EncryptingDecorator.java   # 具體裝飾器 - 加密
│
└── factory/      # 負責解析指令並建構裝飾器鏈的工廠
    └── ProcessorFactory.java
```

## 如何執行 (How to Run)

### 環境需求 (Prerequisites)
- JDK 17 或更高版本
- Gradle 8.0 或更高版本

### 執行範例 (Running the Example)

本專案的 `FileProcessorApplication.java` 包含一個 `main` 方法，清晰地展示了如何：
1.  定義一個處理步驟字串（例如 `"zip,encrypt"`）。
2.  使用 `ProcessorFactory` 來根據字串動態建構一個處理器。
3.  使用該處理器寫入（加工）資料，然後再讀取（還原）資料。
4.  驗證處理後的資料是否能被完美地還原成原始內容。

您可以直接在 IDE 中執行此檔案，並嘗試修改 `steps` 字串來觀察不同的處理組合及其結果。

### 執行單元測試 (Running Tests)

專案包含了對各個裝飾器和工廠的單元測試，驗證了其功能的正確性和組合的靈活性。
```bash
# For Windows
.\gradlew test

# For macOS / Linux
./gradlew test
```
