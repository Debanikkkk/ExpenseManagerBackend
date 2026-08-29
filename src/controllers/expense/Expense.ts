/**
 * DTO for creating/updating an expense
 */
export interface ExpenseDTO {
  title: string;
  amount: number;
  date: string; // ISO date string
}

/**
 * Service interface for Expense operations
 */
export interface IExpenseService {
  create(data: ExpenseDTO): Promise<Expense>;
  getById(id: number): Promise<Expense | null>;
  update(id: number, data: ExpenseDTO): Promise<Expense | null>;
  delete(id: number): Promise<boolean>;
  getAll(): Promise<Expense[]>;
}

/**
 * DTO for response in create/update operations
 */
export interface ExpenseCreateUpdateResponse {
  id: number;
  title: string;
  amount: number;
  date: string;
}