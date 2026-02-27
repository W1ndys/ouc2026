## 分析当前系统

当前系统使用Bmob云数据库进行数据存储和查询，主要包括以下功能：
1. 用户登录与数据获取
2. 成绩提交与更新
3. 排名查询
4. 图表数据展示

## 替换方案

由于前端无法直接连接MySQL数据库，需要通过后端API进行交互。我将设计以下方案：

### 1. 移除Bmob相关代码
- 移除Bmob SDK引入
- 移除Bmob初始化代码
- 移除所有Bmob.Query相关操作

### 2. 添加MySQL数据库操作函数
- 创建数据库连接配置
- 实现用户登录查询函数
- 实现成绩提交与更新函数
- 实现排名查询函数
- 实现图表数据获取函数

### 3. 修改现有功能
- 更新loginWithQQ函数，使用MySQL查询替代Bmob
- 更新submitCloudScore函数，使用MySQL保存替代Bmob
- 更新fetchRanking函数，使用MySQL查询替代Bmob
- 更新updateChart函数，使用MySQL查询替代Bmob

### 4. 数据结构适配
- 适配MySQL表结构（qq, major, politics, english, math, professional, total, created_at, updated_at, is_deleted）
- 确保前端数据与后端API的格式一致

### 5. 错误处理与降级方案
- 保留本地IndexedDB作为降级方案，当网络连接失败时使用
- 添加适当的错误处理和用户提示

## 技术实现

使用Fetch API与后端API进行交互，假设后端API提供以下端点：
- POST /api/login - 用户登录与数据获取
- POST /api/submit - 成绩提交与更新
- GET /api/ranking - 排名查询
- GET /api/chart - 图表数据获取

这样，系统将能够使用远程MySQL数据库替代Bmob云数据库，同时保持原有功能不变。