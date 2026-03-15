package com.example.finamobileapp.view_model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamobileapp.model.database.TransactionDatabase
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.MonthlyGoal
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.FormMode
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.model.repository.BuyIdeasRepository
import com.example.finamobileapp.model.repository.MonthlyGoalRepository
import com.example.finamobileapp.model.repository.TransactionRepository
import com.example.finamobileapp.view_model.interfaces.BuyIdeaActions
import com.example.finamobileapp.view_model.uiState.BuyIdeaUiState
import com.example.finamobileapp.view_model.uiState.DashboardUiState
import com.example.finamobileapp.view_model.uiState.GoalUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionRepository: TransactionRepository
    private val buyIdeaRepository: BuyIdeasRepository
    private val monthlyGoalRepository: MonthlyGoalRepository

    init {
        val db = TransactionDatabase.getDatabase(application)
        transactionRepository = TransactionRepository(db.transactionDao())
        buyIdeaRepository = BuyIdeasRepository(db.buyIdeaDao())
        monthlyGoalRepository = MonthlyGoalRepository(db.goalDao())


    }

    private val today = LocalDate.now()


    private val _buyIdeaUiState = MutableStateFlow(BuyIdeaUiState())
    private val _goalUiState = MutableStateFlow(GoalUiState())


    val buyIdeaUiState: StateFlow<BuyIdeaUiState> = _buyIdeaUiState.asStateFlow()
    val goalUiState: StateFlow<GoalUiState> = _goalUiState.asStateFlow()

    val uiState: StateFlow<DashboardUiState> = combine(
        combine(
            transactionRepository.getBalance(today, TransactionAccountType.REGULAR),
            transactionRepository.getBalance(today, TransactionAccountType.SAVINGS)

        ) { reg, sav -> reg to sav },
        transactionRepository.getSumyByType(today),
        buyIdeaRepository.allBuyIdeas,
        monthlyGoalRepository.getGoalForMonth(today.year, today.monthValue),
        transactionRepository.getSumForCategory(today, TransactionCategory.INVESTMENT)
    ) { balances, typeSums, ideas, goal, invested ->

        DashboardUiState(
            balanceRegular = balances.first,
            balanceSavings = balances.second,
            typeSums = typeSums,
            buyIdeas = ideas,
            currentGoal = goal,
            currentlyInvested = invested,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )


    fun getSumyByCategories(type: TransactionType): Flow<Map<TransactionCategory, Int>> =
        transactionRepository.getSumyByCategories(today, type)


    fun addTransaction(buyIdea: BuyIdeas) {

        val selectedAccount = buyIdeaUiState.value.selectedOptionAccType[buyIdea.id]
        val accountTypeEnum =
            TransactionAccountType.entries.find {
                it.name == selectedAccount?.name
            }
        if (accountTypeEnum != null) {

            val transaction = Transaction(
                name = buyIdea.name,
                amount = buyIdea.price,
                category = buyIdea.category,
                type = buyIdea.type,
                accountType = accountTypeEnum,
                date = LocalDate.now(),
                description = buyIdea.description,
                groupId = null
            )
            viewModelScope.launch(Dispatchers.IO) {
                transactionRepository.addSmartTransaction(transaction)
            }
            deleteBuyIdea(buyIdea)

        }

    }


    fun onBuyIdeaSubmit() {
        val state = buyIdeaUiState.value
        val category = state.formCategory ?: return

        val ideaToSave = BuyIdeas(

            id = state.selectedIdea?.id ?: 0,
            name = state.formName,
            price = state.formPrice.toIntOrNull() ?: 0,
            category = category,
            type = TransactionType.EXPENSE,
            description = state.formDescription
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (state.mode == FormMode.CREATE) {
                buyIdeaRepository.addBuyIdea(ideaToSave)
            } else {
                buyIdeaRepository.updateBuyIdea(ideaToSave)
            }


        }
    }

    fun deleteBuyIdea(buyIdea: BuyIdeas) {
        viewModelScope.launch(Dispatchers.IO) {
            buyIdeaRepository.deleteBuyIdea(buyIdea)
        }
    }


    fun setGoal(goal: MonthlyGoal) {
        viewModelScope.launch(Dispatchers.IO) {
            monthlyGoalRepository.setGoal(goal)
        }
    }

    fun setBuyIdeaSheet(isOpen: Boolean) {
        _buyIdeaUiState.update { it.copy(isOpen = isOpen) }
    }

    fun prepareUpdate(idea: BuyIdeas) {
        _buyIdeaUiState.update {
            it.copy(
                selectedIdea = idea,
                mode = FormMode.UPDATE,
                isOpen = true,
                formName = idea.name,
                formPrice = idea.price.toString(),
                formCategory = idea.category,
                formDescription = idea.description
            )
        }
    }

    fun prepareCreate() {
        _buyIdeaUiState.update {
            it.copy(
                selectedIdea = null,
                mode = FormMode.CREATE,
                isOpen = true,
                formName = "",
                formPrice = "",
                formDescription = "",
                formCategory = null
            )
        }
    }

    fun onBuyIdeaAction(action: BuyIdeaActions) {
        when (action) {
            is BuyIdeaActions.AddTransaction -> addTransaction(action.buyIdea)
            is BuyIdeaActions.DeleteBuyIdea -> deleteBuyIdea(action.buyIdea)
            is BuyIdeaActions.SelectAccTypeOption -> selectAccTypeOption(
                action.id,
                action.accountType
            )

            is BuyIdeaActions.ToggleDeleteForm -> toggleDeleteForm(action.id)
            is BuyIdeaActions.ToggleExpandAccountType -> toggleExpandAccountType(action.id)
            is BuyIdeaActions.ToggleIsChecked -> toggleIsChecked(action.id)
            is BuyIdeaActions.ToggleOption -> toggleOption(action.id)
            is BuyIdeaActions.PrepareCreate -> prepareCreate()
            is BuyIdeaActions.PrepareUpdate -> prepareUpdate(action.buyIdea)
            is BuyIdeaActions.SetBuyIdeaSheet -> setBuyIdeaSheet(action.isOpen)
            is BuyIdeaActions.SetFormCategory -> setFormCategory(action.category)
            is BuyIdeaActions.SetFormDescription -> setFormDescription(action.description)
            is BuyIdeaActions.SetFormName -> setFormName(action.name)
            is BuyIdeaActions.SetFormPrice -> setFormPrice(action.price)
            is BuyIdeaActions.SetFormExpandedCategory -> setFormExpandedCategory(action.isExpanded)
            is BuyIdeaActions.OnBuyIdeaSubmit -> onBuyIdeaSubmit()
        }
    }


    fun toggleIsChecked(id: Long) {
        _buyIdeaUiState.update { state ->
            val newChecked = if (state.isChecked.contains(id)) {
                state.isChecked - id
            } else {
                state.isChecked + id
            }
            state.copy(isChecked = newChecked)
        }
    }

    fun toggleExpandAccountType(id: Long) {
        _buyIdeaUiState.update { state ->
            val newExpanded = if (state.expandedAccountType.contains(id)) {
                state.expandedAccountType - id
            } else {
                state.expandedAccountType + id
            }
            state.copy(expandedAccountType = newExpanded)
        }
    }

    fun selectAccTypeOption(id: Long, accountType: TransactionAccountType) {
        _buyIdeaUiState.update { state ->
            state.copy(selectedOptionAccType = state.selectedOptionAccType + (id to accountType))
        }
    }

    fun toggleOption(id: Long) {
        _buyIdeaUiState.update { state ->
            state.copy(isOptionOpen = if (state.isOptionOpen == id) null else id)
        }
    }

    fun toggleDeleteForm(id: Long?) {
        _buyIdeaUiState.update {
            it.copy(isDeleteFormOpen = id)
        }
    }


    fun setFormName(name: String) {
        _buyIdeaUiState.update { it.copy(formName = name) }
    }

    fun setFormPrice(price: String) {
        _buyIdeaUiState.update { it.copy(formPrice = price) }
    }

    fun setFormDescription(description: String) {
        _buyIdeaUiState.update { it.copy(formDescription = description) }
    }

    fun setFormCategory(category: TransactionCategory) {
        _buyIdeaUiState.update { it.copy(formCategory = category) }
    }

    fun setFormExpandedCategory(isExpanded: Boolean) {
        _buyIdeaUiState.update { it.copy(formExpandedCategory = isExpanded) }
    }

    fun toggleEditGoalOpen() {
        _goalUiState.update { it.copy(isEditGoalOpen = !it.isEditGoalOpen) }
    }

    fun setInvestmentGoal(investmentGoal: String) {
        _goalUiState.update { it.copy(investmentGoal = investmentGoal) }
    }

    fun setSavingGoal(savingsGoal: String) {
        _goalUiState.update { it.copy(savingsGoal = savingsGoal) }
    }


}