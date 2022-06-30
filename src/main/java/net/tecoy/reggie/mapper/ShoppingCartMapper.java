/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 17:56
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.tecoy.reggie.pojo.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
