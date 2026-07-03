<template>
  <section class="page-stack customer-page">
    <header class="page-heading">
      <div>
        <h1>客户档案</h1>
        <p>维护客户基础资料、负责人、结算偏好、联系人矩阵与地址簿。</p>
      </div>
      <div class="heading-actions">
        <button v-if="hasPermission('customer:customer:create')" class="primary-action" type="button" @click="openCreate">
          新增客户
        </button>
        <button class="secondary-action" type="button" @click="loadCustomers">刷新</button>
      </div>
    </header>

    <div v-if="errorMessage" class="error-banner">{{ errorMessage }}</div>
    <div v-if="toastMessage" class="success-banner">{{ toastMessage }}</div>

    <section class="toolbar query-panel">
      <label>
        <span>关键词</span>
        <input v-model="query.keyword" placeholder="客户编码、客户名称、税号" @keyup.enter="loadCustomers" />
      </label>
      <label>
        <span>状态</span>
        <select v-model="query.status">
          <option value="">全部状态</option>
          <option v-for="option in customerStatusOptions" :key="option.value" :value="option.value">
            {{ option.label }}
          </option>
        </select>
      </label>
      <label>
        <span>公私海</span>
        <select v-model="query.poolStatus">
          <option value="">全部</option>
          <option value="PRIVATE">私海</option>
          <option value="PUBLIC">公海</option>
        </select>
      </label>
      <div class="query-actions">
        <button type="button" @click="loadCustomers">查询</button>
        <button type="button" @click="resetQuery">重置</button>
      </div>
    </section>

    <article class="panel table-panel">
      <div class="panel__head">
        <div>
          <h2>客户列表</h2>
          <p>来自 /api/v1/customer/customers/page</p>
        </div>
        <span class="page-status">{{ loading ? '加载中' : `${total} 条` }}</span>
      </div>

      <table class="data-table dense-table">
        <thead>
          <tr>
            <th>客户编码</th>
            <th>客户名称</th>
            <th>等级</th>
            <th>结算</th>
            <th>币种</th>
            <th>负责人</th>
            <th>状态</th>
            <th>公私海</th>
            <th>更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.customerId">
            <td><code>{{ row.customerCode }}</code></td>
            <td>{{ row.customerName }}</td>
            <td>{{ row.customerLevel || '-' }}</td>
            <td>{{ settlementText(row.settlementType) }}</td>
            <td>{{ row.defaultCurrency || '-' }}</td>
            <td>{{ row.salesOwnerId || '-' }}</td>
            <td>
              <span class="status-tag" :data-tone="customerStatusTone(row.status)">
                {{ customerStatusText(row.status) }}
              </span>
            </td>
            <td>{{ row.poolStatus === 'PUBLIC' ? '公海' : '私海' }}</td>
            <td>{{ formatTime(row.updateTime || row.createTime) }}</td>
            <td class="table-actions">
              <button type="button" @click="openDetail(row)">详情</button>
              <button v-if="hasPermission('customer:customer:update')" type="button" @click="openEdit(row)">编辑</button>
              <button v-if="hasPermission('customer:customer:update')" type="button" @click="toggleStatus(row)">
                {{ row.status === 'ACTIVE' ? '冻结' : '启用' }}
              </button>
              <button v-if="hasPermission('customer:customer:delete')" type="button" @click="removeCustomer(row)">删除</button>
            </td>
          </tr>
          <tr v-if="!loading && rows.length === 0">
            <td colspan="10" class="empty-cell">当前筛选条件下没有客户数据。</td>
          </tr>
        </tbody>
      </table>
      <footer class="pagination-bar">
        <span>第 {{ query.pageNum }} 页 / 共 {{ pageCount }} 页</span>
        <select v-model.number="query.pageSize" @change="changePageSize">
          <option :value="10">10 条/页</option>
          <option :value="20">20 条/页</option>
          <option :value="50">50 条/页</option>
          <option :value="100">100 条/页</option>
        </select>
        <button type="button" :disabled="query.pageNum <= 1 || loading" @click="changePage(query.pageNum - 1)">上一页</button>
        <button type="button" :disabled="query.pageNum >= pageCount || loading" @click="changePage(query.pageNum + 1)">下一页</button>
      </footer>
    </article>

    <div v-if="customerDrawerOpen" class="crud-backdrop" @click.self="closeCustomerDrawer">
      <section class="crud-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ editingCustomerId ? '编辑客户' : '新增客户' }}</h2>
            <p>先维护客户最小可用资料，后续再扩展合同、KYC、公海和客供一体化规则。</p>
          </div>
          <button type="button" @click="closeCustomerDrawer">关闭</button>
        </header>

        <form class="crud-form" @submit.prevent="submitCustomer">
          <label>
            <span>客户编码</span>
            <input v-model="customerForm.customerCode" :disabled="Boolean(editingCustomerId)" required />
          </label>
          <label>
            <span>客户名称</span>
            <input v-model="customerForm.customerName" required />
          </label>
          <label>
            <span>客户类型</span>
            <select v-model="customerForm.customerType">
              <option value="COMPANY">企业</option>
              <option value="INDIVIDUAL">个人</option>
            </select>
          </label>
          <label>
            <span>客户等级</span>
            <select v-model="customerForm.customerLevel">
              <option value="BRONZE">普通</option>
              <option value="SILVER">银牌</option>
              <option value="GOLD">金牌</option>
              <option value="VIP">VIP</option>
            </select>
          </label>
          <label>
            <span>营业执照号</span>
            <input v-model="customerForm.bizLicenseNo" />
          </label>
          <label>
            <span>税号</span>
            <input v-model="customerForm.taxNo" />
          </label>
          <label>
            <span>结算方式</span>
            <select v-model="customerForm.settlementType">
              <option value="PREPAID">预付</option>
              <option value="CREDIT">授信</option>
              <option value="MONTHLY">月结</option>
            </select>
          </label>
          <label>
            <span>默认币种</span>
            <input v-model="customerForm.defaultCurrency" maxlength="8" />
          </label>
          <label>
            <span>销售负责人 ID</span>
            <input v-model="customerForm.salesOwnerId" />
          </label>
          <label>
            <span>负责人部门 ID</span>
            <input v-model="customerForm.ownerDeptId" />
          </label>
          <label v-if="editingCustomerId">
            <span>公私海</span>
            <select v-model="customerForm.poolStatus">
              <option value="PRIVATE">私海</option>
              <option value="PUBLIC">公海</option>
            </select>
          </label>
          <label class="form-wide">
            <span>备注</span>
            <textarea v-model="customerForm.remark" rows="4" />
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeCustomerDrawer">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="detailDrawerOpen" class="crud-backdrop" @click.self="closeDetailDrawer">
      <section class="crud-drawer customer-detail-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ selectedCustomer?.customerName || '客户详情' }}</h2>
            <p>{{ selectedCustomer?.customerCode }} · {{ customerStatusText(selectedCustomer?.status) }}</p>
          </div>
          <button type="button" @click="closeDetailDrawer">关闭</button>
        </header>

        <div class="detail-summary">
          <span>等级：{{ selectedCustomer?.customerLevel || '-' }}</span>
          <span>结算：{{ settlementText(selectedCustomer?.settlementType) }}</span>
          <span>币种：{{ selectedCustomer?.defaultCurrency || '-' }}</span>
          <span>销售负责人：{{ selectedCustomer?.salesOwnerId || '-' }}</span>
        </div>

        <nav class="detail-tabs">
          <button type="button" :class="{ active: activeTab === 'roles' }" @click="activeTab = 'roles'">业务角色</button>
          <button type="button" :class="{ active: activeTab === 'relations' }" @click="activeTab = 'relations'">客户关系</button>
          <button type="button" :class="{ active: activeTab === 'attachments' }" @click="activeTab = 'attachments'">附件</button>
          <button type="button" :class="{ active: activeTab === 'kycs' }" @click="activeTab = 'kycs'">KYC</button>
          <button type="button" :class="{ active: activeTab === 'contacts' }" @click="activeTab = 'contacts'">联系人</button>
          <button type="button" :class="{ active: activeTab === 'addresses' }" @click="activeTab = 'addresses'">地址簿</button>
        </nav>

        <section v-if="activeTab === 'roles'" class="detail-section">
          <div class="section-head">
            <h3>业务角色</h3>
            <button v-if="hasPermission('customer:role:create')" class="secondary-action" type="button" @click="openRoleForm()">
              新增角色
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>角色</th>
                <th>状态</th>
                <th>角色编码</th>
                <th>财务对冲</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="role in roles" :key="role.customerRoleId">
                <td>{{ roleTypeText(role.roleType) }}</td>
                <td>
                  <span class="status-tag" :data-tone="role.roleStatus === 'ACTIVE' ? 'success' : 'muted'">
                    {{ role.roleStatus === 'ACTIVE' ? '启用' : '停用' }}
                  </span>
                </td>
                <td><code>{{ role.roleCode || '-' }}</code></td>
                <td>{{ role.offsetEnabled ? '允许' : '不允许' }}</td>
                <td>{{ role.remark || '-' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:role:update')" type="button" @click="openRoleForm(role)">编辑</button>
                  <button v-if="hasPermission('customer:role:delete')" type="button" @click="removeRole(role)">删除</button>
                </td>
              </tr>
              <tr v-if="roles.length === 0">
                <td colspan="6" class="empty-cell">暂未维护业务角色。</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section v-else-if="activeTab === 'relations'" class="detail-section">
          <div class="section-head">
            <h3>客户关系</h3>
            <button v-if="hasPermission('customer:relation:create')" class="secondary-action" type="button" @click="openRelationForm()">
              新增关系
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>关系类型</th>
                <th>主客户</th>
                <th>关联客户</th>
                <th>独立结算</th>
                <th>状态</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="relation in relations" :key="relation.relationId">
                <td>{{ relationTypeText(relation.relationType) }}</td>
                <td>{{ relation.parentCustomerName || relation.parentCustomerId }}</td>
                <td>{{ relation.childCustomerName || relation.childCustomerId }}</td>
                <td>{{ relation.settlementIndependent ? '是' : '否' }}</td>
                <td>
                  <span class="status-tag" :data-tone="relation.status === 1 ? 'success' : 'muted'">
                    {{ relation.status === 1 ? '启用' : '停用' }}
                  </span>
                </td>
                <td>{{ relation.remark || '-' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:relation:update')" type="button" @click="openRelationForm(relation)">编辑</button>
                  <button v-if="hasPermission('customer:relation:delete')" type="button" @click="removeRelation(relation)">删除</button>
                </td>
              </tr>
              <tr v-if="relations.length === 0">
                <td colspan="7" class="empty-cell">暂未维护客户关系。</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section v-else-if="activeTab === 'attachments'" class="detail-section">
          <div class="section-head">
            <h3>附件索引</h3>
            <button v-if="hasPermission('customer:attachment:create')" class="secondary-action" type="button" @click="openAttachmentForm()">
              新增附件
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>类型</th>
                <th>文件 ID</th>
                <th>文件名</th>
                <th>到期日</th>
                <th>审核状态</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="attachment in attachments" :key="attachment.attachmentId">
                <td>{{ attachmentTypeText(attachment.attachmentType) }}</td>
                <td><code>{{ attachment.fileId }}</code></td>
                <td>{{ attachment.fileName || '-' }}</td>
                <td>{{ attachment.expireDate || '-' }}</td>
                <td>{{ auditStatusText(attachment.auditStatus) }}</td>
                <td>{{ attachment.remark || '-' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:attachment:update')" type="button" @click="openAttachmentForm(attachment)">编辑</button>
                  <button v-if="hasPermission('customer:attachment:delete')" type="button" @click="removeAttachment(attachment)">删除</button>
                </td>
              </tr>
              <tr v-if="attachments.length === 0">
                <td colspan="7" class="empty-cell">暂未维护附件索引。</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section v-else-if="activeTab === 'kycs'" class="detail-section">
          <div class="section-head">
            <h3>KYC 审核</h3>
            <button v-if="hasPermission('customer:kyc:create')" class="secondary-action" type="button" @click="openKycForm()">
              新增 KYC
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>类型</th>
                <th>状态</th>
                <th>审核人</th>
                <th>审核时间</th>
                <th>驳回原因</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="kyc in kycs" :key="kyc.kycId">
                <td>{{ kycTypeText(kyc.kycType) }}</td>
                <td>{{ auditStatusText(kyc.auditStatus) }}</td>
                <td>{{ kyc.auditBy || '-' }}</td>
                <td>{{ formatTime(kyc.auditTime) }}</td>
                <td>{{ kyc.rejectReason || '-' }}</td>
                <td>{{ kyc.remark || '-' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:kyc:update')" type="button" @click="openKycForm(kyc)">编辑</button>
                  <button v-if="hasPermission('customer:kyc:audit')" type="button" @click="auditKycRecord(kyc, 'APPROVED')">通过</button>
                  <button v-if="hasPermission('customer:kyc:audit')" type="button" @click="auditKycRecord(kyc, 'REJECTED')">驳回</button>
                  <button v-if="hasPermission('customer:kyc:delete')" type="button" @click="removeKyc(kyc)">删除</button>
                </td>
              </tr>
              <tr v-if="kycs.length === 0">
                <td colspan="7" class="empty-cell">暂未维护 KYC 记录。</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section v-else-if="activeTab === 'contacts'" class="detail-section">
          <div class="section-head">
            <h3>联系人矩阵</h3>
            <button v-if="hasPermission('customer:contact:create')" class="secondary-action" type="button" @click="openContactForm()">
              新增联系人
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>姓名</th>
                <th>角色</th>
                <th>职位</th>
                <th>电话</th>
                <th>邮箱</th>
                <th>主联系人</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="contact in contacts" :key="contact.contactId">
                <td>{{ contact.contactName }}</td>
                <td>{{ contactRoleText(contact.contactRole) }}</td>
                <td>{{ contact.position || '-' }}</td>
                <td>{{ contact.phone || '-' }}</td>
                <td>{{ contact.email || '-' }}</td>
                <td>{{ contact.primary ? '是' : '否' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:contact:update')" type="button" @click="openContactForm(contact)">编辑</button>
                  <button v-if="hasPermission('customer:contact:delete')" type="button" @click="removeContact(contact)">删除</button>
                </td>
              </tr>
              <tr v-if="contacts.length === 0">
                <td colspan="7" class="empty-cell">暂未维护联系人。</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section v-else class="detail-section">
          <div class="section-head">
            <h3>地址簿</h3>
            <button v-if="hasPermission('customer:address:create')" class="secondary-action" type="button" @click="openAddressForm()">
              新增地址
            </button>
          </div>
          <table class="data-table dense-table">
            <thead>
              <tr>
                <th>类型</th>
                <th>联系人</th>
                <th>电话</th>
                <th>国家</th>
                <th>城市</th>
                <th>邮编</th>
                <th>默认</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="address in addresses" :key="address.addressId">
                <td>{{ addressTypeText(address.addressType) }}</td>
                <td>{{ address.contactName || '-' }}</td>
                <td>{{ address.phone || '-' }}</td>
                <td>{{ address.country || address.countryCode || '-' }}</td>
                <td>{{ [address.province, address.city].filter(Boolean).join(' / ') || '-' }}</td>
                <td>{{ address.zipCode || '-' }}</td>
                <td>{{ address.defaultAddress ? '是' : '否' }}</td>
                <td class="table-actions">
                  <button v-if="hasPermission('customer:address:update')" type="button" @click="openAddressForm(address)">编辑</button>
                  <button v-if="hasPermission('customer:address:delete')" type="button" @click="removeAddress(address)">删除</button>
                </td>
              </tr>
              <tr v-if="addresses.length === 0">
                <td colspan="8" class="empty-cell">暂未维护地址。</td>
              </tr>
            </tbody>
          </table>
        </section>
      </section>
    </div>

    <div v-if="attachmentFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeAttachmentForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ attachmentForm.attachmentId ? '编辑附件' : '新增附件' }}</h2>
            <p>这里只维护文件索引，文件上传与下载由系统文件中心负责。</p>
          </div>
          <button type="button" @click="closeAttachmentForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitAttachment">
          <label>
            <span>附件类型</span>
            <select v-model="attachmentForm.attachmentType" required>
              <option value="CONTRACT_COPY">合同</option>
              <option value="LICENSE">营业执照</option>
              <option value="TAX_FILE">税务文件</option>
              <option value="KYC_FILE">KYC 文件</option>
              <option value="OTHER">其他</option>
            </select>
          </label>
          <label><span>文件 ID</span><input v-model="attachmentForm.fileId" required /></label>
          <label><span>文件名</span><input v-model="attachmentForm.fileName" /></label>
          <label><span>到期日</span><input v-model="attachmentForm.expireDate" type="date" /></label>
          <label>
            <span>审核状态</span>
            <select v-model="attachmentForm.auditStatus">
              <option value="PENDING">待审核</option>
              <option value="APPROVED">通过</option>
              <option value="REJECTED">驳回</option>
            </select>
          </label>
          <label class="form-wide"><span>备注</span><textarea v-model="attachmentForm.remark" rows="3" /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeAttachmentForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="kycFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeKycForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ kycForm.kycId ? '编辑 KYC' : '新增 KYC' }}</h2>
            <p>KYC 记录用于沉淀客户资质审核状态。</p>
          </div>
          <button type="button" @click="closeKycForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitKyc">
          <label>
            <span>KYC 类型</span>
            <select v-model="kycForm.kycType" required>
              <option value="COMPANY_LICENSE">企业营业执照</option>
              <option value="PERSONAL_ID">个人证件</option>
              <option value="TAX">税务资料</option>
              <option value="COMPLIANCE">合规资料</option>
            </select>
          </label>
          <label class="form-wide"><span>备注</span><textarea v-model="kycForm.remark" rows="3" /></label>
          <footer class="crud-actions">
            <button type="button" @click="closeKycForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="relationFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeRelationForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ relationForm.relationId ? '编辑客户关系' : '新增客户关系' }}</h2>
            <p>支持主子公司、多抬头、结算主体关系。关系双方都必须是已存在客户。</p>
          </div>
          <button type="button" @click="closeRelationForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitRelation">
          <label>
            <span>关系类型</span>
            <select v-model="relationForm.relationType" :disabled="Boolean(relationForm.relationId)" required>
              <option value="PARENT_CHILD">主子公司</option>
              <option value="BILLING_TITLE">多抬头</option>
              <option value="SETTLEMENT_SUBJECT">结算主体</option>
            </select>
          </label>
          <label>
            <span>主客户 ID</span>
            <input v-model="relationForm.parentCustomerId" :disabled="Boolean(relationForm.relationId)" required />
          </label>
          <label>
            <span>关联客户 ID</span>
            <input v-model="relationForm.childCustomerId" :disabled="Boolean(relationForm.relationId)" required />
          </label>
          <label>
            <span>状态</span>
            <select v-model.number="relationForm.status">
              <option :value="1">启用</option>
              <option :value="0">停用</option>
            </select>
          </label>
          <label class="checkbox-line">
            <input v-model="relationForm.settlementIndependent" type="checkbox" />
            <span>关联客户独立结算</span>
          </label>
          <label class="form-wide">
            <span>备注</span>
            <textarea v-model="relationForm.remark" rows="3" />
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeRelationForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="roleFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeRoleForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ roleForm.customerRoleId ? '编辑业务角色' : '新增业务角色' }}</h2>
            <p>同一客商可同时作为客户、供应商、海外代理、物流商或仓库服务商。</p>
          </div>
          <button type="button" @click="closeRoleForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitRole">
          <label>
            <span>角色类型</span>
            <select v-model="roleForm.roleType" :disabled="Boolean(roleForm.customerRoleId)" required>
              <option value="CUSTOMER">客户</option>
              <option value="SUPPLIER">供应商</option>
              <option value="OVERSEAS_AGENT">海外代理</option>
              <option value="CARRIER">物流商</option>
              <option value="WAREHOUSE_PROVIDER">仓库服务商</option>
              <option value="CUSTOMS_BROKER">清关行</option>
            </select>
          </label>
          <label>
            <span>状态</span>
            <select v-model="roleForm.roleStatus">
              <option value="ACTIVE">启用</option>
              <option value="DISABLED">停用</option>
            </select>
          </label>
          <label><span>角色编码</span><input v-model="roleForm.roleCode" placeholder="可填写财务或外部系统编码" /></label>
          <label class="checkbox-line">
            <input v-model="roleForm.offsetEnabled" type="checkbox" />
            <span>允许应收应付对冲</span>
          </label>
          <label class="form-wide">
            <span>备注</span>
            <textarea v-model="roleForm.remark" rows="3" />
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeRoleForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="contactFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeContactForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ contactForm.contactId ? '编辑联系人' : '新增联系人' }}</h2>
            <p>维护客户业务、财务、技术、仓储等联系人。</p>
          </div>
          <button type="button" @click="closeContactForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitContact">
          <label><span>姓名</span><input v-model="contactForm.contactName" required /></label>
          <label>
            <span>角色</span>
            <select v-model="contactForm.contactRole">
              <option value="BUSINESS">业务</option>
              <option value="FINANCE">财务</option>
              <option value="TECH">技术</option>
              <option value="WAREHOUSE">仓储</option>
              <option value="LEGAL">法务</option>
              <option value="MANAGER">管理层</option>
            </select>
          </label>
          <label><span>职位</span><input v-model="contactForm.position" /></label>
          <label><span>电话</span><input v-model="contactForm.phone" /></label>
          <label><span>邮箱</span><input v-model="contactForm.email" type="email" /></label>
          <label><span>社交账号</span><input v-model="contactForm.socialAccount" /></label>
          <label class="checkbox-line">
            <input v-model="contactForm.primary" type="checkbox" />
            <span>设为主联系人</span>
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeContactForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>

    <div v-if="addressFormOpen" class="crud-backdrop nested-backdrop" @click.self="closeAddressForm">
      <section class="crud-drawer small-drawer">
        <header class="crud-drawer__head">
          <div>
            <h2>{{ addressForm.addressId ? '编辑地址' : '新增地址' }}</h2>
            <p>维护注册地址、发货地址、退货地址、账单地址等。</p>
          </div>
          <button type="button" @click="closeAddressForm">关闭</button>
        </header>
        <form class="crud-form" @submit.prevent="submitAddress">
          <label>
            <span>地址类型</span>
            <select v-model="addressForm.addressType">
              <option value="REGISTERED">注册地址</option>
              <option value="SHIP_FROM">发货地址</option>
              <option value="RETURN_TO">退货地址</option>
              <option value="BILLING">账单地址</option>
              <option value="WAREHOUSE_CONTACT">仓库联系人</option>
              <option value="OTHER">其他</option>
            </select>
          </label>
          <label><span>联系人</span><input v-model="addressForm.contactName" /></label>
          <label><span>电话</span><input v-model="addressForm.phone" /></label>
          <label><span>国家</span><input v-model="addressForm.country" /></label>
          <label><span>国家代码</span><input v-model="addressForm.countryCode" maxlength="8" /></label>
          <label><span>省/州</span><input v-model="addressForm.province" /></label>
          <label><span>城市</span><input v-model="addressForm.city" /></label>
          <label><span>区县</span><input v-model="addressForm.district" /></label>
          <label><span>邮编</span><input v-model="addressForm.zipCode" /></label>
          <label class="form-wide"><span>详细地址</span><textarea v-model="addressForm.street" rows="3" /></label>
          <label class="checkbox-line">
            <input v-model="addressForm.defaultAddress" type="checkbox" />
            <span>设为该类型默认地址</span>
          </label>
          <footer class="crud-actions">
            <button type="button" @click="closeAddressForm">取消</button>
            <button class="primary-action" type="submit" :disabled="saving">{{ saving ? '保存中' : '保存' }}</button>
          </footer>
        </form>
      </section>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import type {
  ApiId,
  CustomerAddress,
  CustomerAddressPayload,
  CustomerAttachment,
  CustomerAttachmentPayload,
  CustomerContact,
  CustomerContactPayload,
  CustomerCreatePayload,
  CustomerKyc,
  CustomerKycPayload,
  CustomerPoolStatus,
  CustomerProfile,
  CustomerRelation,
  CustomerRelationPayload,
  CustomerRelationType,
  CustomerRole,
  CustomerRolePayload,
  CustomerRoleType,
  CustomerStatus,
  CustomerUpdatePayload
} from '@myow/api';
import { confirmDelete, confirmImportantAction } from '@/composables/use-confirm-action';
import { usePermission } from '@/composables/use-permission';
import {
  changeCustomerStatus,
  createCustomer,
  createCustomerAddress,
  createCustomerAttachment,
  createCustomerContact,
  createCustomerKyc,
  createCustomerRelation,
  createCustomerRole,
  deleteCustomer,
  deleteCustomerAddress,
  deleteCustomerAttachment,
  deleteCustomerContact,
  deleteCustomerKyc,
  deleteCustomerRelation,
  deleteCustomerRole,
  getCustomer,
  pageCustomerAddresses,
  pageCustomerAttachments,
  pageCustomerContacts,
  pageCustomerKycs,
  pageCustomerRelations,
  pageCustomerRoles,
  pageCustomers,
  updateCustomer,
  updateCustomerAddress,
  updateCustomerAttachment,
  updateCustomerContact,
  updateCustomerKyc,
  auditCustomerKyc,
  updateCustomerRelation,
  updateCustomerRole
} from '@/services/customer-service';

const { hasPermission } = usePermission();

const customerStatusOptions: Array<{ label: string; value: CustomerStatus }> = [
  { label: '待审核', value: 'PENDING' },
  { label: '启用', value: 'ACTIVE' },
  { label: '冻结', value: 'SUSPENDED' },
  { label: '终止', value: 'TERMINATED' }
];

const loading = ref(false);
const saving = ref(false);
const errorMessage = ref('');
const toastMessage = ref('');
const rows = ref<CustomerProfile[]>([]);
const total = ref(0);
const query = reactive({
  keyword: '',
  status: '' as CustomerStatus | '',
  poolStatus: '' as CustomerPoolStatus | '',
  pageNum: 1,
  pageSize: 20
});
const pageCount = computed(() => Math.max(1, Math.ceil(total.value / query.pageSize)));

const customerDrawerOpen = ref(false);
const editingCustomerId = ref<ApiId | undefined>();
const customerForm = reactive<CustomerCreatePayload & Partial<CustomerUpdatePayload>>({
  customerCode: '',
  customerName: '',
  customerType: 'COMPANY',
  customerLevel: 'BRONZE',
  bizLicenseNo: '',
  taxNo: '',
  settlementType: 'PREPAID',
  defaultCurrency: 'USD',
  salesOwnerId: undefined,
  ownerDeptId: undefined,
  poolStatus: 'PRIVATE',
  remark: ''
});

const detailDrawerOpen = ref(false);
const selectedCustomer = ref<CustomerProfile | undefined>();
const activeTab = ref<'roles' | 'relations' | 'attachments' | 'kycs' | 'contacts' | 'addresses'>('roles');
const roles = ref<CustomerRole[]>([]);
const relations = ref<CustomerRelation[]>([]);
const attachments = ref<CustomerAttachment[]>([]);
const kycs = ref<CustomerKyc[]>([]);
const contacts = ref<CustomerContact[]>([]);
const addresses = ref<CustomerAddress[]>([]);

const roleFormOpen = ref(false);
const roleForm = reactive<CustomerRolePayload>({
  roleType: 'CUSTOMER',
  roleStatus: 'ACTIVE',
  roleCode: '',
  offsetEnabled: false,
  remark: ''
});

const relationFormOpen = ref(false);
const relationForm = reactive<CustomerRelationPayload>({
  parentCustomerId: undefined,
  childCustomerId: undefined,
  relationType: 'PARENT_CHILD',
  settlementIndependent: false,
  status: 1,
  remark: ''
});

const attachmentFormOpen = ref(false);
const attachmentForm = reactive<CustomerAttachmentPayload>({
  attachmentType: 'LICENSE',
  fileId: undefined,
  fileName: '',
  expireDate: '',
  auditStatus: 'PENDING',
  remark: ''
});

const kycFormOpen = ref(false);
const kycForm = reactive<CustomerKycPayload>({
  kycType: 'COMPANY_LICENSE',
  remark: ''
});

const contactFormOpen = ref(false);
const contactForm = reactive<CustomerContactPayload>({
  contactName: '',
  contactRole: 'BUSINESS',
  position: '',
  phone: '',
  email: '',
  socialAccount: '',
  primary: false,
  status: 1
});

const addressFormOpen = ref(false);
const addressForm = reactive<CustomerAddressPayload>({
  addressType: 'REGISTERED',
  contactName: '',
  phone: '',
  country: '',
  countryCode: '',
  province: '',
  city: '',
  district: '',
  street: '',
  zipCode: '',
  defaultAddress: false,
  status: 1
});

onMounted(() => {
  void loadCustomers();
});

async function loadCustomers() {
  loading.value = true;
  errorMessage.value = '';
  try {
    const page = await pageCustomers({
      keyword: query.keyword || undefined,
      status: query.status || undefined,
      poolStatus: query.poolStatus || undefined,
      pageNum: query.pageNum,
      pageSize: query.pageSize
    });
    rows.value = page.list ?? [];
    total.value = Number(page.total ?? rows.value.length);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '客户数据加载失败';
  } finally {
    loading.value = false;
  }
}

function changePage(pageNum: number) {
  query.pageNum = Math.max(1, pageNum);
  void loadCustomers();
}

function changePageSize() {
  query.pageNum = 1;
  void loadCustomers();
}

function resetQuery() {
  query.keyword = '';
  query.status = '';
  query.poolStatus = '';
  query.pageNum = 1;
  void loadCustomers();
}

function openCreate() {
  editingCustomerId.value = undefined;
  resetCustomerForm();
  customerDrawerOpen.value = true;
}

function openEdit(row: CustomerProfile) {
  editingCustomerId.value = row.customerId;
  Object.assign(customerForm, {
    customerId: row.customerId,
    customerCode: row.customerCode,
    customerName: row.customerName,
    customerType: row.customerType || 'COMPANY',
    customerLevel: row.customerLevel || 'BRONZE',
    bizLicenseNo: row.bizLicenseNo || '',
    taxNo: row.taxNo || '',
    settlementType: row.settlementType || 'PREPAID',
    defaultCurrency: row.defaultCurrency || 'USD',
    salesOwnerId: row.salesOwnerId,
    ownerDeptId: row.ownerDeptId,
    poolStatus: row.poolStatus || 'PRIVATE',
    remark: row.remark || ''
  });
  customerDrawerOpen.value = true;
}

async function openDetail(row: CustomerProfile) {
  selectedCustomer.value = await getCustomer(row.customerId);
  detailDrawerOpen.value = true;
  activeTab.value = 'roles';
  await loadCustomerChildren(row.customerId);
}

async function loadCustomerChildren(customerId: ApiId) {
  const [rolePage, relationPage, attachmentPage, kycPage, contactPage, addressPage] = await Promise.all([
    pageCustomerRoles(customerId),
    pageCustomerRelations(customerId),
    pageCustomerAttachments(customerId),
    pageCustomerKycs(customerId),
    pageCustomerContacts(customerId),
    pageCustomerAddresses(customerId)
  ]);
  roles.value = rolePage.list ?? [];
  relations.value = relationPage.list ?? [];
  attachments.value = attachmentPage.list ?? [];
  kycs.value = kycPage.list ?? [];
  contacts.value = contactPage.list ?? [];
  addresses.value = addressPage.list ?? [];
}

async function submitCustomer() {
  saving.value = true;
  try {
    if (editingCustomerId.value) {
      await updateCustomer({
        customerId: editingCustomerId.value,
        customerName: customerForm.customerName,
        customerType: customerForm.customerType,
        customerLevel: customerForm.customerLevel,
        bizLicenseNo: customerForm.bizLicenseNo,
        taxNo: customerForm.taxNo,
        settlementType: customerForm.settlementType,
        defaultCurrency: customerForm.defaultCurrency,
        salesOwnerId: customerForm.salesOwnerId,
        ownerDeptId: customerForm.ownerDeptId,
        poolStatus: customerForm.poolStatus,
        remark: customerForm.remark
      });
      showToast('客户已更新');
    } else {
      await createCustomer(customerForm as CustomerCreatePayload);
      showToast('客户已创建');
    }
    closeCustomerDrawer();
    await loadCustomers();
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '客户保存失败';
  } finally {
    saving.value = false;
  }
}

async function toggleStatus(row: CustomerProfile) {
  const nextStatus: CustomerStatus = row.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
  const action = nextStatus === 'ACTIVE' ? '启用' : '冻结';
  if (!confirmImportantAction({
    title: `${action}客户 ${row.customerName}`,
    risk: '客户状态会影响订单、结算、账号访问和业务处理，请确认该客户当前状态变更合理。',
    confirmText: `确认${action}客户 ${row.customerName}？`
  })) return;
  await changeCustomerStatus(row.customerId, nextStatus);
  showToast('客户状态已更新');
  await loadCustomers();
}

async function removeCustomer(row: CustomerProfile) {
  if (!confirmDelete(`客户 ${row.customerName}`, '删除客户会影响客商档案、联系人、地址、附件、角色和后续业务引用。已有业务数据时建议冻结。')) return;
  await deleteCustomer(row.customerId);
  showToast('客户已删除');
  await loadCustomers();
}

function closeCustomerDrawer() {
  customerDrawerOpen.value = false;
  editingCustomerId.value = undefined;
}

function closeDetailDrawer() {
  detailDrawerOpen.value = false;
  selectedCustomer.value = undefined;
  roles.value = [];
  relations.value = [];
  attachments.value = [];
  kycs.value = [];
  contacts.value = [];
  addresses.value = [];
}

function openRelationForm(relation?: CustomerRelation) {
  Object.assign(relationForm, relation ? {
    relationId: relation.relationId,
    parentCustomerId: relation.parentCustomerId,
    childCustomerId: relation.childCustomerId,
    relationType: relation.relationType,
    settlementIndependent: Boolean(relation.settlementIndependent),
    status: relation.status ?? 1,
    remark: relation.remark || ''
  } : {
    relationId: undefined,
    parentCustomerId: selectedCustomer.value?.customerId,
    childCustomerId: undefined,
    relationType: 'PARENT_CHILD' as CustomerRelationType,
    settlementIndependent: false,
    status: 1,
    remark: ''
  });
  relationFormOpen.value = true;
}

async function submitRelation() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (relationForm.relationId) {
      await updateCustomerRelation(relationForm);
    } else {
      await createCustomerRelation(relationForm);
    }
    showToast('客户关系已保存');
    closeRelationForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '客户关系保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeRelation(relation: CustomerRelation) {
  if (!selectedCustomer.value || !confirmDelete(`关系 ${relationTypeText(relation.relationType)}`, '删除客户关系会影响主子公司、多抬头和结算独立性判断。')) return;
  await deleteCustomerRelation(relation.relationId);
  showToast('客户关系已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeRelationForm() {
  relationFormOpen.value = false;
}

function openAttachmentForm(attachment?: CustomerAttachment) {
  Object.assign(attachmentForm, attachment ? {
    attachmentId: attachment.attachmentId,
    attachmentType: attachment.attachmentType,
    fileId: attachment.fileId,
    fileName: attachment.fileName || '',
    expireDate: attachment.expireDate || '',
    auditStatus: attachment.auditStatus || 'PENDING',
    remark: attachment.remark || ''
  } : {
    attachmentId: undefined,
    attachmentType: 'LICENSE',
    fileId: undefined,
    fileName: '',
    expireDate: '',
    auditStatus: 'PENDING',
    remark: ''
  });
  attachmentFormOpen.value = true;
}

async function submitAttachment() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (attachmentForm.attachmentId) {
      await updateCustomerAttachment(attachmentForm);
    } else {
      await createCustomerAttachment(selectedCustomer.value.customerId, attachmentForm);
    }
    showToast('附件索引已保存');
    closeAttachmentForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '附件保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeAttachment(attachment: CustomerAttachment) {
  if (!selectedCustomer.value || !confirmDelete(`附件 ${attachment.fileName || attachment.fileId}`, '删除附件索引后，业务人员可能无法继续查看合同、资质或证明材料。')) return;
  await deleteCustomerAttachment(attachment.attachmentId);
  showToast('附件已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeAttachmentForm() {
  attachmentFormOpen.value = false;
}

function openKycForm(kyc?: CustomerKyc) {
  Object.assign(kycForm, kyc ? {
    kycId: kyc.kycId,
    kycType: kyc.kycType,
    remark: kyc.remark || ''
  } : {
    kycId: undefined,
    kycType: 'COMPANY_LICENSE',
    remark: ''
  });
  kycFormOpen.value = true;
}

async function submitKyc() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (kycForm.kycId) {
      await updateCustomerKyc(kycForm);
    } else {
      await createCustomerKyc(selectedCustomer.value.customerId, kycForm);
    }
    showToast('KYC 已保存');
    closeKycForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'KYC 保存失败';
  } finally {
    saving.value = false;
  }
}

async function auditKycRecord(kyc: CustomerKyc, auditStatus: 'APPROVED' | 'REJECTED') {
  if (!selectedCustomer.value) return;
  if (!confirmImportantAction({
    title: `${auditStatus === 'APPROVED' ? '通过' : '驳回'} KYC ${kycTypeText(kyc.kycType)}`,
    risk: 'KYC 审核结果会影响客户准入、风控和后续业务处理。',
    confirmText: `确认${auditStatus === 'APPROVED' ? '通过' : '驳回'}该 KYC？`
  })) return;
  const rejectReason = auditStatus === 'REJECTED' ? window.prompt('请输入驳回原因') || '' : '';
  await auditCustomerKyc({ kycId: kyc.kycId, auditStatus, rejectReason });
  showToast(auditStatus === 'APPROVED' ? 'KYC 已通过' : 'KYC 已驳回');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

async function removeKyc(kyc: CustomerKyc) {
  if (!selectedCustomer.value || !confirmDelete(`KYC ${kycTypeText(kyc.kycType)}`, '删除 KYC 记录会影响合规审计和客户准入判断。')) return;
  await deleteCustomerKyc(kyc.kycId);
  showToast('KYC 已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeKycForm() {
  kycFormOpen.value = false;
}

function openRoleForm(role?: CustomerRole) {
  Object.assign(roleForm, role ? {
    customerRoleId: role.customerRoleId,
    roleType: role.roleType,
    roleStatus: role.roleStatus || 'ACTIVE',
    roleCode: role.roleCode || '',
    offsetEnabled: Boolean(role.offsetEnabled),
    remark: role.remark || ''
  } : {
    customerRoleId: undefined,
    roleType: 'CUSTOMER' as CustomerRoleType,
    roleStatus: 'ACTIVE',
    roleCode: '',
    offsetEnabled: false,
    remark: ''
  });
  roleFormOpen.value = true;
}

async function submitRole() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (roleForm.customerRoleId) {
      await updateCustomerRole(roleForm);
    } else {
      await createCustomerRole(selectedCustomer.value.customerId, roleForm);
    }
    showToast('业务角色已保存');
    closeRoleForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '业务角色保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeRole(role: CustomerRole) {
  if (!selectedCustomer.value || !confirmDelete(`角色 ${roleTypeText(role.roleType)}`, '删除业务角色会影响客供一体、财务对冲和业务模块引用。')) return;
  await deleteCustomerRole(role.customerRoleId);
  showToast('业务角色已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeRoleForm() {
  roleFormOpen.value = false;
}

function openContactForm(contact?: CustomerContact) {
  Object.assign(contactForm, contact ? {
    contactId: contact.contactId,
    contactName: contact.contactName,
    contactRole: contact.contactRole || 'BUSINESS',
    position: contact.position || '',
    phone: contact.phone || '',
    email: contact.email || '',
    socialAccount: contact.socialAccount || '',
    primary: Boolean(contact.primary),
    status: contact.status ?? 1
  } : {
    contactId: undefined,
    contactName: '',
    contactRole: 'BUSINESS',
    position: '',
    phone: '',
    email: '',
    socialAccount: '',
    primary: false,
    status: 1
  });
  contactFormOpen.value = true;
}

async function submitContact() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (contactForm.contactId) {
      await updateCustomerContact(contactForm);
    } else {
      await createCustomerContact(selectedCustomer.value.customerId, contactForm);
    }
    showToast('联系人已保存');
    closeContactForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '联系人保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeContact(contact: CustomerContact) {
  if (!selectedCustomer.value || !confirmDelete(`联系人 ${contact.contactName}`, '删除联系人会影响业务、财务、技术或仓储沟通记录。')) return;
  await deleteCustomerContact(contact.contactId);
  showToast('联系人已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeContactForm() {
  contactFormOpen.value = false;
}

function openAddressForm(address?: CustomerAddress) {
  Object.assign(addressForm, address ? {
    addressId: address.addressId,
    addressType: address.addressType || 'REGISTERED',
    contactName: address.contactName || '',
    phone: address.phone || '',
    country: address.country || '',
    countryCode: address.countryCode || '',
    province: address.province || '',
    city: address.city || '',
    district: address.district || '',
    street: address.street || '',
    zipCode: address.zipCode || '',
    defaultAddress: Boolean(address.defaultAddress),
    status: address.status ?? 1
  } : {
    addressId: undefined,
    addressType: 'REGISTERED',
    contactName: '',
    phone: '',
    country: '',
    countryCode: '',
    province: '',
    city: '',
    district: '',
    street: '',
    zipCode: '',
    defaultAddress: false,
    status: 1
  });
  addressFormOpen.value = true;
}

async function submitAddress() {
  if (!selectedCustomer.value) return;
  saving.value = true;
  try {
    if (addressForm.addressId) {
      await updateCustomerAddress(addressForm);
    } else {
      await createCustomerAddress(selectedCustomer.value.customerId, addressForm);
    }
    showToast('地址已保存');
    closeAddressForm();
    await loadCustomerChildren(selectedCustomer.value.customerId);
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : '地址保存失败';
  } finally {
    saving.value = false;
  }
}

async function removeAddress(address: CustomerAddress) {
  if (!selectedCustomer.value || !confirmDelete(`地址 ${address.contactName || address.addressId}`, '删除地址会影响发货、退货、注册地址和默认地址选择。')) return;
  await deleteCustomerAddress(address.addressId);
  showToast('地址已删除');
  await loadCustomerChildren(selectedCustomer.value.customerId);
}

function closeAddressForm() {
  addressFormOpen.value = false;
}

function resetCustomerForm() {
  Object.assign(customerForm, {
    customerCode: '',
    customerName: '',
    customerType: 'COMPANY',
    customerLevel: 'BRONZE',
    bizLicenseNo: '',
    taxNo: '',
    settlementType: 'PREPAID',
    defaultCurrency: 'USD',
    salesOwnerId: undefined,
    ownerDeptId: undefined,
    poolStatus: 'PRIVATE',
    remark: ''
  });
}

function showToast(message: string) {
  toastMessage.value = message;
  window.setTimeout(() => {
    toastMessage.value = '';
  }, 2200);
}

function customerStatusText(status?: string) {
  return customerStatusOptions.find((option) => option.value === status)?.label || status || '-';
}

function customerStatusTone(status?: string) {
  if (status === 'ACTIVE') return 'success';
  if (status === 'PENDING') return 'warning';
  if (status === 'SUSPENDED') return 'danger';
  return 'muted';
}

function settlementText(value?: string) {
  const map: Record<string, string> = {
    PREPAID: '预付',
    CREDIT: '授信',
    MONTHLY: '月结'
  };
  return value ? map[value] ?? value : '-';
}

function contactRoleText(value?: string) {
  const map: Record<string, string> = {
    BUSINESS: '业务',
    FINANCE: '财务',
    TECH: '技术',
    WAREHOUSE: '仓储',
    LEGAL: '法务',
    MANAGER: '管理层'
  };
  return value ? map[value] ?? value : '-';
}

function roleTypeText(value?: string) {
  const map: Record<string, string> = {
    CUSTOMER: '客户',
    SUPPLIER: '供应商',
    OVERSEAS_AGENT: '海外代理',
    CARRIER: '物流商',
    WAREHOUSE_PROVIDER: '仓库服务商',
    CUSTOMS_BROKER: '清关行'
  };
  return value ? map[value] ?? value : '-';
}

function relationTypeText(value?: string) {
  const map: Record<string, string> = {
    PARENT_CHILD: '主子公司',
    BILLING_TITLE: '多抬头',
    SETTLEMENT_SUBJECT: '结算主体'
  };
  return value ? map[value] ?? value : '-';
}

function attachmentTypeText(value?: string) {
  const map: Record<string, string> = {
    CONTRACT_COPY: '合同',
    LICENSE: '营业执照',
    TAX_FILE: '税务文件',
    KYC_FILE: 'KYC 文件',
    OTHER: '其他'
  };
  return value ? map[value] ?? value : '-';
}

function kycTypeText(value?: string) {
  const map: Record<string, string> = {
    COMPANY_LICENSE: '企业营业执照',
    PERSONAL_ID: '个人证件',
    TAX: '税务资料',
    COMPLIANCE: '合规资料'
  };
  return value ? map[value] ?? value : '-';
}

function auditStatusText(value?: string) {
  const map: Record<string, string> = {
    PENDING: '待审核',
    APPROVED: '通过',
    REJECTED: '驳回'
  };
  return value ? map[value] ?? value : '-';
}

function addressTypeText(value?: string) {
  const map: Record<string, string> = {
    REGISTERED: '注册地址',
    SHIP_FROM: '发货地址',
    RETURN_TO: '退货地址',
    BILLING: '账单地址',
    WAREHOUSE_CONTACT: '仓库联系人',
    OTHER: '其他'
  };
  return value ? map[value] ?? value : '-';
}

function formatTime(value?: string) {
  if (!value) return '-';
  return value.replace('T', ' ').slice(0, 16);
}
</script>

<style scoped>
.customer-page .data-table th,
.customer-page .data-table td {
  white-space: nowrap;
}

.customer-detail-drawer {
  width: min(1120px, calc(100vw - 48px));
}

.small-drawer {
  width: min(720px, calc(100vw - 48px));
}

.nested-backdrop {
  z-index: 40;
}

.detail-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
  margin: 16px 0;
}

.detail-summary span {
  border: 1px solid var(--border-subtle);
  border-radius: 6px;
  padding: 10px 12px;
  color: var(--text-secondary);
  background: var(--surface-muted);
}

.detail-tabs {
  display: flex;
  gap: 8px;
  border-bottom: 1px solid var(--border-subtle);
  margin-bottom: 14px;
}

.detail-tabs button {
  border: 0;
  border-radius: 0;
  padding: 10px 14px;
  background: transparent;
  color: var(--text-secondary);
}

.detail-tabs button.active {
  color: var(--brand-primary);
  box-shadow: inset 0 -2px 0 var(--brand-primary);
}

.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-head h3 {
  margin: 0;
  font-size: 16px;
}

.checkbox-line {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  padding-top: 22px;
}

.checkbox-line input {
  width: auto;
}

@media (max-width: 900px) {
  .detail-summary {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
