# Personal Finance Application Design

## 1. Overview
A household finance tracking application (user and spouse) designed for clarity, portfolio impact, and extensibility.

- **Goal**: Clear visualization of balances, liabilities, and savings.
- **Philosophy**: Clarity > Accounting Purity; focus on usable insights.
- **Stack**: Java/Spring Boot, React, PostgreSQL, gRPC, GitHub Actions, AWS EC2/RDS.

---

## 2. Core Design Principles
- **Single Shared Account**: Simplifies tracking for a two-person household.
- **Compute, Don't Edit**: Balances are calculated from movements to ensure data integrity.
- **Multi-Tenancy**: Household-scoped data for future expansion.
- **State-Based Movements**: Using statuses (PENDING/COMPLETED/POSTPONED) to manage future liabilities like BNPL.
- **Immutability With Admin Deletion**: Movements are immutable after creation, except admin can soft-delete mistakes.

---

## 3. Database Schema

### Household / User
- **Household**: `id (UUID)`, `name`, `createdAt`.
- **User**: `id (UUID)`, `email`, `passwordHash`, `role (ADMIN / MEMBER)`, `householdId (FK)`, `active (Boolean)`, `createdAt`.

### Movement (Ledger)
| Column | Type | Notes |
| :--- | :--- | :--- |
| id | UUID | Primary Key |
| householdId | UUID | FK → Household |
| amount | Decimal | Always positive |
| direction | ENUM | INCOME / EXPENSE / TRANSFER_TO_SAVINGS / TRANSFER_FROM_SAVINGS |
| status | ENUM | PENDING / COMPLETED / POSTPONED |
| date | Date | Movement due date |
| originalDate | Date | Optional, audit trail for prorogation |
| paymentMethodId | UUID | FK → PaymentMethod |
| installmentPlanId | UUID | Nullable FK → InstallmentPlan |
| tagId | UUID | Optional, single tag per movement |
| createdBy | UUID | FK → User |
| createdAt | DateTime | Auto |
| deletedAt | DateTime | Nullable, admin soft delete timestamp |
| deletedBy | UUID | Nullable, FK → User who deleted |

### PaymentMethod
- `id`, `householdId`, `name`, `type (CASH / DEBIT / CREDIT / BNPL)`, `active`, `createdAt`.
- BNPL includes Klarna, Clearpay, and similar providers.

### InstallmentPlan (BNPL)
- `id`, `householdId`, `provider` (KLARNA / CLEARPAY / OTHER), `totalAmount`, `numberOfInstallments`.
- `status`: ACTIVE / COMPLETED.

### Tag
- `id`, `householdId`, `name`, `color`, `active`, `createdAt`.

### SavingsAccount
- `id`, `householdId`, `name`, `targetAmount (optional)`, `active`, `createdAt`.
- Balance = sum of transfers in - sum of transfers out.

---

## 4. Advanced BNPL Logic
- **Pre-generation**: Creating a BNPL plan immediately generates all installments as `PENDING` movements.
- **Credit Card Integration**: If an installment is paid via a credit card, it is treated as an EXPENSE, reducing net balance but reflecting increased liability.
- **Prorogation Ripple Effect**: If a user shifts the `date` of a PENDING movement, subsequent PENDING installments of the same `installmentPlanId` are automatically adjusted to maintain schedule.

---

## 5. Dashboard & Analytics
### Metrics
- **Realized Net Balance**: Sum of completed income minus completed expenses.
- **Safe to Spend (Liquidity)**: Checking balance - credit card debt - total pending BNPL.
- **Liabilities**: View of all pending BNPL installments.

### Visuals
- **Pie Chart**: Expenses by Tag.
- **Recent Activity**: List of movements with status indicators (Green check = completed, Yellow clock = pending, etc.).

---

## 6. Technical & Architectural Points
- **Indexes**: Composite index `(householdId, date)`, plus `(householdId, status)`, `(householdId, direction)`, `(installmentPlanId)` for queries.
- **Soft Deletes**: `deletedAt` and `deletedBy` for movements, `active` flag for tags and payment methods.
- **Reconciliation Task**: Spring `@Scheduled` job alerts for overdue pending movements.
- **Immutability**: Movements are immutable after creation, except for admin deletion or status/date updates.
- **Modular Package Structure**: `core`, `bnpl`, `savings` for potential future gRPC microservices.
- **Compute-on-the-fly balances**: No stored balance columns; all aggregates calculated from movements.

---

## 7. Backend & Cloud Considerations
- **Backend Layers**: Controller → Service → Domain → Repository.
- **Auth**: JWT-based, scoped by household.
- **CI/CD**: GitHub Actions builds, tests, pushes Docker images, deploys to AWS EC2.
- **Cloud Deployment**: EC2 + RDS initially; can be migrated to ECS/Fargate later.
- **Audit & Accountability**: Deleted and prorogated movements are tracked with `deletedAt`, `deletedBy`, `originalDate`.

---

## 8. UX/Usability Principles
- Expense registration is under 10 seconds: amount, description, tag, payment method.
- BNPL purchases automatically create installments; users see full schedule.
- Dashboard is simple: top-level balances, visual expense breakdown, recent activity.
- Admin handles deletion of mistakes, standard users only see completed/pending statuses.

---
