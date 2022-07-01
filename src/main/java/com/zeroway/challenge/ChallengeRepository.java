package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChallengeRepository  {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetChallengeRes> getList(Long userId) {
        String getChallengeQuery = "select c.challenge_id, uc.complete, c.content from challenge as c join user_challenge uc on c.challenge_id = uc.challenge_id\n" +
                "                              where c.level=(select u.level\n" +
                "                                             from user as u\n" +
                "                                             where user_id=?);";

        Long getChallengeParam = userId;
        return this.jdbcTemplate.query(getChallengeQuery,
                (rs,rowNum) -> new GetChallengeRes(
                        rs.getLong("challenge_id"),
                        rs.getBoolean("complete"),
                        rs.getString("content")
                ),getChallengeParam);
    }

    public int completeChallenge(Long userId, Long challengeId) {
        String completeChallengeQuery = "UPDATE user_challenge SET complete=true WHERE user_id=? and challenge_id=?;";
        Object []completeChallengeParams= new Object[] {userId, challengeId};
        return this.jdbcTemplate.update(completeChallengeQuery, completeChallengeParams);
    }
}
