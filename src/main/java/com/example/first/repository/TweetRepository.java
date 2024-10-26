package com.example.first.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first.entity.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
