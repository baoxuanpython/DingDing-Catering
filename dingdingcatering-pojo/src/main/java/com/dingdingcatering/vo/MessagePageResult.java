package com.dingdingcatering.vo;

import com.dingdingcatering.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 消息通知分页结果
 * <p>
 * 一次往返同时返回列表、总数与未读数，
 * 避免前端「列表已加载但红点未清零」这类两次请求的竞态问题。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagePageResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 消息总数 */
    private Long total;

    /** 当前页消息列表 */
    private List<Message> records;

    /** 未读消息数 */
    private Long unreadCount;
}