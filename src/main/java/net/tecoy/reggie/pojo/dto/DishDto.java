package net.tecoy.reggie.pojo.dto;

import lombok.Data;
import net.tecoy.reggie.pojo.entity.Dish;
import net.tecoy.reggie.pojo.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tecoy
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
