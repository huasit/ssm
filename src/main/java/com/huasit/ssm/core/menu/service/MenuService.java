package com.huasit.ssm.core.menu.service;

import com.huasit.ssm.core.menu.entity.Menu;
import com.huasit.ssm.core.menu.entity.MenuRepository;
import com.huasit.ssm.core.role.entity.RoleMenuRepository;
import com.huasit.ssm.core.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {

    /**
     *
     */
    public Menu getMenuById(Long id) {
        return this.menuRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<Menu> list(Menu form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.menuRepository.findAll((Specification<Menu>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public List<Menu> getUserMenuTree(Long userId) {
        List<Menu> menus = this.menuRepository.findAll();
        if (menus == null) {
            return null;
        }
        Map<Long, Boolean> authMap = this.getUserAuthMenuMap(userId);
        Menu parent = this.getUserMenuTree(null, menus, authMap, null);
        return parent.getChildrens();
    }

    /**
     *
     */
    private Menu getUserMenuTree(Menu menu, List<Menu> menus, Map<Long, Boolean> authMap, Map<Long, Boolean> tempAuthMap) {
        if (menu == null) {
            menu = new Menu();
            menu.setId(0L);
            tempAuthMap = new HashMap<>();
        }
        for (Menu m : menus) {
            if (!menu.getId().equals(m.getPid())) {
                continue;
            }
            List<Menu> childrens = menu.getChildrens();
            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            Menu children = this.getUserMenuTree(m, menus, authMap, tempAuthMap);
            if (authMap.containsKey(menu.getId())) {
                tempAuthMap.put(children.getId(), true);
            }
            if (authMap.containsKey(children.getId())) {
                tempAuthMap.put(children.getId(), true);
            }
            if (authMap.containsKey(children.getId()) || tempAuthMap.containsKey(children.getId())) {
                tempAuthMap.put(menu.getId(), true);
                childrens.add(children);
            }
            menu.setChildrens(childrens);
        }
        return menu;
    }

    /**
     *
     */
    private Map<Long, Boolean> getUserAuthMenuMap(Long userId) {
        List<Menu> menus = this.menuRepository.findMenusByUserId(userId);
        Map<Long, Boolean> map = new HashMap<>();
        if (menus != null) {
            for (Menu menu : menus) {
                map.put(menu.getId(), true);
            }
        }
        return map;
    }

    /**
     *
     */
    @Autowired
    MenuRepository menuRepository;

    /**
     *
     */
    @Autowired
    RoleMenuRepository roleMenuRepository;
}
