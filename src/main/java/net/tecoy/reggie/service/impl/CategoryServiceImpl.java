/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 12:43
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.pojo.entity.Category;
import net.tecoy.reggie.pojo.entity.Dish;
import net.tecoy.reggie.pojo.entity.Setmeal;
import net.tecoy.reggie.common.CustomException;
import net.tecoy.reggie.mapper.CategoryMapper;
import net.tecoy.reggie.service.CategoryService;
import net.tecoy.reggie.service.DishService;
import net.tecoy.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    private final DishService dishService;
    private final SetmealService setmealService;

    public CategoryServiceImpl(DishService dishService, SetmealService setmealService) {
        this.dishService = dishService;
        this.setmealService = setmealService;
    }

    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        // 删除前判断是否关联菜品, 如果关联抛出业务异常
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishQueryWrapper);
        if (dishCount > 0) {
            // 已经关联, 抛出异常
            throw new CustomException("当前分类下关联了菜品, 不能删除!");
            // 或者返回false在controller中处理
        }
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        // 删除前判断是否关联套餐, 如果关联抛出业务异常
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(setmealQueryWrapper);
        if (setmealCount > 0) {
            throw new CustomException("当前分类下关联了套餐, 不能删除!");
        }
        return super.removeById(id);
    }
}
