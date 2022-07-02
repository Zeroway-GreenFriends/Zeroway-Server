package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeListRes;
import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChallengeRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetChallengeListRes> getChallengeListRes;


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetChallengeRes> getList(Long userId) {
        String getChallengeQuery = "select user_id, level, (challenge_count / (select count(c.challenge_id) from challenge as c " +
                "where c.level= (select user.level from user where user_id=?)))*100 as exp from user where user_id=?;";

        Object[] getChallengeParam = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getChallengeQuery,
                (rs, rowNum) -> new GetChallengeRes(
                        rs.getLong("user_id"),
                        rs.getInt("level"),
                        rs.getDouble("exp"),
                        getChallengeListRes = this.jdbcTemplate.query("select c.challenge_id, uc.complete, c.content from challenge as c " +
                                        "join user_challenge uc on c.challenge_id = uc.challenge_id\n" +
                                        "    where c.level=(select u.level from user as u where user_id=?);",
                                (rk, rownum) -> new GetChallengeListRes(
                                        rk.getLong("challenge_id"),
                                        rk.getBoolean("complete"),
                                        rk.getString("content")
                                ),rs.getLong("user_id")
                        )
                ),getChallengeParam);
    }

    public int updateChallengeCount(Long userId, Long challengeId) {
        String completeChallengeQuery = "UPDATE user_challenge SET complete=true WHERE user_id=? and challenge_id=?;";
        Object[] completeChallengeParams = new Object[]{userId, challengeId};
        return this.jdbcTemplate.update(completeChallengeQuery, completeChallengeParams);
    }

    public int checkLevelUpgrade(Long userId) {
        String findChallengeCountQuery = "select challenge_count from user where user_id=?";

        Long findChallengeCountParams = userId;
        return this.jdbcTemplate.queryForObject(findChallengeCountQuery,
                int.class,
                findChallengeCountParams);
    }

    public int findUserLevel(Long userId) {
        String findChallengeCountQuery = "select level from user where user_id=?";

        Long findChallengeCountParams = userId;
        return this.jdbcTemplate.queryForObject(findChallengeCountQuery,
                int.class,
                findChallengeCountParams);
    }

    public void addUserLevel(Long userId) {
        String completeChallengeQuery = "UPDATE user SET level=level+1 where user_id=?;";
        Object[] completeChallengeParams = new Object[]{userId};
        this.jdbcTemplate.update(completeChallengeQuery, completeChallengeParams);
    }

    public void addChallengeCount(Long userId) {
        String completeChallengeQuery = "UPDATE user SET challenge_count=challenge_count+1 where user_id=?;";
        Object[] completeChallengeParams = new Object[]{userId};
        this.jdbcTemplate.update(completeChallengeQuery, completeChallengeParams);
    }

    public List<PatchChallengeCompleteRes> findUserExp(Long userId) {
        String patchChallengeCompleteQuery = "select level, (challenge_count / (select count(c.challenge_id) from challenge as c " +
                "where c.level= (select user.level from user where user_id=?)))*100 as exp from user where user_id=?;";

        Object[] patchChallengeCompleteParam = new Object[]{userId, userId};
        return this.jdbcTemplate.query(patchChallengeCompleteQuery,
                (rs,rowNum) -> new PatchChallengeCompleteRes(
                        rs.getInt("level"),
                        rs.getDouble("exp")
                ),patchChallengeCompleteParam);

    }
}
