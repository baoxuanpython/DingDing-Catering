package com.dingdingcatering.mapper.common;

import com.dingdingcatering.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息通知 Mapper
 * <p>
 * 写入方为用户端业务（支付、催单），读取方为管理端消息中心，
 * 两端共用，故放在 common 包下。
 */
@Mapper
public interface MessageMapper {

    /**
     * 插入一条消息
     *
     * @param message 消息对象
     */
    void insert(Message message);

    /**
     * 分页查询消息列表
     *
     * @param offset   偏移量
     * @param pageSize 每页条数
     * @return 消息列表
     */
    List<Message> pageQuery(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 统计消息总数
     *
     * @return 总数
     */
    Long countAll();

    /**
     * 统计未读消息数
     *
     * @return 未读数
     */
    Long countUnread();

    /**
     * 统计指定类型、指定订单号的消息条数，用于写入前去重
     *
     * @param type        消息类型
     * @param orderNumber 订单号
     * @return 条数
     */
    Long countByTypeAndOrderNumber(@Param("type") Integer type, @Param("orderNumber") String orderNumber);

    /**
     * 将全部未读消息标记为已读
     */
    void markAllRead();

    /**
     * 删除指定时间之前的消息
     *
     * @param time 时间界限
     * @return 删除条数
     */
    int deleteBefore(@Param("time") LocalDateTime time);
}