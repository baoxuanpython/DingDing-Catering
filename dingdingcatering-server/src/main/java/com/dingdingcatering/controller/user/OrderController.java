package com.dingdingcatering.controller.user;
import com.dingdingcatering.service.user.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/reminder/{id}")
    public void sendReminder(@PathVariable Long id) {
        orderService.sendReminder(id);
    }
}
