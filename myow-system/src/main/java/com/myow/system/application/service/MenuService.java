package com.myow.system.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myow.common.response.PageResult;
import com.myow.common.util.MyPageUtil;
import com.myow.system.application.converter.MenuApplicationConverter;
import com.myow.system.application.dto.CreateMenuReqDTO;
import com.myow.system.application.dto.MenuRespDTO;
import com.myow.system.application.dto.PageMenuReqDTO;
import com.myow.system.application.dto.UpdateMenuReqDTO;
import com.myow.system.domain.entity.Menu;
import com.myow.system.infrastructure.converter.MenuConverter;
import com.myow.system.infrastructure.persistence.po.MenuDO;
import com.myow.system.infrastructure.persistence.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yss
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuApplicationConverter menuApplicationConverter;
    private final MenuConverter menuConverter;

    public Long createMenu(CreateMenuReqDTO createReqDTO) {
        Menu menu = menuApplicationConverter.convert(createReqDTO);
        menuRepository.save(menuConverter.toDo(menu));
        return menu.getMenuId();
    }

    public void updateMenu(UpdateMenuReqDTO updateReqDTO) {
        Menu menu = menuApplicationConverter.convert(updateReqDTO);
        menuRepository.updateById(menuConverter.toDo(menu));
    }

    public void deleteMenu(Long id) {
        menuRepository.removeById(id);
    }

    public MenuRespDTO getMenu(Long id) {
        MenuDO menuDO = menuRepository.getById(id);
        return menuApplicationConverter.convert(menuConverter.toEntity(menuDO));
    }

    public PageResult<MenuRespDTO> getMenuPage(PageMenuReqDTO pageMenuReqDTO) {
        Page<MenuDO> menuDOPage = menuRepository.selectPage(pageMenuReqDTO);
        if (Objects.isNull(menuDOPage) || menuDOPage.getRecords().isEmpty()) {
            return PageResult.empty();
        }
        return MyPageUtil.of(menuDOPage, menuApplicationConverter::convert);
    }
}
