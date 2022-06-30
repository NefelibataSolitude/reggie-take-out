/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 22:37
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.tecoy.reggie.common.R;
import net.tecoy.reggie.pojo.dto.DishDto;
import net.tecoy.reggie.pojo.dto.SetmealDto;
import net.tecoy.reggie.pojo.entity.Category;
import net.tecoy.reggie.pojo.entity.Dish;
import net.tecoy.reggie.pojo.entity.Setmeal;
import net.tecoy.reggie.pojo.entity.SetmealDish;
import net.tecoy.reggie.service.CategoryService;
import net.tecoy.reggie.service.DishService;
import net.tecoy.reggie.service.SetmealDishService;
import net.tecoy.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    private final SetmealService setmealService;
    private final SetmealDishService setmealDishService;
    private final CategoryService categoryService;
    private final DishService dishService;

    public SetmealController(SetmealService setmealService,
                             SetmealDishService setmealDishService,
                             CategoryService categoryService,
                             DishService dishService) {
        this.setmealService = setmealService;
        this.setmealDishService = setmealDishService;
        this.categoryService = categoryService;
        this.dishService = dishService;
    }

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        return setmealService.save(setmealDto) ? R.success("操作成功!") : R.error("操作失败!");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);

        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<SetmealDto> dtoPageRecords = setmealPage.getRecords().stream().map(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(dtoPageRecords);
        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        return setmealService.removeByIds(ids) ? R.success("操作成功!") : R.error("操作失败!");
    }

    /**
     * id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> query(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        // 分类名
        Category category = categoryService.getById(setmeal.getCategoryId());
        if (category != null) {
            setmealDto.setCategoryName(category.getName());
        }
        // 菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return R.success(setmealDto);
    }

    /**
     * 修改菜品
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        System.out.println(setmealDto.getId());
        return setmealService.update(setmealDto) ? R.success("操作i成功") : R.error("操作失败");
    }

    /**
     * 起售/停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status, @RequestParam List<Long> ids) {
        List<Setmeal> setmealList = new ArrayList<>();
        setmealList = ids.stream().map(id -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());

        return setmealService.updateBatchById(setmealList) ? R.success("操纵做成功!") : R.error("操作失败!");
    }

    /**
     * 查询套餐信息
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        List<SetmealDto> setmealDtoList =  setmealList.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            // 查询套餐菜品数据
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
            setmealDto.setSetmealDishes(setmealDishes);
            // 查询CategoryName
            Category category = categoryService.getById(setmeal.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(setmealDtoList);
    }

    /**
     * 查询套餐的菜品信息
     * @param setmealId
     * @return
     */
    @GetMapping("/dish/{setmealId}")
    public R<List<DishDto>> dish(@PathVariable Long setmealId) {
        // 查询套餐菜品数据
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealId);
        List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
        List<DishDto> dishDtoList = setmealDishes.stream().map(setmealDish -> {
            DishDto dishDto = dishService.getById(setmealDish.getDishId());
            dishDto.setCopies(setmealDish.getCopies());
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }


}
