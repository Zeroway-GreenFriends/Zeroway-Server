package com.zeroway.challenge;

import com.zeroway.challenge.dto.GetChallengeRes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChallengeRepository  {

    private JdbcTemplate jdbcTemplate;


    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetChallengeRes> getList(int userId) {
        String getChallengeQuery = "SELECT comlete, content FROM Challenge WHERE user_id=?";

        int getChallengeParam = userId;
        return this.jdbcTemplate.query(getChallengeQuery,
                (rs,rowNum) -> new GetChallengeRes(
                        rs.getBoolean("complete"),
                        rs.getString("content")
                ),getChallengeParam);
    }
}
