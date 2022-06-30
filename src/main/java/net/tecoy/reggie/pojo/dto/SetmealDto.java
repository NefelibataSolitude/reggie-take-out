package net.tecoy.reggie.pojo.dto;


import lombok.Data;
import net.tecoy.reggie.pojo.entity.Setmeal;
import net.tecoy.reggie.pojo.entity.SetmealDish;

import java.util.List;

/**
 * SetmealDto
 * @author Tecoy
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
