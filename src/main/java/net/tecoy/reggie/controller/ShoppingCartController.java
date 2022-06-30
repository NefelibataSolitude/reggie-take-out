/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 17:58
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.tecoy.reggie.common.BaseContext;
import net.tecoy.reggie.common.R;
import net.tecoy.reggie.pojo.entity.ShoppingCart;
import net.tecoy.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> save(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        shoppingCart.setCreateTime(LocalDateTime.now());
        // 查询当前用户购物车是否存在该商品
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        // 判断是菜品还是套餐
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart oneShoppingCart = shoppingCartService.getOne(queryWrapper);
        if (oneShoppingCart != null) {
            Integer number = oneShoppingCart.getNumber();
            oneShoppingCart.setNumber(number + 1);
            return shoppingCartService.updateById(oneShoppingCart) ? R.success(oneShoppingCart) : R.error("添加到购物车失败!");
        }
        return shoppingCartService.save(shoppingCart) ? R.success(shoppingCart) : R.error("添加到购物车失败!");
    }

    /**
     * 减少商品数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        // 查询当前用户购物车是否存在该商品
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        // 判断是菜品还是套餐
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart oneShoppingCart = shoppingCartService.getOne(queryWrapper);
        if (oneShoppingCart == null) {
            return R.error("操作失败!");
        }
        if (oneShoppingCart.getNumber()-1 <= 0) {
            return shoppingCartService.remove(queryWrapper) ? R.success("操作成功!") : R.error("操作失败!!");
        }
        Integer number = oneShoppingCart.getNumber();
        oneShoppingCart.setNumber(number - 1);
        return shoppingCartService.updateById(oneShoppingCart) ? R.success("操作成功!") : R.error("操作失败!!");
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        return shoppingCartService.remove(queryWrapper) ? R.success("操作成功!") : R.error("操作失败!");
    }

}
