package com.chengtai.fashionmgmt.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.chengtai.fashionmgmt.module.auth.context.LoginUserContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditMetaObjectHandler implements MetaObjectHandler {
    /**
     * MyBatis-Plus 新增时自动填充创建时间、更新时间和创建人。
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        strictInsertFill(metaObject, "createdAt", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, now);
        if (metaObject.hasSetter("createdBy") && getFieldValByName("createdBy", metaObject) == null) {
            LoginUserContext.LoginUser user = LoginUserContext.get();
            if (user != null) {
                setFieldValByName("createdBy", user.id(), metaObject);
            }
        }
        if (metaObject.hasSetter("isDel") && getFieldValByName("isDel", metaObject) == null) {
            setFieldValByName("isDel", 0, metaObject);
        }
    }

    /**
     * MyBatis-Plus 修改时自动填充更新时间。
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }
}
