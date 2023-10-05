package com.okeicalm.simpleJournalEntry.handler.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.okeicalm.simpleJournalEntry.handler.type.AccountType
import com.okeicalm.simpleJournalEntry.usecase.account.CreateAccountUseCase
import com.okeicalm.simpleJournalEntry.usecase.account.CreateAccountUseCaseInput
import com.okeicalm.simpleJournalEntry.valueobject.AccountElementType
import org.springframework.stereotype.Component

data class CreateAccountInput(val code: String, val name: String, val elementType: AccountElementType)

@Component
class CreateAccountMutation(private val createAccountUseCase: CreateAccountUseCase) : Mutation {
    fun createAccount(input: CreateAccountInput): AccountType {
        val output = createAccountUseCase.call(
            CreateAccountUseCaseInput(
                code = input.code,
                name = input.name,
                elementType = input.elementType,
            )
        )
        return AccountType(output.account)
    }
}
