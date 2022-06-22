package com.czt.reggit.dto;


import com.czt.reggit.pojo.Setmeal;
import com.czt.reggit.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
