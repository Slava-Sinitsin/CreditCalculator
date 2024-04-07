package com.example.creditcalc.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.creditcalc.R
import com.example.creditcalc.ui.components.modifier.clearFocusOnTap
import com.example.creditcalc.ui.components.tabsindicator.DisabledInteractionSource
import com.example.creditcalc.ui.components.tabsindicator.TabsIndicator
import com.example.creditcalc.ui.components.tabsindicator.tabsIndicatorOffset
import com.example.creditcalc.ui.viewmodels.MainScreenViewModel
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clearFocusOnTap(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MainScreenTopAppBar(
                scrollBehavior = scrollBehavior
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = scaffoldPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            SumField(
                sum = viewModel.sum,
                updateSum = { newSum -> viewModel.updateSum(newSum) }
            )
            Spacer(modifier = Modifier.size(10.dp))
            RateField(
                rate = viewModel.rate,
                updateRate = { newRate -> viewModel.updateRate(newRate) }
            )
            Spacer(modifier = Modifier.size(10.dp))
            PeriodField(
                period = viewModel.period,
                updatePeriod = { newPeriod -> viewModel.updatePeriod(newPeriod) },
                selectedPeriodTypeIndex = viewModel.selectedPeriodTypeIndex,
                periodTypeList = viewModel.periodTypeList,
                updatePeriodTypeIndex = { newPeriodTypeIndex ->
                    viewModel.updatePeriodTypeIndex(newPeriodTypeIndex)
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            IssueDateField(issueDate = viewModel.issueDate,
                updateIssueDate = { newDate ->
                    viewModel.updateDate(newDate)
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            RepaymentTypeField(
                repaymentTypeList = viewModel.repaymentTypeList,
                selectedRepaymentTypeIndex = viewModel.selectedRepaymentTypeIndex,
                updateRepaymentTypeIndex = { newRepaymentTypeIndex ->
                    viewModel.updateRepaymentTypeIndex(
                        newRepaymentTypeIndex
                    )
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            RepaymentFrequencyField(
                repaymentFrequencyList = viewModel.repaymentFrequencyList,
                selectedRepaymentFrequencyIndex = viewModel.selectedRepaymentFrequencyIndex,
                updateRepaymentFrequencyIndex = { newRepaymentFrequencyIndex ->
                    viewModel.updateRepaymentFrequencyIndex(
                        newRepaymentFrequencyIndex
                    )
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            CalculateButton(onCalculateButtonClick = { viewModel.onCalculateButtonClick() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(
                text = "Credit calculator",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        actions = { },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surface)
    )
}

@Composable
private fun SumField(
    sum: Pair<String, Boolean>,
    updateSum: (newCreditSum: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = sum.first,
        onValueChange = { updateSum(it) },
        label = { Text(text = "Sum") },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = !sum.second,
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ruble),
                contentDescription = "RubleIcon"
            )
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun RateField(
    rate: Pair<String, Boolean>,
    updateRate: (newCreditSum: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = rate.first,
        onValueChange = { updateRate(it) },
        label = { Text(text = "Rate") },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = !rate.second,
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.percent),
                contentDescription = "PercentIcon"
            )
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@Composable
private fun PeriodField(
    period: Pair<String, Boolean>,
    updatePeriod: (newCreditSum: String) -> Unit,
    selectedPeriodTypeIndex: Int,
    periodTypeList: List<String>,
    updatePeriodTypeIndex: (index: Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val localDensity = LocalDensity.current
    var textFieldHeight by remember { mutableStateOf(0.dp) }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabsIndicator(
            Modifier
                .tabsIndicatorOffset(tabPositions[selectedPeriodTypeIndex])
                .fillMaxSize()
                .padding(5.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                )
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    with(localDensity) {
                        textFieldHeight = it.size.height.toDp()
                    }
                },
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = period.first,
            onValueChange = { updatePeriod(it) },
            label = { Text(text = "Period") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                errorContainerColor = MaterialTheme.colorScheme.background
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !period.second,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        TabRow(
            modifier = Modifier
                .height(textFieldHeight - 8.dp)
                .weight(1f)
                .padding(start = 10.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ),
            selectedTabIndex = selectedPeriodTypeIndex,
            indicator = indicator,
            divider = {}
        ) {
            periodTypeList.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier
                        .height(textFieldHeight - 8.dp)
                        .zIndex(2f),
                    selected = index == selectedPeriodTypeIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { updatePeriodTypeIndex(index) },
                    text = { Text(text = item) }
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IssueDateField(
    issueDate: Date,
    updateIssueDate: (newIssueDate: Date) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showDialogState by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = SimpleDateFormat("dd.MM.yyyy").format(issueDate).toString(),
        onValueChange = { },
        label = { Text(text = "Issue date") },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            errorContainerColor = MaterialTheme.colorScheme.background
        ),
        singleLine = true,
        readOnly = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "IssueDateIcon"
            )
        },
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showDialogState = true
                        }
                    }
                }
            }
    )
    if (showDialogState) {
        DatePickerDialog(
            onDismissRequest = { showDialogState = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { updateIssueDate(Date(it)) }
                    showDialogState = false
                    Log.e("datePickerState", issueDate.toString())
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogState = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun RepaymentTypeField(
    repaymentTypeList: List<String>,
    selectedRepaymentTypeIndex: Int,
    updateRepaymentTypeIndex: (newRepaymentTypeIndex: Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabsIndicator(
            Modifier
                .tabsIndicatorOffset(tabPositions[selectedRepaymentTypeIndex])
                .fillMaxSize()
                .padding(5.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                )
        )
    }
    Column {
        Text(text = "Repayment type")
        TabRow(
            modifier = Modifier
                .padding(top = 5.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ),
            selectedTabIndex = selectedRepaymentTypeIndex,
            indicator = indicator,
            divider = {}
        ) {
            repaymentTypeList.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = index == selectedRepaymentTypeIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { updateRepaymentTypeIndex(index) },
                    text = {
                        Text(
                            modifier = Modifier.padding(8.5.dp),
                            text = item
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun RepaymentFrequencyField(
    repaymentFrequencyList: List<Pair<String, Boolean>>,
    selectedRepaymentFrequencyIndex: Int,
    updateRepaymentFrequencyIndex: (newRepaymentFrequency: Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabsIndicator(
            Modifier
                .tabsIndicatorOffset(tabPositions[selectedRepaymentFrequencyIndex])
                .fillMaxSize()
                .padding(5.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                )
        )
    }
    Column {
        Text(text = "Repayment frequency")
        TabRow(
            modifier = Modifier
                .padding(top = 5.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ),
            selectedTabIndex = selectedRepaymentFrequencyIndex,
            indicator = indicator,
            divider = {}
        ) {
            repaymentFrequencyList.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = index == selectedRepaymentFrequencyIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { updateRepaymentFrequencyIndex(index) },
                    text = {
                        Text(
                            modifier = Modifier
                                .padding(8.5.dp)
                                .alpha(if (item.second) 1f else 0.6f),
                            text = item.first
                        )
                    },
                    enabled = item.second
                )
            }
        }
    }
}

@Composable
private fun CalculateButton(
    onCalculateButtonClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onCalculateButtonClick() }
    ) {
        Text(text = "Calculate")
    }
}