package com.example.first.framework.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.framework.auth.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
