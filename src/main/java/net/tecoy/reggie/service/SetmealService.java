/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 13:50
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import net.tecoy.reggie.pojo.dto.SetmealDto;
import net.tecoy.reggie.pojo.entity.Setmeal;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 添加菜品信息
     * @param setmealDto
     * @return
     */
    boolean save(SetmealDto setmealDto);

    /**
     * 删除套餐及关联
     * @param ids
     * @return
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 修改菜品
     * @param setmealDto
     * @return
     */
    boolean update(SetmealDto setmealDto);
}
