package ru.tokmakov.mapper;

import ru.tokmakov.dto.HitDto;
import ru.tokmakov.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private HitMapper() {
    }

    public static Hit hitDtoToHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return hit;
    }

    public static HitDto hitToHitDto(Hit hit) {
        HitDto hitDto = new HitDto();
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getTimestamp().format(formatter));
        return hitDto;
    }
}