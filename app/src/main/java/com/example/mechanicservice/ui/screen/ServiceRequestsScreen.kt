package com.example.mechanicservice.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mechanicservice.MechanicServiceApplication
import com.example.mechanicservice.viewmodel.ServiceRequestsViewModel
import com.example.mechanicservice.viewmodel.ServiceRequestsViewModelFactory
import androidx.compose.ui.platform.LocalContext
import com.example.mechanicservice.data.model.ServiceRequestResponse
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ServiceRequestsScreen() {

    val context = LocalContext.current
    val application =
        context.applicationContext as MechanicServiceApplication

    val factory = ServiceRequestsViewModelFactory(
        application.sessionManager
    )

    val viewModel: ServiceRequestsViewModel =
        viewModel<ServiceRequestsViewModel>(
            factory = factory
        )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Service History",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Track your service requests",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.loadRequests()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color.Black
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        when {

            uiState.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error
                        )

                        Button(
                            onClick = {
                                viewModel.loadRequests()
                            }
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }

            uiState.requests.isEmpty() -> {

                EmptyServiceHistory()

            }

            else -> {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {

                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.primary
                                    ),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Spacer(
                                modifier = Modifier.size(14.dp)
                            )

                            Column {

                                Text(
                                    text = "${uiState.requests.size} Requests",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Your service activity",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 20.dp,
                            vertical = 4.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = uiState.requests,
                            key = { it.id }
                        ) { request ->

                            ServiceRequestCard(
                                request = request
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
private fun StatusBadge(
    status: String
) {

    val displayStatus = status.lowercase()
        .replaceFirstChar {
            it.uppercase()
        }

    Surface(
        shape = RoundedCornerShape(50),
        color = when (status.uppercase()) {
            "COMPLETED" ->
                MaterialTheme.colorScheme.primaryContainer

            "ACCEPTED" ->
                MaterialTheme.colorScheme.secondaryContainer

            "CANCELLED" ->
                MaterialTheme.colorScheme.errorContainer

            else ->
                MaterialTheme.colorScheme.tertiaryContainer
        }
    ) {

        Text(
            text = displayStatus,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun EmptyServiceHistory() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(34.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "No service history",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Your completed and ongoing service requests will appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDate(
    dateString: String
): String {

    return try {

        val inputFormat =
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault()
            )

        val outputFormat =
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

        inputFormat.parse(dateString)?.let {
            outputFormat.format(it)
        } ?: dateString

    } catch (e: Exception) {

        dateString
    }
}

@Composable
private fun ServiceRequestCard(
    request: ServiceRequestResponse
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {


                    Text(
                        text = request.mechanics?.garage_name ?: "Mechanic Garage",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )


                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "Request #${request.id.take(8)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                StatusBadge(
                    status = request.status
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            InfoRow(
                icon = Icons.Default.Build,
                text = request.services?.name ?: "Service"
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            InfoRow(
                icon = Icons.Default.DirectionsCar,
                text = request.vehicle_number
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            InfoRow(
                icon = Icons.Default.CalendarToday,
                text = formatDate(request.created_at)
            )

            if (!request.problem_description.isNullOrBlank()) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = request.problem_description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}