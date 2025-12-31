-- 测试视频数据

-- 英语视频
INSERT INTO video_info (id, title, description, cover_url, video_url, duration, language, level, play_count, status, has_subtitle) VALUES
(1, 'TED Talk: The Power of Introverts', '苏珊·凯恩讲述内向者的力量', 'https://img.youtube.com/vi/c0KYU2j0TM4/maxresdefault.jpg', 'https://www.youtube.com/watch?v=c0KYU2j0TM4', 1140, 'en', 3, 15200, 1, 1),
(2, 'Friends S01E01 - The One Where Monica Gets a Roommate', '老友记第一季第一集', 'https://img.youtube.com/vi/example1/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4', 1320, 'en', 2, 28500, 1, 1),
(3, 'Daily English Conversation Practice', '日常英语对话练习', 'https://img.youtube.com/vi/example2/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4', 600, 'en', 1, 12300, 1, 1),
(4, 'Business English: Meeting Skills', '商务英语：会议技巧', 'https://img.youtube.com/vi/example3/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4', 900, 'en', 3, 8700, 1, 1),
(5, 'IELTS Listening Practice Test', '雅思听力练习测试', 'https://img.youtube.com/vi/example4/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4', 1800, 'en', 4, 5600, 1, 1);

-- 日语视频
INSERT INTO video_info (id, title, description, cover_url, video_url, duration, language, level, play_count, status, has_subtitle) VALUES
(6, '日本語の日常会話 - 初心者向け', '初学者日语日常会话', 'https://img.youtube.com/vi/example5/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4', 720, 'ja', 1, 9800, 1, 1),
(7, 'JLPT N3 リスニング練習', 'JLPT N3听力练习', 'https://img.youtube.com/vi/example6/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4', 1200, 'ja', 3, 6500, 1, 1),
(8, 'アニメで学ぶ日本語', '动漫学日语', 'https://img.youtube.com/vi/example7/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4', 480, 'ja', 2, 18200, 1, 1);

-- 韩语视频
INSERT INTO video_info (id, title, description, cover_url, video_url, duration, language, level, play_count, status, has_subtitle) VALUES
(9, '한국어 기초 회화', '韩语基础会话', 'https://img.youtube.com/vi/example8/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4', 600, 'ko', 1, 7400, 1, 1),
(10, 'K-Drama 학습: 사랑의 불시착', '韩剧学习：爱的迫降', 'https://img.youtube.com/vi/example9/maxresdefault.jpg', 'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4', 1500, 'ko', 2, 12100, 1, 1);

-- 字幕数据
INSERT INTO video_subtitle (id, video_id, language, format, content) VALUES
(1, 1, 'en', 'srt', '1\n00:00:01,000 --> 00:00:05,000\nWhen I was nine years old, I went off to summer camp for the first time.\n\n2\n00:00:05,500 --> 00:00:10,000\nAnd my mother packed me a suitcase full of books.\n\n3\n00:00:10,500 --> 00:00:15,000\nWhich to me seemed like a perfectly natural thing to do.'),
(2, 1, 'zh', 'srt', '1\n00:00:01,000 --> 00:00:05,000\n九岁那年，我第一次去参加夏令营。\n\n2\n00:00:05,500 --> 00:00:10,000\n我妈妈给我装了一箱书。\n\n3\n00:00:10,500 --> 00:00:15,000\n对我来说这是再自然不过的事情。'),
(3, 2, 'en', 'srt', '1\n00:00:01,000 --> 00:00:04,000\nThere is nothing to tell. He is just some guy I work with.\n\n2\n00:00:04,500 --> 00:00:08,000\nCome on, you are going out with the guy.\n\n3\n00:00:08,500 --> 00:00:12,000\nThere is got to be something wrong with him.');
