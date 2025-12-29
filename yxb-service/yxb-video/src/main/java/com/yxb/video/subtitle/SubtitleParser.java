package com.yxb.video.subtitle;

import com.yxb.api.video.dto.SubtitleDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字幕解析器 - 模板方法模式
 */
@Component
public class SubtitleParser {

    private static final Pattern SRT_PATTERN = Pattern.compile(
            "(\\d+)\\s*\\n(\\d{2}:\\d{2}:\\d{2},\\d{3})\\s*-->\\s*(\\d{2}:\\d{2}:\\d{2},\\d{3})\\s*\\n(.+?)(?=\\n\\n|\\n\\d+\\n|$)",
            Pattern.DOTALL
    );

    public List<SubtitleDTO.SubtitleItem> parse(String content, String format) {
        return switch (format.toLowerCase()) {
            case "srt" -> parseSRT(content);
            case "vtt" -> parseVTT(content);
            default -> parseSRT(content);
        };
    }

    private List<SubtitleDTO.SubtitleItem> parseSRT(String content) {
        List<SubtitleDTO.SubtitleItem> items = new ArrayList<>();
        Matcher matcher = SRT_PATTERN.matcher(content);
        
        while (matcher.find()) {
            SubtitleDTO.SubtitleItem item = new SubtitleDTO.SubtitleItem();
            item.setIndex(Integer.parseInt(matcher.group(1)));
            item.setStartTime(parseTime(matcher.group(2)));
            item.setEndTime(parseTime(matcher.group(3)));
            item.setText(matcher.group(4).trim());
            items.add(item);
        }
        return items;
    }

    private List<SubtitleDTO.SubtitleItem> parseVTT(String content) {
        // VTT格式与SRT类似，但时间格式略有不同
        return parseSRT(content.replace(".", ","));
    }

    private Long parseTime(String timeStr) {
        // 格式: 00:00:00,000
        String[] parts = timeStr.replace(",", ":").split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        long millis = Long.parseLong(parts[3]);
        return hours * 3600000 + minutes * 60000 + seconds * 1000 + millis;
    }
}
