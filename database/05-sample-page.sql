INSERT INTO ct_gm.t_ct_page(page_code,page_name,module_code,route_path,page_type,is_menu,sort_no,status,remark,created_at,updated_at,is_del)
VALUES('prod.sample','样衣管理','production','/samples',1,1,179,1,'样衣登记、客户确认和订单放行',NOW(),NOW(),0)
ON DUPLICATE KEY UPDATE page_name=VALUES(page_name),module_code=VALUES(module_code),route_path=VALUES(route_path),page_type=VALUES(page_type),is_menu=VALUES(is_menu),sort_no=VALUES(sort_no),status=VALUES(status),updated_at=NOW(),is_del=0;
