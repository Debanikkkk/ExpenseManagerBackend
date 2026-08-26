package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.entity.expense.Expense;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void createExpenseRequestIgnoresClientSuppliedId() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Expense expense = objectMapper.readValue(
				"{\"id\":999,\"title\":\"Groceries\",\"amount\":45.5,\"date\":\"2026-08-25\"}",
				Expense.class
		);

		assertNull(expense.getId());
	}

}
