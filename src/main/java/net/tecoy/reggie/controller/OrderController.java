/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 21:06
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.tecoy.reggie.common.BaseContext;
import net.tecoy.reggie.common.R;
import net.tecoy.reggie.pojo.entity.Orders;
import net.tecoy.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService ordersService;

    public OrderController(OrderService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orders.setUserId(BaseContext.getCurrentId());
        return ordersService.submit(orders) ? R.success("下单成功!") : R.error("下单失败!");
    }

    @GetMapping("/userPage")
    public R<Page<Orders>> page(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage, queryWrapper);

        return R.success(ordersPage);
    }
}
