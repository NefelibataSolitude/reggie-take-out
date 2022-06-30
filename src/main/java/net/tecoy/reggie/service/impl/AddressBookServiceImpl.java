/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/29 15:54
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.tecoy.reggie.common.BaseContext;
import net.tecoy.reggie.mapper.AddressBookMapper;
import net.tecoy.reggie.pojo.entity.AddressBook;
import net.tecoy.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressBook update(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        super.update(wrapper);
        addressBook.setIsDefault(1);
        super.updateById(addressBook);
        return addressBook;
    }
}
