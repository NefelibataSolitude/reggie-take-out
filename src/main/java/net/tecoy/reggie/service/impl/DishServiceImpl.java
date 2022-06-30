/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 13:54
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.pojo.dto.DishDto;
import net.tecoy.reggie.pojo.entity.Dish;
import net.tecoy.reggie.mapper.DishMapper;
import net.tecoy.reggie.pojo.entity.DishFlavor;
import net.tecoy.reggie.service.DishFlavorService;
import net.tecoy.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

     private final DishFlavorService dishFlavorService;

    public DishServiceImpl(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(DishDto dishDto) {
        // 保存菜品的基本信息
        super.save(dishDto);
        // 菜品id
        Long dishId = dishDto.getId();
        // 口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return false;
    }

    @Override
    public DishDto getById(Long id) {
        Dish dish = super.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(DishDto dishDto) {
        // 更新dish表信息
        super.updateById(dishDto);
        // 先清理菜品口味对应数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 添加口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        return dishFlavorService.saveBatch(flavors);
    }
}
