import type { ApiId, PageQuery } from './common';

export type CustomerStatus = 'PENDING' | 'ACTIVE' | 'SUSPENDED' | 'TERMINATED';
export type CustomerPoolStatus = 'PRIVATE' | 'PUBLIC';

export interface CustomerProfile {
  customerId: ApiId;
  tenantId?: ApiId;
  customerCode: string;
  customerName: string;
  customerType?: string;
  customerLevel?: string;
  bizLicenseNo?: string;
  taxNo?: string;
  settlementType?: string;
  defaultCurrency?: string;
  status?: CustomerStatus;
  salesOwnerId?: ApiId;
  ownerDeptId?: ApiId;
  poolStatus?: CustomerPoolStatus;
  registerTime?: string;
  auditTime?: string;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerPageQuery extends PageQuery {
  tenantId?: ApiId;
  salesOwnerId?: ApiId;
  poolStatus?: CustomerPoolStatus | '';
}

export interface CustomerCreatePayload {
  tenantId?: ApiId;
  customerCode: string;
  customerName: string;
  customerType?: string;
  customerLevel?: string;
  bizLicenseNo?: string;
  taxNo?: string;
  settlementType?: string;
  defaultCurrency?: string;
  salesOwnerId?: ApiId;
  ownerDeptId?: ApiId;
  remark?: string;
}

export interface CustomerUpdatePayload extends Omit<CustomerCreatePayload, 'customerCode'> {
  customerId: ApiId;
  poolStatus?: CustomerPoolStatus;
}

export interface CustomerContact {
  contactId: ApiId;
  tenantId?: ApiId;
  customerId: ApiId;
  contactName: string;
  contactRole?: string;
  position?: string;
  phone?: string;
  email?: string;
  socialAccount?: string;
  primary?: boolean;
  status?: number;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerContactPayload {
  customerId?: ApiId;
  contactId?: ApiId;
  contactName: string;
  contactRole?: string;
  position?: string;
  phone?: string;
  email?: string;
  socialAccount?: string;
  primary?: boolean;
  status?: number;
}

export type CustomerRoleType = 'CUSTOMER' | 'SUPPLIER' | 'OVERSEAS_AGENT' | 'CARRIER' | 'WAREHOUSE_PROVIDER' | 'CUSTOMS_BROKER';

export interface CustomerRole {
  customerRoleId: ApiId;
  tenantId?: ApiId;
  customerId: ApiId;
  roleType: CustomerRoleType;
  roleStatus?: string;
  roleCode?: string;
  offsetEnabled?: boolean;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerRolePayload {
  customerRoleId?: ApiId;
  customerId?: ApiId;
  roleType?: CustomerRoleType;
  roleStatus?: string;
  roleCode?: string;
  offsetEnabled?: boolean;
  remark?: string;
}

export interface CustomerRoleOptionQuery {
  tenantId?: ApiId;
  roleType: CustomerRoleType;
  keyword?: string;
  limit?: number;
}

export interface CustomerRoleValidatePayload {
  customerId: ApiId;
  roleType: CustomerRoleType;
}

export interface CustomerOption {
  customerId: ApiId;
  customerCode: string;
  customerName: string;
  status?: CustomerStatus;
}

export type BlacklistTargetType = 'CUSTOMER_ID' | 'TAX_NO' | 'LICENSE_NO' | 'PHONE' | 'EMAIL';

export interface CustomerBlacklist {
  blacklistId: ApiId;
  tenantId?: ApiId;
  targetType: BlacklistTargetType;
  targetValue: string;
  riskLevel?: string;
  reason: string;
  sourceCustomerId?: ApiId;
  status?: string;
  effectiveTime?: string;
  expireTime?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerBlacklistQuery extends PageQuery {
  tenantId?: ApiId;
  targetType?: BlacklistTargetType | '';
}

export interface CustomerBlacklistPayload {
  blacklistId?: ApiId;
  tenantId?: ApiId;
  targetType: BlacklistTargetType;
  targetValue: string;
  riskLevel?: string;
  reason: string;
  sourceCustomerId?: ApiId;
  status?: string;
  effectiveTime?: string;
  expireTime?: string;
}

export type CustomerRelationType = 'PARENT_CHILD' | 'BILLING_TITLE' | 'SETTLEMENT_SUBJECT';

export interface CustomerRelation {
  relationId: ApiId;
  tenantId?: ApiId;
  parentCustomerId: ApiId;
  parentCustomerName?: string;
  childCustomerId: ApiId;
  childCustomerName?: string;
  relationType: CustomerRelationType;
  settlementIndependent?: boolean;
  status?: number;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerRelationPayload {
  relationId?: ApiId;
  parentCustomerId?: ApiId;
  childCustomerId?: ApiId;
  relationType?: CustomerRelationType;
  settlementIndependent?: boolean;
  status?: number;
  remark?: string;
}

export interface CustomerAttachment {
  attachmentId: ApiId;
  tenantId?: ApiId;
  customerId: ApiId;
  attachmentType: string;
  fileId: ApiId;
  fileName?: string;
  expireDate?: string;
  auditStatus?: string;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerAttachmentPayload {
  attachmentId?: ApiId;
  customerId?: ApiId;
  attachmentType: string;
  fileId?: ApiId;
  fileName?: string;
  expireDate?: string;
  auditStatus?: string;
  remark?: string;
}

export interface CustomerKyc {
  kycId: ApiId;
  tenantId?: ApiId;
  customerId: ApiId;
  kycType: string;
  auditStatus?: string;
  auditBy?: ApiId;
  auditTime?: string;
  rejectReason?: string;
  remark?: string;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerKycPayload {
  kycId?: ApiId;
  customerId?: ApiId;
  kycType?: string;
  auditStatus?: string;
  auditBy?: ApiId;
  rejectReason?: string;
  remark?: string;
}

export interface CustomerAddress {
  addressId: ApiId;
  tenantId?: ApiId;
  customerId: ApiId;
  addressType: string;
  contactName?: string;
  phone?: string;
  country?: string;
  countryCode?: string;
  province?: string;
  city?: string;
  district?: string;
  street?: string;
  zipCode?: string;
  defaultAddress?: boolean;
  status?: number;
  createTime?: string;
  updateTime?: string;
}

export interface CustomerAddressPayload {
  customerId?: ApiId;
  addressId?: ApiId;
  addressType: string;
  contactName?: string;
  phone?: string;
  country?: string;
  countryCode?: string;
  province?: string;
  city?: string;
  district?: string;
  street?: string;
  zipCode?: string;
  defaultAddress?: boolean;
  status?: number;
}
