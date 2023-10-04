package com.okeicalm.simpleJournalEntry.repository

import com.okeicalm.simpleJournalEntry.entity.Account
import com.okeicalm.simpleJournalEntry.infra.db.enums.AccountsElementType
import com.okeicalm.simpleJournalEntry.infra.db.tables.Accounts
import com.okeicalm.simpleJournalEntry.infra.db.tables.references.ACCOUNTS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

interface AccountRepository {
    fun findAll(): List<Account>
    fun findById(id: Long): Account?
    fun filterByIds(ids: List<Long>): List<Account>
    fun create(account: Account): Account
    fun update(account: Account): Account
    fun delete(id: Long): Long
}

@Repository
        class AccountRepositoryImpl(private val dslContext: DSLContext) : AccountRepository {
            override fun findAll(): List<Account> {
                return dslContext.select()
                    .from(Accounts.ACCOUNTS)
                    .fetch()
                    .into(Account::class.java)
            }

            override fun findById(id: Long): Account? {
                return dslContext
                    .fetchOne(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ID.eq(id))
            ?.into(Account::class.java)
    }

    override fun filterByIds(ids: List<Long>): List<Account> {
        return dslContext
            .select()
            .from(ACCOUNTS)
            .where(ACCOUNTS.ID.`in`(ids))
            .fetchInto(Account::class.java)
    }

    override fun create(account: Account): Account {
        val record = dslContext
            .newRecord(Accounts.ACCOUNTS)
            .apply {
                name = account.name
                code = account.code
                elementType = AccountsElementType.valueOf(account.elementType.toString())
            }
        record.store()

        return account.copy(id = record.id!!)
    }

    override fun update(account: Account): Account {
        dslContext
            .update(Accounts.ACCOUNTS)
            .set(Accounts.ACCOUNTS.CODE, account.code)
            .set(Accounts.ACCOUNTS.NAME, account.name)
            .set(Accounts.ACCOUNTS.ELEMENT_TYPE, AccountsElementType.valueOf(account.elementType.toString()))
            .where(Accounts.ACCOUNTS.ID.eq(account.id))
            .execute()
        return account
    }

    override fun delete(id: Long): Long {
        dslContext
            .delete(Accounts.ACCOUNTS)
            .where(Accounts.ACCOUNTS.ID.eq(id))
            .execute()
        return id
    }
}
