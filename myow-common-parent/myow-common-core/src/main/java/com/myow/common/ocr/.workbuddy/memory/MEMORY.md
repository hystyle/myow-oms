# 项目长期记忆：DO（Delivery Order）单据智能解析模块

## 目标
Java 工具/服务，解析物流 DO 单据（原生 PDF 文本层 + 扫描件 OCR），
提取 **21 个核心字段**，输出严格匹配目标的 JSON。
样例：`/Volumes/yss/agentic/file/EZI-0022777-9-DO-1.pdf`

## 技术栈
Java 17（运行时）/ 8+ 兼容；Maven 3.9.11；Apache PDFBox 2.0.30；
Tess4J 5.8.0（OCR 回退通道，依赖本机 Tesseract native，当前本机未装）；Jackson。

## 核心设计主张
**双通道复用同一套几何解析逻辑**：PDF 文本层与 OCR 输出映射到同一个
`PageLayout` 坐标空间（左上原点、pt）。统一版面模型是"扫描件抗抖动"成立的前提。
- 解析引擎：`engine/GridResolver`（标签锚定网格解析：标签定位→列边界推断→向下取值→内聚修复）
- 单元格 `TextLine.Cell` 为"抖动打不散的原子"，由"词距远小于留白"聚出。
- `LineAssembler` 负责行聚类与并排残片合并。

## 关键文件
- `DeliveryOrderParser.java` — 入口（parse / toJson）
- `engine/GridResolver.java` — 网格解析 + 取行守卫
- `engine/DeliveryOrderMapper.java` — 标签→字段映射
- `layout/TextLine.java` — 单元格自适应切分
- `layout/LineAssembler.java` — 行聚类
- `extractor/PdfTextLayoutExtractor.java`（文本层）、`OcrLayoutExtractor.java`（OCR）
- `config/OcrConfig.java` — 开关（如 setUseVectorRules）
- `model/` — 21 字段目标模型

## 测试资产
- `DeliveryOrderRobustnessCheck.java` — 正式回归工具（4 场景，文档化）
- `JitterFieldDiag.java` — 字段级失配定位（复用 `parseRealisticNoise`）
- 运行见每日日志的运行方式段落。

## 当前收敛阈值（2026-08-06 定稿）
MAX_LABEL_ROW_OVERLAP=0.35f；SIDE_BY_SIDE_TOLERANCE=1.0f；
MAX_SPLIT_DEPTH=3；DOMINANT_GAP_RATIO=2.2f；GAP_CLUSTER_RATIO=2.2f。

## 已知限制
- 本机无 Tesseract：OCR 通道未实跑，仅以拟真噪声模型模拟几何条件。
- cargo 区块在 ±1.00°/±1.2pt 噪声下偶有失败（description 渗值 / total_packages 丢值），其余字段稳健。
- 验证基线依赖样例 PDF；新样本需重新跑回归 + 字段诊断。
