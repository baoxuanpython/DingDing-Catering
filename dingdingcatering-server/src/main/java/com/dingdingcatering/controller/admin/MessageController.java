package com.dingdingcatering.controller.admin;

import com.dingdingcatering.result.Result;
import com.dingdingcatering.service.common.MessageService;
import com.dingdingcatering.vo.MessagePageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端消息通知控制器
 * <p>
 * 消息由用户端触发（支付成功、催单），在管理端以铃铛面板形式展示。
 */
@Slf4j
@RestController
@RequestMapping("/admin/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 分页查询消息列表，同时返回总数与未读数
     *
     * @param page     页码，默认 1
     * @param pageSize 每页条数，默认 20，上限 50
     * @return 消息分页结果
     */
    @GetMapping("/list")
    public Result<MessagePageResult> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "20") Integer pageSize) {
        log.info("查询消息列表: page={}, pageSize={}", page, pageSize);
        return Result.success(messageService.pageQuery(page, pageSize));
    }

    /**
     * 将全部未读消息标记为已读
     *
     * @return 操作结果
     */
    @PutMapping("/readAll")
    public Result<Void> readAll() {
        log.info("全部消息标记为已读");
        messageService.markAllRead();
        return Result.success();
    }
}