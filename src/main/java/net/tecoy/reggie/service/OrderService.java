/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 21:01
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.tecoy.reggie.pojo.entity.Orders;

public interface OrderService extends IService<Orders> {
    /**
     * 下单
     * @param orders
     * @return
     */
    boolean submit(Orders orders);
}
