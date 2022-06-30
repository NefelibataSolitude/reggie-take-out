/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 15:53
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import net.tecoy.reggie.pojo.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    /**
     * 更新默认地址
     * @param addressBook
     * @return
     */
    AddressBook update(AddressBook addressBook);
}
