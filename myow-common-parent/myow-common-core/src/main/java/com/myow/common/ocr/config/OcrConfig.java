package com.myow.common.ocr.config;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 解析引擎配置。所有几何阈值均以 <b>PDF 点</b>为单位，或以字号为基准的相对系数，
 * 从而在不同缩放比例 / 不同 DPI 的扫描件上保持一致行为。
 */
public class OcrConfig {

    // ---------------- OCR ----------------
    /** Tesseract 语言包目录，如 /opt/homebrew/share/tessdata */
    private String tessDataPath = System.getenv("TESSDATA_PREFIX");
    /** 识别语言 */
    private String language = "eng";
    /** PDF 转图片的渲染 DPI，300 是精度与性能的平衡点 */
    private int ocrDpi = 300;
    /** Tesseract PSM：6 = 假定为统一的文本块，最适合表单 */
    private int pageSegMode = 6;
    /** OCR 引擎模式：1 = LSTM */
    private int engineMode = 1;
    /** 单词最低置信度，低于此值丢弃 */
    private float minWordConfidence = 30f;
    /** 是否对图片做二值化预处理 */
    private boolean binarize = true;

    // ---------------- 通道选择 ----------------
    /** 文本层词数低于该阈值时判定为扫描件，自动降级 OCR */
    private int textLayerMinWords = 20;
    /** 允许在文本层不足时回退 OCR */
    private boolean ocrFallbackEnabled = true;

    // ---------------- 版面几何 ----------------
    /** 行聚类的最小垂直重叠比例 */
    private float lineOverlapRatio = 0.4f;
    /** 词切分：绝对最小间距 */
    private float wordGapMin = 2.0f;
    /** 词切分：字号系数（取 max(wordGapMin, wordGapFontRatio * fontSize)） */
    private float wordGapFontRatio = 0.30f;
    /** 单元格切分：绝对最小间距 */
    private float cellGapMin = 8.0f;
    /** 单元格切分：字号系数 */
    private float cellGapFontRatio = 1.5f;
    /** 取值行相对标签行允许的最大纵向间距（字号倍数） */
    private float valueRowMaxGapRatio = 3.0f;
    /** 判定为直线的最大线宽/线长比容差 */
    private float ruleLineTolerance = 3.0f;
    /**
     * 是否使用矢量表格线增强列切分。
     * <p>关闭后引擎完全依赖标签中点几何推断——等价于 OCR 通道的运行条件，
     * 可用于回归测试"无表格线时解析是否仍然正确"。
     */
    private boolean useVectorRules = true;

    public static OcrConfig defaults() {
        return new OcrConfig();
    }

    // ---------------- 链式 setter ----------------

    public String getTessDataPath() {
        return tessDataPath;
    }

    public OcrConfig setTessDataPath(String tessDataPath) {
        this.tessDataPath = tessDataPath;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public OcrConfig setLanguage(String language) {
        this.language = language;
        return this;
    }

    public int getOcrDpi() {
        return ocrDpi;
    }

    public OcrConfig setOcrDpi(int ocrDpi) {
        this.ocrDpi = ocrDpi;
        return this;
    }

    public int getPageSegMode() {
        return pageSegMode;
    }

    public OcrConfig setPageSegMode(int pageSegMode) {
        this.pageSegMode = pageSegMode;
        return this;
    }

    public int getEngineMode() {
        return engineMode;
    }

    public OcrConfig setEngineMode(int engineMode) {
        this.engineMode = engineMode;
        return this;
    }

    public float getMinWordConfidence() {
        return minWordConfidence;
    }

    public OcrConfig setMinWordConfidence(float minWordConfidence) {
        this.minWordConfidence = minWordConfidence;
        return this;
    }

    public boolean isBinarize() {
        return binarize;
    }

    public OcrConfig setBinarize(boolean binarize) {
        this.binarize = binarize;
        return this;
    }

    public int getTextLayerMinWords() {
        return textLayerMinWords;
    }

    public OcrConfig setTextLayerMinWords(int textLayerMinWords) {
        this.textLayerMinWords = textLayerMinWords;
        return this;
    }

    public boolean isOcrFallbackEnabled() {
        return ocrFallbackEnabled;
    }

    public OcrConfig setOcrFallbackEnabled(boolean ocrFallbackEnabled) {
        this.ocrFallbackEnabled = ocrFallbackEnabled;
        return this;
    }

    public float getLineOverlapRatio() {
        return lineOverlapRatio;
    }

    public OcrConfig setLineOverlapRatio(float lineOverlapRatio) {
        this.lineOverlapRatio = lineOverlapRatio;
        return this;
    }

    public float getWordGapMin() {
        return wordGapMin;
    }

    public OcrConfig setWordGapMin(float wordGapMin) {
        this.wordGapMin = wordGapMin;
        return this;
    }

    public float getWordGapFontRatio() {
        return wordGapFontRatio;
    }

    public OcrConfig setWordGapFontRatio(float wordGapFontRatio) {
        this.wordGapFontRatio = wordGapFontRatio;
        return this;
    }

    public float getCellGapMin() {
        return cellGapMin;
    }

    public OcrConfig setCellGapMin(float cellGapMin) {
        this.cellGapMin = cellGapMin;
        return this;
    }

    public float getCellGapFontRatio() {
        return cellGapFontRatio;
    }

    public OcrConfig setCellGapFontRatio(float cellGapFontRatio) {
        this.cellGapFontRatio = cellGapFontRatio;
        return this;
    }

    public float getValueRowMaxGapRatio() {
        return valueRowMaxGapRatio;
    }

    public OcrConfig setValueRowMaxGapRatio(float valueRowMaxGapRatio) {
        this.valueRowMaxGapRatio = valueRowMaxGapRatio;
        return this;
    }

    public float getRuleLineTolerance() {
        return ruleLineTolerance;
    }

    public OcrConfig setRuleLineTolerance(float ruleLineTolerance) {
        this.ruleLineTolerance = ruleLineTolerance;
        return this;
    }

    public boolean isUseVectorRules() {
        return useVectorRules;
    }

    public OcrConfig setUseVectorRules(boolean useVectorRules) {
        this.useVectorRules = useVectorRules;
        return this;
    }

    /** 给定字号下的词切分阈值 */
    public float wordGapThreshold(float fontSize) {
        return Math.max(wordGapMin, wordGapFontRatio * fontSize);
    }

    /** 给定字号下的单元格切分阈值 */
    public float cellGapThreshold(float fontSize) {
        return Math.max(cellGapMin, cellGapFontRatio * fontSize);
    }
}
