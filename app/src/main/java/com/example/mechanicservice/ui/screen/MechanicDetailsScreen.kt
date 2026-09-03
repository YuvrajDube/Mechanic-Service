package com.example.mechanicservice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mechanicservice.viewmodel.MechanicDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicDetailsScreen(
    mechanicId: String,
    onBackClick: () -> Unit,
    onRequestServiceClick: () -> Unit,
    viewModel: MechanicDetailsViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(mechanicId) {
        viewModel.loadMechanic(mechanicId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            TopAppBar(
                title = {
                    Text("Detail Screen")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.tertiaryContainer
                )
            )


            when {


                uiState.isLoading -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }


                uiState.errorMessage != null -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.Center
                    ) {

                        Text(
                            text = uiState.errorMessage
                                ?: "Something went wrong",
                            color =
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Button(
                            onClick = onBackClick
                        ) {
                            Text("Go Back")
                        }
                    }
                }


                uiState.mechanic != null -> {

                    val mechanic = uiState.mechanic!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {


                        AsyncImage(
                            model = mechanic.cover_image_url,
                            contentDescription =
                                mechanic.garage_name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {


                            Text(
                                text = mechanic.garage_name,
                                style =
                                    MaterialTheme.typography
                                        .headlineMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "⭐ ${mechanic.rating}  •  " +
                                            "${mechanic.distance_km} km",
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )


                            Text(
                                text = mechanic.location,
                                style =
                                    MaterialTheme.typography
                                        .titleMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )


                            Text(
                                text = mechanic.address,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant
                            )

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Text(
                                text = "Services",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            if (uiState.services.isEmpty()) {

                                Text(
                                    text = "No services listed",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                            } else {

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    uiState.services.forEach { service ->

                                        Surface(
                                            shape = MaterialTheme.shapes.medium,
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        ) {
                                            Text(
                                                text = service.name,
                                                modifier = Modifier.padding(
                                                    horizontal = 14.dp,
                                                    vertical = 10.dp
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Text(
                                text = "Working Hours",
                                style =
                                    MaterialTheme.typography
                                        .titleMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = mechanic.working_hours,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )


                            Text(
                                text = "Contact",
                                style =
                                    MaterialTheme.typography
                                        .titleMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = mechanic.phone_number,
                                color =
                                    MaterialTheme.colorScheme
                                        .onBackground
                            )

                            Spacer(
                                modifier = Modifier.height(28.dp)
                            )


                            Button(
                                onClick = onRequestServiceClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Request Service")
                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}