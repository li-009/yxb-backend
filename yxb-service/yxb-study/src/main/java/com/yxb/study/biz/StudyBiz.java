package com.yxb.study.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yxb.api.study.dto.StudyProgressDTO;
import com.yxb.api.study.dto.WordBookDTO;
import com.yxb.api.user.feign.UserFeignClient;
import com.yxb.api.video.feign.VideoFeignClient;
import com.yxb.common.core.util.UserContextHolder;
import com.yxb.study.convert.StudyConvert;
import com.yxb.study.domain.entity.StudyProgress;
import com.yxb.study.domain.entity.WordBook;
import com.yxb.study.service.StudyProgressService;
import com.yxb.study.service.StudyNoteService;
import com.yxb.study.service.UserCollectService;
import com.yxb.study.service.WordBookService;
import com.yxb.study.domain.entity.StudyNote;
import com.yxb.study.domain.entity.UserCollect;
import com.yxb.api.study.dto.StudyNoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudyBiz {

    private final StudyProgressService progressService;
    private final WordBookService wordBookService;
    private final StudyNoteService noteService;
    private final UserCollectService collectService;
    private final UserFeignClient userFeignClient;
    private final VideoFeignClient videoFeignClient;

    public void updateProgress(Long videoId, Integer position, Integer watchedDuration) {
        Long userId = UserContextHolder.requireUserId();
        StudyProgress progress = progressService.getByUserAndVideo(userId, videoId);
        
        if (progress == null) {
            progress = new StudyProgress();
            progress.setUserId(userId);
            progress.setVideoId(videoId);
            progress.setCurrentPosition(position);
            progress.setWatchedDuration(watchedDuration);
            progress.setCompleted(false);
            progress.setReadAlongCount(0);
            progress.setAvgReadAlongScore(0);
            progress.setLastStudyTime(LocalDateTime.now());
            progressService.save(progress);
        } else {
            progress.setCurrentPosition(position);
            progress.setWatchedDuration(Math.max(progress.getWatchedDuration(), watchedDuration));
            progress.setLastStudyTime(LocalDateTime.now());
            progressService.updateById(progress);
        }

        // 异步更新用户学习时长
        userFeignClient.addStudyTime(userId, watchedDuration / 60);
    }

    public IPage<StudyProgressDTO> pageProgress(int pageNum, int pageSize) {
        Long userId = UserContextHolder.requireUserId();
        IPage<StudyProgress> page = progressService.pageByUser(userId, pageNum, pageSize);
        return page.convert(StudyConvert.INSTANCE::toProgressDTO);
    }

    public void addWord(WordBook wordBook) {
        Long userId = UserContextHolder.requireUserId();
        WordBook existing = wordBookService.getByUserAndWord(userId, wordBook.getWord());
        if (existing != null) {
            return;
        }
        wordBook.setUserId(userId);
        wordBook.setMasteryStatus(0);
        wordBook.setReviewCount(0);
        wordBook.setNextReviewTime(LocalDateTime.now().plusDays(1));
        wordBookService.save(wordBook);
    }

    public IPage<WordBookDTO> pageWords(Integer masteryStatus, int pageNum, int pageSize) {
        Long userId = UserContextHolder.requireUserId();
        IPage<WordBook> page = wordBookService.pageByUser(userId, masteryStatus, pageNum, pageSize);
        return page.convert(StudyConvert.INSTANCE::toWordBookDTO);
    }

    public List<WordBookDTO> getReviewWords(int limit) {
        Long userId = UserContextHolder.requireUserId();
        List<WordBook> words = wordBookService.getNeedReview(userId, limit);
        return words.stream().map(StudyConvert.INSTANCE::toWordBookDTO).toList();
    }

    public void updateWordMastery(Long wordId, Integer masteryStatus) {
        WordBook word = wordBookService.getById(wordId);
        if (word == null) return;
        
        word.setMasteryStatus(masteryStatus);
        word.setReviewCount(word.getReviewCount() + 1);
        // 艾宾浩斯遗忘曲线：根据复习次数调整下次复习时间
        int days = switch (word.getReviewCount()) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 7;
            case 5 -> 15;
            default -> 30;
        };
        word.setNextReviewTime(LocalDateTime.now().plusDays(days));
        wordBookService.updateById(word);
    }

    public void addNote(StudyNote note) {
        Long userId = UserContextHolder.requireUserId();
        note.setUserId(userId);
        noteService.save(note);
    }

    public IPage<StudyNoteDTO> pageNotes(Long videoId, int pageNum, int pageSize) {
        Long userId = UserContextHolder.requireUserId();
        IPage<StudyNote> page = noteService.pageByUser(userId, videoId, pageNum, pageSize);
        return page.convert(this::toNoteDTO);
    }

    public void deleteNote(Long noteId) {
        StudyNote note = noteService.getById(noteId);
        if (note != null) {
            note.setDeleted(1);
            noteService.updateById(note);
        }
    }

    public int getNoteCount() {
        Long userId = UserContextHolder.requireUserId();
        return noteService.countByUser(userId);
    }

    private StudyNoteDTO toNoteDTO(StudyNote note) {
        StudyNoteDTO dto = new StudyNoteDTO();
        dto.setId(note.getId());
        dto.setUserId(note.getUserId());
        dto.setVideoId(note.getVideoId());
        dto.setTimestamp(note.getTimestamp());
        dto.setContent(note.getContent());
        dto.setSubtitleText(note.getSubtitleText());
        dto.setCategory(note.getCategory());
        dto.setTags(note.getTags());
        dto.setCreateTime(note.getCreateTime());
        dto.setUpdateTime(note.getUpdateTime());
        return dto;
    }

    public void collectVideo(Long videoId) {
        Long userId = UserContextHolder.requireUserId();
        UserCollect existing = collectService.getByUserAndVideo(userId, videoId);
        if (existing != null) {
            return;
        }
        UserCollect collect = new UserCollect();
        collect.setUserId(userId);
        collect.setVideoId(videoId);
        collect.setCollectType(1);
        collectService.save(collect);
    }

    public void uncollectVideo(Long videoId) {
        Long userId = UserContextHolder.requireUserId();
        UserCollect collect = collectService.getByUserAndVideo(userId, videoId);
        if (collect != null) {
            collect.setDeleted(1);
            collectService.updateById(collect);
        }
    }

    public boolean isVideoCollected(Long videoId) {
        Long userId = UserContextHolder.requireUserId();
        return collectService.isCollected(userId, videoId);
    }

    public IPage<Long> pageCollectedVideos(int pageNum, int pageSize) {
        Long userId = UserContextHolder.requireUserId();
        IPage<UserCollect> page = collectService.pageByUser(userId, pageNum, pageSize);
        return page.convert(UserCollect::getVideoId);
    }

    public int getCollectCount() {
        Long userId = UserContextHolder.requireUserId();
        return collectService.countByUser(userId);
    }
}
