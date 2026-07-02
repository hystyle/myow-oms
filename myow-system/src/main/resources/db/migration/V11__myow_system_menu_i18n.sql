-- Localize current startup-phase system menus.

UPDATE sys_menu
SET menu_name = CASE menu_id
    WHEN 1000 THEN '系统中心'
    WHEN 1100 THEN '用户账号'
    WHEN 1101 THEN '新增用户'
    WHEN 1102 THEN '编辑用户'
    WHEN 1103 THEN '删除用户'
    WHEN 1200 THEN '角色权限'
    WHEN 1300 THEN '菜单权限'
    WHEN 1400 THEN '部门组织'
    WHEN 1700 THEN '字典管理'
    WHEN 1820 THEN '登录日志'
    WHEN 1900 THEN '系统运维'
    WHEN 1910 THEN '定时任务'
    WHEN 1911 THEN '新增任务'
    WHEN 1912 THEN '编辑任务'
    WHEN 1913 THEN '删除任务'
    WHEN 1914 THEN '执行任务'
    WHEN 1915 THEN '暂停任务'
    WHEN 1916 THEN '恢复任务'
    WHEN 1920 THEN '通知公告'
    WHEN 1921 THEN '新增公告'
    WHEN 1922 THEN '编辑公告'
    WHEN 1923 THEN '删除公告'
    WHEN 1924 THEN '发布公告'
    WHEN 1925 THEN '下线公告'
    WHEN 1930 THEN '文件管理'
    WHEN 1931 THEN '上传文件'
    WHEN 1932 THEN '删除文件'
    WHEN 1933 THEN '下载文件'
    WHEN 1940 THEN '站点配置'
    WHEN 1941 THEN '新增配置'
    WHEN 1942 THEN '编辑配置'
    WHEN 1943 THEN '删除配置'
    WHEN 1944 THEN '刷新配置缓存'
    WHEN 1950 THEN '敏感词'
    WHEN 1951 THEN '新增敏感词'
    WHEN 1952 THEN '编辑敏感词'
    WHEN 1953 THEN '删除敏感词'
    WHEN 1954 THEN '导入敏感词'
    WHEN 1960 THEN '消息模板'
    WHEN 1961 THEN '新增模板'
    WHEN 1962 THEN '编辑模板'
    WHEN 1963 THEN '删除模板'
    WHEN 1964 THEN '预览模板'
    WHEN 1970 THEN '导出任务'
    WHEN 1971 THEN '创建导出'
    WHEN 1972 THEN '删除导出'
    WHEN 1973 THEN '下载导出'
    WHEN 1980 THEN '系统监控'
    WHEN 1990 THEN '在线用户'
    WHEN 1991 THEN '踢出用户'
    ELSE menu_name
END,
update_time = CURRENT_TIMESTAMP(3)
WHERE menu_id IN (
    1000, 1100, 1101, 1102, 1103,
    1200, 1300, 1400, 1700, 1820,
    1900, 1910, 1911, 1912, 1913, 1914, 1915, 1916,
    1920, 1921, 1922, 1923, 1924, 1925,
    1930, 1931, 1932, 1933,
    1940, 1941, 1942, 1943, 1944,
    1950, 1951, 1952, 1953, 1954,
    1960, 1961, 1962, 1963, 1964,
    1970, 1971, 1972, 1973,
    1980, 1990, 1991
);
