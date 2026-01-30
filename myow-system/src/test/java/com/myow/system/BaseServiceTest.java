package com.myow.system;

import com.myow.system.application.converter.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service测试基类")
public abstract class BaseServiceTest {

    @Mock
    protected UserApplicationConverter userApplicationConverter;

    @Mock
    protected RoleApplicationConverter roleApplicationConverter;

    @Mock
    protected DeptApplicationConverter deptApplicationConverter;

    @Mock
    protected MenuApplicationConverter menuApplicationConverter;

    @Mock
    protected DictApplicationConverter dictApplicationConverter;

    @Mock
    protected DictDataApplicationConverter dictDataApplicationConverter;

    @Mock
    protected TenantApplicationConverter tenantApplicationConverter;

    @Mock
    protected TenantPlansApplicationConverter tenantPlansApplicationConverter;

    @Mock
    protected PositionApplicationConverter positionApplicationConverter;

    @Mock
    protected I18nKeyApplicationConverter i18nKeyApplicationConverter;

    @Mock
    protected I18nMessageApplicationConverter i18nMessageApplicationConverter;

    @Mock
    protected SerialNoConfigApplicationConverter serialNoConfigApplicationConverter;

}
