import { ExpenseDTO, IExpenseService } from './Expense';

/**
 * Controller for Expense CRUD operations
 */
export class ExpenseController {
  private expenseService: IExpenseService;

  constructor(expenseService: IExpenseService) {
    this.expenseService = expenseService;
  }

  /**
   * Create a new expense
   */
  public async create(dto: ExpenseDTO): Promise<ExpenseCreateUpdateResponse> {
    const created = await this.expenseService.create(dto);
    return {
      id: created.id,
      title: created.title,
      amount: created.amount,
      date: created.date.toISOString(),
    };
  }

  /**
   * Get an expense by ID
   */
  public async getById(id: number): Promise<ExpenseCreateUpdateResponse | null> {
    const expense = await this.expenseService.getById(id);
    if (!expense) return null;
    return {
      id: expense.id,
      title: expense.title,
      amount: expense.amount,
      date: expense.date.toISOString(),
    };
  }

  /**
   * Update an existing expense
   */
  public async update(id: number, dto: ExpenseDTO): Promise<ExpenseCreateUpdateResponse | null> {
    const updated = await this.expenseService.update(id, dto);
    if (!updated) return null;
    return {
      id: updated.id,
      title: updated.title,
      amount: updated.amount,
      date: updated.date.toISOString(),
    };
  }

  /**
   * Delete an expense by ID
   */
  public async delete(id: number): Promise<boolean> {
    return await this.expenseService.delete(id);
  }

  /**
   * Get all expenses
   */
  public async getAll(): Promise<ExpenseCreateUpdateResponse[]> {
    const expenses = await this.expenseService.getAll();
    return expenses.map(expense => ({
      id: expense.id,
      title: expense.title,
      amount: expense.amount,
      date: expense.date.toISOString(),
    }));
  }
}