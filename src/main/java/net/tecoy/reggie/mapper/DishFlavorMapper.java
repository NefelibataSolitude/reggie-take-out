/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 18:10
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.tecoy.reggie.pojo.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
