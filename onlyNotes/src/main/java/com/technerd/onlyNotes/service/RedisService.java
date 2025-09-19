package com.technerd.onlyNotes.service;

import com.technerd.onlyNotes.entity.Notes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RedisService {


    private final RedisTemplate<String, Object> redisTemplate;

    // Time To Live Duration
    private static final Duration TTL = Duration.ofHours(1);

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    // Redis Key
    String keyFor(String userId){
        return "favNotes:"+ userId;
    }


    public Optional<List<Notes>> getFavouriteNotesFromRedis(String userId){
        try {
            Object fromRedis = redisTemplate.opsForValue().get(keyFor(userId));
            if (fromRedis != null){
              return Optional.of((List<Notes>) fromRedis);
            }
        } catch (Exception e){
            log.error("Error while Getting from Redis: ",e);
        }
        return Optional.empty(); // Never return null.
    }

    // Set in redis
    public void set(String userId, List<Notes> notes){
        try {
            redisTemplate.opsForValue().set(keyFor(userId),notes, TTL);
        } catch (Exception e){
            log.error("Error while setting in Redis: ", e);
        }
    }

}
