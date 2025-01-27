package com.abd.news.view

import NewsCard
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.wear.compose.material.Button
import com.abd.news.model.Article
import com.abd.news.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel,
    onArticleClick: (Article) -> Unit

) {
    val isLoading by viewModel.isLoading
    val articles = viewModel.articles
    var searchQuery by remember { mutableStateOf("") }
    val debouncePeriod = 300L

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }
    val dateRangePickerState = rememberDateRangePickerState()
    val startMillis = dateRangePickerState.selectedStartDateMillis
    val endMillis = dateRangePickerState.selectedEndDateMillis

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var showSortMenu by remember { mutableStateOf(false) }
    var selectedSortType by remember { mutableStateOf("popularity") }

    val context = LocalContext.current


    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            delay(debouncePeriod)
            viewModel.getQueries(searchQuery)
        } else if (searchQuery.isEmpty()) {
            viewModel.getNews()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {

                    IconButton(onClick = {
                        if (selectedStartDate != null && selectedEndDate != null) {

                            showSortMenu = true
                        } else {

                            Toast.makeText(context, "Please select a date range first.", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Info, contentDescription = "Sort Options")
                    }


                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Popularity") },
                            onClick = {
                                selectedSortType = "popularity"
                                showSortMenu = false

                                viewModel.getNewsByDateRange(
                                    searchQuery,
                                    convertMillisToDate(startMillis!!),
                                    convertMillisToDate(endMillis!!),
                                    selectedSortType,
                                    onError = { errorMessage = it; showErrorDialog = true }
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Published At") },
                            onClick = {
                                selectedSortType = "publishedAt"
                                showSortMenu = false

                                viewModel.getNewsByDateRange(
                                    searchQuery,
                                    convertMillisToDate(startMillis!!),
                                    convertMillisToDate(endMillis!!),
                                    selectedSortType,
                                    onError = { errorMessage = it; showErrorDialog = true }
                                )
                            }
                        )
                    }
                },
                title = {
                    TextField(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .border(1.dp, Color.Gray.copy(alpha = 0.3f)),
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                showDatePicker = true
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Calendar")
                            }
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,

                    )
                }

            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(articles) { article ->
                    NewsCard(
                        article = article,
                        viewModel = viewModel,
                        onFavoriteClick = {
                            viewModel.saveArticle(article)
                        },
                        onArticleClick = {

                            onArticleClick(article)

                        }
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        Popup(
            onDismissRequest = { showDatePicker = false },
            alignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .shadow(8.dp)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    DateRangePicker(
                        state = dateRangePickerState,
                        showModeToggle = true,
                        modifier = Modifier.fillMaxWidth()
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (startMillis != null && endMillis != null) {
                                    selectedStartDate = LocalDate.ofEpochDay(startMillis / (1000 * 60 * 60 * 24))
                                    selectedEndDate = LocalDate.ofEpochDay(endMillis / (1000 * 60 * 60 * 24))

                                    println(
                                        "Selected date range: ${convertMillisToDate(startMillis)} - " +
                                                convertMillisToDate(endMillis)
                                    )

                                    viewModel.getNewsByDateRange(
                                        searchQuery,
                                        convertMillisToDate(startMillis),
                                        convertMillisToDate(endMillis),
                                        selectedSortType,
                                        onError = { errorMessage = it; showErrorDialog = true }
                                    )
                                }
                                showDatePicker = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Apply")
                        }
                        Button(
                            onClick = { showDatePicker = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            },
            title = {
                Text("Error")
            },
            text = {
                Text(errorMessage)
            }
        )
    }


}



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}
