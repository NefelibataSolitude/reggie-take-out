/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 13:53
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.tecoy.reggie.pojo.dto.DishDto;
import net.tecoy.reggie.pojo.entity.Dish;

import java.io.Serializable;

public interface DishService extends IService<Dish> {
    /**
     * 保存菜品+口味
     * @param dishDto
     * @return
     */
    boolean save(DishDto dishDto);

    /**
     * 根据id查询菜品信息和口味数据
     * @param id
     * @return
     */
    DishDto getById(Long id);

    /**
     * 更新菜品信息和口味
     * @param dishDto
     * @return
     */
    boolean update(DishDto dishDto);
}
