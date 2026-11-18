package com.cube001.cubeboot.access.controller.dto;

import com.baomidou.mybatisplus.plugins.Page;
import com.cube001.cubeboot.core.authorization.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author:张向宇
 * @Description:
 * @Date:Create in 13:57 2018/07/10
 * @Modified By:
 */
@Mapper(componentModel = "spring")
public interface AccessUserDtoConverter {
    public UserDto userToUserDto(User user);
    public List<UserDto> userListToDto(List<User> users);

    public UserListItemDto userToUserListItemDto(User user);
    public List<UserListItemDto> userListToItemDto(List<User> users);
    public Page<UserListItemDto> toPagedDto(Page<User> users);

    public UserEditDetailDto userToEditDto(User user);
    public User editDtoToUser(UserEditDetailDto detailDto);
}
