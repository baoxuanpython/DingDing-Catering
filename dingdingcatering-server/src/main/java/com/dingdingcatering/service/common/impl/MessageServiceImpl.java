package com.dingdingcatering.service.common.impl;

import com.dingdingcatering.entity.Message;
import com.dingdingcatering.mapper.common.MessageMapper;
import com.dingdingcatering.service.common.MessageService;
import com.dingdingcatering.vo.MessagePageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息通知服务实现
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    /** 默认每页条数 */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /** 每页条数上限，防止一次拉取过多拖垮数据库 */
    private static final int MAX_PAGE_SIZE = 50;

    /** 消息保留天数 */
    private static final int KEEP_DAYS = 30;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public void save(Integer type, Long orderId, String orderNumber, String content) {
        messageMapper.insert(Message.builder()
                .type(type)
                .orderId(orderId)
                .orderNumber(orderNumber)
                .content(content)
                .isRead(Message.UNREAD)
                .createTime(LocalDateTime.now())
                .build());
        log.info("消息已保存: type={}, orderId={}, orderNumber={}", type, orderId, orderNumber);
    }

    @Override
    public boolean saveIfAbsent(Integer type, Long orderId, String orderNumber, String content) {
        Long exist = messageMapper.countByTypeAndOrderNumber(type, orderNumber);
        if (exist != null && exist > 0) {
            log.info("消息已存在，跳过重复写入: type={}, orderNumber={}", type, orderNumber);
            return false;
        }
        save(type, orderId, orderNumber, content);
        return true;
    }

    @Override
    public MessagePageResult pageQuery(Integer page, Integer pageSize) {
        int currentPage = (page == null || page < 1) ? 1 : page;
        int size = (pageSize == null || pageSize < 1 || pageSize > MAX_PAGE_SIZE) ? DEFAULT_PAGE_SIZE : pageSize;

        List<Message> records = messageMapper.pageQuery((currentPage - 1) * size, size);
        Long total = messageMapper.countAll();
        Long unreadCount = messageMapper.countUnread();

        return MessagePageResult.builder()
                .total(total == null ? 0L : total)
                .records(records)
                .unreadCount(unreadCount == null ? 0L : unreadCount)
                .build();
    }

    @Override
    public void markAllRead() {
        messageMapper.markAllRead();
        log.info("全部消息已标记为已读");
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpired() {
        int deleted = messageMapper.deleteBefore(LocalDateTime.now().minusDays(KEEP_DAYS));
        log.info("清理过期消息 {} 条（保留最近 {} 天）", deleted, KEEP_DAYS);
    }
}