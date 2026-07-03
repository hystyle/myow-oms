import { request } from '@myow/shared';
import type {
  ApiId,
  CustomerAddress,
  CustomerAddressPayload,
  CustomerAttachment,
  CustomerAttachmentPayload,
  CustomerBlacklist,
  CustomerBlacklistPayload,
  CustomerBlacklistQuery,
  CustomerContact,
  CustomerContactPayload,
  CustomerCreatePayload,
  CustomerKyc,
  CustomerKycPayload,
  CustomerOption,
  CustomerPageQuery,
  CustomerProfile,
  CustomerRelation,
  CustomerRelationPayload,
  CustomerRole,
  CustomerRoleOptionQuery,
  CustomerRolePayload,
  CustomerRoleValidatePayload,
  CustomerStatus,
  CustomerUpdatePayload,
  PageResult
} from '@myow/api';

const CUSTOMER_BASE = '/myow/api/v1/customer/customers';
const CONTACT_BASE = '/myow/api/v1/customer/contacts';
const ADDRESS_BASE = '/myow/api/v1/customer/addresses';
const ROLE_BASE = '/myow/api/v1/customer/roles';
const BLACKLIST_BASE = '/myow/api/v1/customer/blacklists';
const RELATION_BASE = '/myow/api/v1/customer/relations';
const ATTACHMENT_BASE = '/myow/api/v1/customer/attachments';
const KYC_BASE = '/myow/api/v1/customer/kycs';

export function pageCustomers(query: CustomerPageQuery) {
  return request.post<PageResult<CustomerProfile>>(`${CUSTOMER_BASE}/page`, query);
}

export function getCustomer(customerId: ApiId) {
  return request.post<CustomerProfile>(`${CUSTOMER_BASE}/detail`, { id: customerId });
}

export function createCustomer(data: CustomerCreatePayload) {
  return request.post<CustomerProfile>(`${CUSTOMER_BASE}/create`, data);
}

export function updateCustomer(data: CustomerUpdatePayload) {
  return request.post<CustomerProfile>(`${CUSTOMER_BASE}/update`, data);
}

export function deleteCustomer(customerId: ApiId) {
  return request.post<boolean>(`${CUSTOMER_BASE}/delete`, { id: customerId });
}

export function changeCustomerStatus(customerId: ApiId, status: CustomerStatus) {
  return request.post<boolean>(`${CUSTOMER_BASE}/change-status`, { customerId, status });
}

export function pageCustomerRoles(customerId: ApiId) {
  return request.post<PageResult<CustomerRole>>(`${ROLE_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function listCustomerOptionsByRole(query: CustomerRoleOptionQuery) {
  return request.post<CustomerOption[]>(`${ROLE_BASE}/options-by-role`, query);
}

export function validateCustomerRole(data: CustomerRoleValidatePayload) {
  return request.post<boolean>(`${ROLE_BASE}/validate-role`, data);
}

export function createCustomerRole(customerId: ApiId, data: CustomerRolePayload) {
  return request.post<CustomerRole>(`${ROLE_BASE}/create`, { ...data, customerId });
}

export function updateCustomerRole(data: CustomerRolePayload) {
  return request.post<CustomerRole>(`${ROLE_BASE}/update`, data);
}

export function deleteCustomerRole(customerRoleId: ApiId) {
  return request.post<boolean>(`${ROLE_BASE}/delete`, { id: customerRoleId });
}

export function pageCustomerRelations(customerId: ApiId) {
  return request.post<PageResult<CustomerRelation>>(`${RELATION_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function createCustomerRelation(data: CustomerRelationPayload) {
  return request.post<CustomerRelation>(`${RELATION_BASE}/create`, data);
}

export function updateCustomerRelation(data: CustomerRelationPayload) {
  return request.post<CustomerRelation>(`${RELATION_BASE}/update`, data);
}

export function deleteCustomerRelation(relationId: ApiId) {
  return request.post<boolean>(`${RELATION_BASE}/delete`, { id: relationId });
}

export function pageCustomerAttachments(customerId: ApiId) {
  return request.post<PageResult<CustomerAttachment>>(`${ATTACHMENT_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function createCustomerAttachment(customerId: ApiId, data: CustomerAttachmentPayload) {
  return request.post<CustomerAttachment>(`${ATTACHMENT_BASE}/create`, { ...data, customerId });
}

export function updateCustomerAttachment(data: CustomerAttachmentPayload) {
  return request.post<CustomerAttachment>(`${ATTACHMENT_BASE}/update`, data);
}

export function deleteCustomerAttachment(attachmentId: ApiId) {
  return request.post<boolean>(`${ATTACHMENT_BASE}/delete`, { id: attachmentId });
}

export function pageCustomerKycs(customerId: ApiId) {
  return request.post<PageResult<CustomerKyc>>(`${KYC_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function createCustomerKyc(customerId: ApiId, data: CustomerKycPayload) {
  return request.post<CustomerKyc>(`${KYC_BASE}/create`, { ...data, customerId });
}

export function updateCustomerKyc(data: CustomerKycPayload) {
  return request.post<CustomerKyc>(`${KYC_BASE}/update`, data);
}

export function auditCustomerKyc(data: CustomerKycPayload) {
  return request.post<CustomerKyc>(`${KYC_BASE}/audit`, data);
}

export function deleteCustomerKyc(kycId: ApiId) {
  return request.post<boolean>(`${KYC_BASE}/delete`, { id: kycId });
}

export function pageCustomerContacts(customerId: ApiId) {
  return request.post<PageResult<CustomerContact>>(`${CONTACT_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function createCustomerContact(customerId: ApiId, data: CustomerContactPayload) {
  return request.post<CustomerContact>(`${CONTACT_BASE}/create`, { ...data, customerId });
}

export function updateCustomerContact(data: CustomerContactPayload) {
  return request.post<CustomerContact>(`${CONTACT_BASE}/update`, data);
}

export function deleteCustomerContact(contactId: ApiId) {
  return request.post<boolean>(`${CONTACT_BASE}/delete`, { id: contactId });
}

export function pageCustomerAddresses(customerId: ApiId) {
  return request.post<PageResult<CustomerAddress>>(`${ADDRESS_BASE}/page`, { customerId, pageNum: 1, pageSize: 200 });
}

export function createCustomerAddress(customerId: ApiId, data: CustomerAddressPayload) {
  return request.post<CustomerAddress>(`${ADDRESS_BASE}/create`, { ...data, customerId });
}

export function updateCustomerAddress(data: CustomerAddressPayload) {
  return request.post<CustomerAddress>(`${ADDRESS_BASE}/update`, data);
}

export function deleteCustomerAddress(addressId: ApiId) {
  return request.post<boolean>(`${ADDRESS_BASE}/delete`, { id: addressId });
}

export function pageCustomerBlacklists(query: CustomerBlacklistQuery) {
  return request.post<PageResult<CustomerBlacklist>>(`${BLACKLIST_BASE}/page`, query);
}

export function createCustomerBlacklist(data: CustomerBlacklistPayload) {
  return request.post<CustomerBlacklist>(`${BLACKLIST_BASE}/create`, data);
}

export function updateCustomerBlacklist(data: CustomerBlacklistPayload) {
  return request.post<CustomerBlacklist>(`${BLACKLIST_BASE}/update`, data);
}

export function deleteCustomerBlacklist(blacklistId: ApiId) {
  return request.post<boolean>(`${BLACKLIST_BASE}/delete`, { id: blacklistId });
}
