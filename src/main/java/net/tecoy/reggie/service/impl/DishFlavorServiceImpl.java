/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 18:11
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.pojo.entity.DishFlavor;
import net.tecoy.reggie.mapper.DishFlavorMapper;
import net.tecoy.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
