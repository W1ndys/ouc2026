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
            String updateSql = "UPDATE score.ScoreRecord SET major = ?, politics = ?, english = ?, math = ?, professional = ?, total = ?, updated_at = ? WHERE qq = ?";
            int rowsAffected = jdbcTemplate.update(updateSql,
                    record.getMajor(),
                    record.getPoliticsScore(),
                    record.getEnglishScore(),
                    record.getMathScore(),
                    record.getProfessionalScore(),
                    record.getTotalScore(),
                    new Date(),
                    record.getQqNumber());
            return rowsAffected > 0;
        } else {
            // 插入新记录
            String insertSql = "INSERT INTO score.ScoreRecord (qq, major, politics, english, math, professional, total, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                    record.getIsDeleted() != 0 ? 1 : 0);
            return rowsAffected > 0;
        }
    }

    // 获取排行榜
    public List<ScoreRecord> getRanking(String major) {
        String sql = "SELECT * FROM score.ScoreRecord WHERE major = ? ORDER BY total DESC ";
        return jdbcTemplate.query(sql, new ScoreRecordRowMapper(), major);
    }

    // 获取图表数据
    public List<ScoreRecord> getChartData(String major) {
        String sql = "SELECT * FROM score.ScoreRecord WHERE major = ? ORDER BY created_at ASC";
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
            "081200 计算机科学与技术 (00不区分)",
            "083500 软件工程 (00不区分)",
            "085400 电子信息 (01计算机技术)",
            "085400 电子信息 (02软件工程)"
        };
        
        int insertedCount = 0;
        Date now = new Date();
        
        for (int i = 0; i < count; i++) {
            // 生成随机QQ号（10位数字）
            String qq = "" + (1000000000 + (long)(Math.random() * 9000000000L));
            
            // 随机选择专业方向
            String major = majors[(int)(Math.random() * majors.length)];
            
            // 生成随机分数
            int politics = 60 + (int)(Math.random() * 40); // 60-100
            int english = 50 + (int)(Math.random() * 50); // 50-100
            int math = 40 + (int)(Math.random() * 60); // 40-100
            int professional = 80 + (int)(Math.random() * 20); // 80-100
            int total = politics + english + math + professional;
            
            // 随机设置is_deleted状态（80%概率为0，20%概率为1）
            int isDeleted = Math.random() < 0.8 ? 0 : 1;
            
            // 插入数据
            String insertSql = "INSERT INTO score.ScoreRecord (qq, major, politics, english, math, professional, total, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertSql,
                    qq, major, politics, english, math, professional, total, now, now, isDeleted);
            
            if (rowsAffected > 0) {
                insertedCount++;
            }
        }
        
        return insertedCount;
    }

    // 分页查询数据
    public List<ScoreRecord> getRecordsWithPagination(String major, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM score.ScoreRecord WHERE major = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new ScoreRecordRowMapper(), major, pageSize, offset);
    }

    // 获取总记录数
    public int getTotalRecords(String major) {
        String sql = "SELECT COUNT(*) FROM score.ScoreRecord WHERE major = ?";
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
            return record;
        }
    }
}