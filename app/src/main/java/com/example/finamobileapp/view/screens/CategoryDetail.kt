package com.example.finamobileapp.view.screens

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.view.components.ShowOptions
import com.example.finamobileapp.view.components.TransactionBox
import com.example.finamobileapp.view.forms.DeleteForm
import com.example.finamobileapp.view.forms.UpdateForm
import com.example.finamobileapp.view_model.CategoryDetailViewModel
import com.example.finamobileapp.view_model.CategoryDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetail(categoryName: String) {
    val viewModel: CategoryDetailViewModel = viewModel(
        factory = CategoryDetailViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            categoryName = categoryName
        )
    )


    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFA69D9D)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = categoryName, modifier = Modifier.padding(30.dp), fontSize = 40.sp)

                LazyColumn {
                    items(state.transactions) { item ->
                        TransactionBox(
                            transaction = item,
                            isExpanded = state.expandedTransactionId == item.id,
                            onExpandClick = { viewModel.toggleExpand(item.id) },
                            onLongClick = { viewModel.openOptions(item) }
                        )
                    }
                }
            }



            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (state.isOptionsOpen && state.selectedTransaction != null) {
                ShowOptions(
                    transaction = state.selectedTransaction!!,
                    closeOptions = { viewModel.closeAllModals() },
                    onDelete = { viewModel.openDeleteForm(false) },
                    onUpdate = { viewModel.openUpdateForm(false) },
                    onRecurringDelete = { viewModel.openDeleteForm(true) },
                    onRecurringUpdate = { viewModel.openUpdateForm(true) },

                )
            }

            if (state.isDeleteFormOpen && state.selectedTransaction != null) {
                DeleteForm(
                    onDismiss = { viewModel.closeAllModals() },
                    itemName = state.selectedTransaction!!.name,
                    onDelete = {
                        if (state.isRecurringAction) {
                            viewModel.deleteRecurring(state.selectedTransaction!!.groupId!!)
                        } else {
                            viewModel.deleteTransaction(state.selectedTransaction!!)
                        }
                    },
                    closeOptions = { viewModel.closeAllModals() }
                )
            }
            if (state.isUpdateFormOpen && state.selectedTransaction != null) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.closeAllModals() },
                    containerColor = Color(0xFFF0F0F0), // Barva pozadí sheetu
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {

                    UpdateForm(
                        transaction = state.selectedTransaction!!,
                        onDismiss = { viewModel.closeAllModals() },
                        onUpdate = { updatedItem ->
                            if (state.isRecurringAction) {

                                viewModel.updateRecurring(
                                    groupId = updatedItem.groupId!!,
                                    name = updatedItem.name,
                                    amount = updatedItem.amount,
                                    category = updatedItem.category,
                                    type = updatedItem.type,
                                    description = updatedItem.description
                                )
                            } else {

                                viewModel.updateTransaction(updatedItem)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}