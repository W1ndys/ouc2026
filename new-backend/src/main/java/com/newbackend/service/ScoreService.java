package com.newbackend.service;

import com.newbackend.entity.ScoreRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class ScoreService {
    private final JdbcTemplate jdbcTemplate;

    public ScoreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 登录验证
    public ScoreRecord login(String qq) {
        String sql = "SELECT * FROM score.ScoreRecord WHERE qq = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ScoreRecordRowMapper(), qq);
        } catch (Exception e) {
            return null;
        }
    }

    // 提交或更新分数
    public boolean submitScore(ScoreRecord record) {
        // 检查是否已存在记录
        ScoreRecord existingRecord = login(record.getQqNumber());
        if (existingRecord != null) {
            // 更新现有记录
            String updateSql = "UPDATE score.ScoreRecord SET major = ?, politics = ?, english = ?, math = ?, professional = ?, total = ?, exam_number = ?, picture = ?, updated_at = ? WHERE qq = ?";
            int rowsAffected = jdbcTemplate.update(updateSql,
                    record.getMajor(),
                    record.getPoliticsScore(),
                    record.getEnglishScore(),
                    record.getMathScore(),
                    record.getProfessionalScore(),
                    record.getTotalScore(),
                    record.getExamNumber(),
                    record.getPicture(),
                    new Date(),
                    record.getQqNumber());
            return rowsAffected > 0;
        } else {
            // 插入新记录
            String insertSql = "INSERT INTO score.ScoreRecord (qq, major, politics, english, math, professional, total, created_at, updated_at, is_deleted, exam_number, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertSql,
                    record.getQqNumber(),
                    record.getMajor(),
                    record.getPoliticsScore(),
                    record.getEnglishScore(),
                    record.getMathScore(),
                    record.getProfessionalScore(),
                    record.getTotalScore(),
                    new Date(),
                    new Date(),
                    record.getIsDeleted() != 0 ? 1 : 0,
                    record.getExamNumber(),
                    record.getPicture());
            return rowsAffected > 0;
        }
    }

    // 获取排行榜
    public List<ScoreRecord> getRanking(String major) {
        String sql = "SELECT * FROM score.ScoreRecord WHERE major = ? AND is_deleted = 0 ORDER BY total DESC ";
        return jdbcTemplate.query(sql, new ScoreRecordRowMapper(), major);
    }

    // 获取图表数据
    public List<ScoreRecord> getChartData(String major) {
        String sql = "SELECT * FROM score.ScoreRecord WHERE major = ? AND is_deleted = 0 ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, new ScoreRecordRowMapper(), major);
    }

    // 更新状态（is_deleted字段）
    public boolean updateStatus(String qq, int isDeleted) {
        String sql = "UPDATE score.ScoreRecord SET is_deleted = ?, updated_at = ? WHERE qq = ?";
        int rowsAffected = jdbcTemplate.update(sql, isDeleted, new Date(), qq);
        return rowsAffected > 0;
    }

    // 生成并插入合成数据
    public int generateAndInsertData(int count) {
        String[] majors = {
            "[信息] 081200 计算机科学与技术 (00不区分)",
            "[信息] 0812Z1 网络空间安全与保密 (00不区分)",
            "[信息] 085404 计算机技术 (01计算机技术)",
            "[信息] 085404 计算机技术 (02三亚)",
            "[信息] 085404 计算机技术 (03万里学院联培)",
            "[信息] 085404 计算机技术 (04中法双硕士)",
            "[信息] 085404 计算机技术 (05中新双硕士)",
            "[信息] 085404 计算机技术 (00不区分)",
            "[信息] 085405 软件工程 (00不区分)",
            "[信息] 085410 人工智能 (01人工智能)",
            "[信息] 085410 人工智能 (02三亚)",
            "[信息] 085410 人工智能 (00不区分)",
            "[卓越] 085404 计算机技术 (00不区分)",
            "[卓越] 085405 软件工程 (00不区分)",
            "[卓越] 085410 人工智能 (00不区分)",
            "[卓越] 085411 大数据技术与工程 (00不区分)",
            "[卓越] 085412 网络与信息安全 (00不区分)",
        };
        
        int insertedCount = 0;
        Date now = new Date();
        
        for (int i = 0; i < count; i++) {
            // 生成随机QQ号（10位数字）
            String qq = "" + (1000000000 + (long)(Math.random() * 9000000000L));
            
            // 随机生成考试编号（15位数字）
            String examNumber = "" + (100000000000000L + (long)(Math.random() * 900000000000000L));
            
            // 随机选择专业方向
            String major = majors[(int)(Math.random() * majors.length)];
            
            // 生成随机分数
            int politics = 60 + (int)(Math.random() * 40); // 60-100
            int english = 50 + (int)(Math.random() * 50); // 50-100
            int math = 40 + (int)(Math.random() * 60); // 40-100
            int professional = 80 + (int)(Math.random() * 20); // 80-100
            int total = politics + english + math + professional;
            
            // 随机设置is_deleted状态（80%概率为0，20%概率为1）
            int isDeleted = 1;
            //图片路径
            String picture = "/upload/9562ead4-4147-47bb-b0ea-e564f2ecae9b.jpg";
            // 插入数据
            String insertSql = "INSERT INTO score.ScoreRecord (qq, major, politics, english, math, professional, total, created_at, updated_at, is_deleted, exam_number, picture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertSql,
                    qq, major, politics, english, math, professional, total, now, now, isDeleted, examNumber, picture);
            
            if (rowsAffected > 0) {
                insertedCount++;
            }
        }
        
        return insertedCount;
    }

    // 分页查询数据
    public List<ScoreRecord> getRecordsWithPagination(String major, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        // 按照状态（is_deleted）和修改时间（updated_at）排序
        // is_deleted为1的记录排在前面（未启用的需要审核），然后按照修改时间降序排列
        String sql = "SELECT * FROM score.ScoreRecord WHERE major LIKE CONCAT('%', ?, '%') AND is_deleted = 1 ORDER BY updated_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new ScoreRecordRowMapper(), major, pageSize, offset);
    }

    // 获取总记录数
    public int getTotalRecords(String major) {
        String sql = "SELECT COUNT(*) FROM score.ScoreRecord WHERE major LIKE CONCAT('%', ?, '%')";
        return jdbcTemplate.queryForObject(sql, Integer.class, major);
    }

    // 批量更新状态
    public int batchUpdateStatus(List<String> qqs, int isDeleted) {
        int updatedCount = 0;
        Date now = new Date();
        
        for (String qq : qqs) {
            String updateSql = "UPDATE score.ScoreRecord SET is_deleted = ?, updated_at = ? WHERE qq = ?";
            int rowsAffected = jdbcTemplate.update(updateSql, isDeleted, now, qq);
            
            if (rowsAffected > 0) {
                updatedCount++;
            }
        }
        
        return updatedCount;
    }

    // 内部类：RowMapper实现
    private class ScoreRecordRowMapper implements RowMapper<ScoreRecord> {
        @Override
        public ScoreRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoreRecord record = new ScoreRecord();
            record.setQqNumber(rs.getString("qq"));
            record.setExamDirection(rs.getString("major"));
            record.setPoliticsScore(rs.getInt("politics"));
            record.setEnglishScore(rs.getInt("english"));
            record.setMathScore(rs.getInt("math"));
            record.setProfessionalScore(rs.getInt("professional"));
            record.setTotalScore(rs.getInt("total"));
            record.setCreateTime(rs.getTimestamp("created_at"));
            record.setUpdateTime(rs.getTimestamp("updated_at"));
            record.setIsDeleted(rs.getInt("is_deleted"));
            record.setExamNumber(rs.getString("exam_number"));
            record.setPicture(rs.getString("picture"));
            return record;
        }
    }
}