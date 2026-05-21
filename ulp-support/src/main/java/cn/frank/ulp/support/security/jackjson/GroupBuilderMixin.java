/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.security.jackjson;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.support.security.userdetails.Group;

/**
 * 组构建器Mixin类
 * 用于扩展组构建器功能
 */
public class GroupBuilderMixin {
   
   /**
    * 组列表
    */
   private List<Group> groups = new ArrayList<>();

   /**
    * 添加组
    * 
    * @param group 组
    * @return 组构建器Mixin
    */
   public GroupBuilderMixin addGroup(Group group) {
      this.groups.add(group);
      return this;
   }

   /**
    * 获取组列表
    * 
    * @return 组列表
    */
   public List<Group> getGroups() {
      return this.groups;
   }
}
