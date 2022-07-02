package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeListRes;
import com.zeroway.challenge.dto.GetChallengeRes;
import com.zeroway.challenge.dto.PatchChallengeCompleteRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChallengeRepository {

    private JdbcTemplate jdbcTemplate;
    private List<GetChallengeListRes> getChallengeListRes;


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetChallengeRes getList(Long userId) {
        String getChallengeQuery = "select user_id, level, (challenge_count / (select count(c.challenge_id) from challenge as c " +
                "where c.level= (select user.level from user where user_id=?)))*100 as exp from user where user_id=?;";

        Object[] getChallengeParam = new Object[]{userId, userId};
        return this.jdbcTemplate.queryForObject(getChallengeQuery,
                (rs, rowNum) -> new GetChallengeRes(
                        rs.getLong("user_id"),
                        rs.getInt("level"),
                        rs.getDouble("exp"),
                        getChallengeListRes = this.jdbcTemplate.query("select c.challenge_id, uc.complete, c.content\n" +
                                        "    from user_challenge as uc\n" +
                                        "    join challenge as c on c.challenge_id = uc.challenge_id\n" +
                                        "    where c.level=(select u.level from user as u where user_id=?)\n" +
                                        "and uc.user_id=?;",
                                (rk, rownum) -> new GetChallengeListRes(
                                        rk.getLong("challenge_id"),
                                        rk.getBoolean("complete"),
                                        rk.getString("content")
                                ),getChallengeParam
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

    public PatchChallengeCompleteRes findUserExp(Long userId) {
        String patchChallengeCompleteQuery = "select level, (challenge_count / (select count(c.challenge_id) from challenge as c " +
                "where c.level= (select user.level from user where user_id=?)))*100 as exp from user where user_id=?;";

        Object[] patchChallengeCompleteParam = new Object[]{userId, userId};
        return this.jdbcTemplate.queryForObject(patchChallengeCompleteQuery,
                (rs,rowNum) -> new PatchChallengeCompleteRes(
                        rs.getInt("level"),
                        rs.getDouble("exp")
                ),patchChallengeCompleteParam);
    }

    public void insertUserChallenge(long challengeId, long userId){
        String insertUserChallengeQuery = "INSERT INTO user_challenge(challenge_id, user_id) VALUES (?,?)";

        Object []insertUserChallengeParams= new Object[] {challengeId, userId};
        this.jdbcTemplate.update(insertUserChallengeQuery, insertUserChallengeParams);
    }

    public void resetUserChallengeCount(Long userId) {
        String completeChallengeQuery = "UPDATE user SET challenge_count=0 where user_id=?;";
        Object[] completeChallengeParams = new Object[]{userId};
        this.jdbcTemplate.update(completeChallengeQuery, completeChallengeParams);
    }

    public List<Long> findUserChallengeId(Long userId) {
        String patchChallengeCompleteQuery = "select challenge_id from challenge where level=(select level from user where user_id=?);";

        Long patchChallengeCompleteParam = userId;
        return this.jdbcTemplate.queryForList(patchChallengeCompleteQuery, Long.class, patchChallengeCompleteParam);
    }
}

//  return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
