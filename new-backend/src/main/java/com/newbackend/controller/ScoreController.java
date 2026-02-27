package com.newbackend.controller;

import com.newbackend.entity.ScoreRecord;
import com.newbackend.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    // 登录验证
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request) {
        ScoreRecord record = scoreService.login(request.getQq());
        if (record != null) {
            return new Response(true, "登录成功", record);
        } else {
            return new Response(false, "未找到记录", null);
        }
    }

    // 提交分数
    @PostMapping("/submit")
    public Response submit(@RequestBody ScoreRecord record) {
        boolean success = scoreService.submitScore(record);
        if (success) {
            return new Response(true, "提交成功", null);
        } else {
            return new Response(false, "提交失败", null);
        }
    }

    // 获取排行榜
    @GetMapping("/ranking")
    public Response getRanking(@RequestParam String major) {
        List<ScoreRecord> ranking = scoreService.getRanking(major);
        return new Response(true, "获取排行榜成功", ranking);
    }

    // 获取图表数据
    @GetMapping("/chart")
    public Response getChartData(@RequestParam String major) {
        List<ScoreRecord> chartData = scoreService.getChartData(major);
        return new Response(true, "获取图表数据成功", chartData);
    }

    // 更新状态（is_deleted字段）
    @PostMapping("/update-status")
    public Response updateStatus(@RequestBody StatusUpdateRequest request) {
        boolean success = scoreService.updateStatus(request.getQq(), request.getIsDeleted());
        if (success) {
            return new Response(true, "状态更新成功", null);
        } else {
            return new Response(false, "状态更新失败", null);
        }
    }

    // 生成并填充合成数据
    @PostMapping("/generate-data")
    public Response generateData(@RequestBody GenerateDataRequest request) {
        int count = scoreService.generateAndInsertData(request.getCount());
        return new Response(true, "成功生成" + count + "条合成数据", null);
    }

    // 分页查询数据
    @GetMapping("/records")
    public Response getRecordsWithPagination(@RequestParam String major, @RequestParam int page, @RequestParam int pageSize) {
        List<ScoreRecord> records = scoreService.getRecordsWithPagination(major, page, pageSize);
        int total = scoreService.getTotalRecords(major);
        return new Response(true, "获取数据成功", new PaginationResponse(records, total, page, pageSize));
    }

    // 批量更新状态
    @PostMapping("/batch-update-status")
    public Response batchUpdateStatus(@RequestBody BatchUpdateStatusRequest request) {
        int count = scoreService.batchUpdateStatus(request.getQqs(), request.getIsDeleted());
        return new Response(true, "成功更新" + count + "条记录的状态", null);
    }

    // 批量更新状态请求类
    static class BatchUpdateStatusRequest {
        private List<String> qqs;
        private int isDeleted;

        public List<String> getQqs() {
            return qqs;
        }

        public void setQqs(List<String> qqs) {
            this.qqs = qqs;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
        }
    }

    // 分页响应类
    static class PaginationResponse {
        private List<ScoreRecord> data;
        private int total;
        private int page;
        private int pageSize;

        public PaginationResponse(List<ScoreRecord> data, int total, int page, int pageSize) {
            this.data = data;
            this.total = total;
            this.page = page;
            this.pageSize = pageSize;
        }

        public List<ScoreRecord> getData() {
            return data;
        }

        public void setData(List<ScoreRecord> data) {
            this.data = data;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

    // 生成数据请求类
    static class GenerateDataRequest {
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    // 状态更新请求类
    static class StatusUpdateRequest {
        private String qq;
        private int isDeleted;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public int getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(int isDeleted) {
            this.isDeleted = isDeleted;
        }
    }

    // 登录请求类
    static class LoginRequest {
        private String qq;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }
    }

    // 响应类
    static class Response {
        private boolean success;
        private String message;
        private Object data;

        public Response(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}