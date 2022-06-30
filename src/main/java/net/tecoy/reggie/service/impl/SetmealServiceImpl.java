/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 13:51
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.common.CustomException;
import net.tecoy.reggie.pojo.dto.SetmealDto;
import net.tecoy.reggie.pojo.entity.Setmeal;
import net.tecoy.reggie.mapper.SetmealMapper;
import net.tecoy.reggie.pojo.entity.SetmealDish;
import net.tecoy.reggie.service.SetmealDishService;
import net.tecoy.reggie.service.SetmealService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    private final SetmealDishService setmealDishService;

    public SetmealServiceImpl(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SetmealDto setmealDto) {
        // 保存套餐的基本信息, 操作setmeal, 执行insert操作
        super.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(setmealDishe -> {
            setmealDishe.setSetmealId(setmealDto.getId());
            return setmealDishe;
        }).collect(Collectors.toList());
        // 保存套餐和菜品的关联信息
        return setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> ids) {
        // 查询套餐状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = super.count(queryWrapper);
        if (count > 0) {
            // 不能删除抛出异常
            throw new CustomException("套餐正在售卖中, 不能删除!");
        }
        // 可以删除, 删除
        // 删除套餐
        super.removeByIds(ids);
        // 删除关联数据
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        return setmealDishService.remove(dishLambdaQueryWrapper);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SetmealDto setmealDto) {
        super.updateById(setmealDto);
        // 删除关联关联菜品信息setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        // 添加菜品关联信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map(setmealDish -> {
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());
        return setmealDishService.saveBatch(setmealDishes);
    }
}
