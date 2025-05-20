package com.example.demo.student.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.model.VoteResult;
import com.example.demo.student.repository.VoteRepository;
import com.example.demo.student.service.VoteService;

// This makes it return JSON directly (no need for @ResponseBody per method)
@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {

    private final VoteService voteService;
    private final VoteRepository voteRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VoteController(VoteService voteService, VoteRepository voteRepository, JdbcTemplate jdbcTemplate) {
        this.voteService = voteService;
        this.voteRepository = voteRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Endpoint to get vote results sorted descending
    @GetMapping("/results")
    public List<VoteResult> getResults() {
        String sql = "SELECT candidate_name, total_votes FROM votes ORDER BY total_votes DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new VoteResult(
                rs.getString("candidate_name"),
                rs.getInt("total_votes")
            )
        );
    }
}
