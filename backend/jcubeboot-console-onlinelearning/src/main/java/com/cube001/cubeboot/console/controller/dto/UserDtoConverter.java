package com.cube001.cubeboot.console.controller.dto;


import com.cube001.cubeboot.core.authorization.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 15:59 2018/03/20
 * @Modified By:
 */
@Mapper(componentModel = "spring")
public interface UserDtoConverter {
    public UserDto userToUserDto(User user);
    public List<UserDto> userListToDto(List<User> users);

    UserPersonInfoDto userToPersonDto(User user);

}
