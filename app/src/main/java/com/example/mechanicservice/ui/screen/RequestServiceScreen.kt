package com.example.mechanicservice.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mechanicservice.viewmodel.RequestServiceViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceScreen(
    mechanicId: String,
    onBackClick: () -> Unit,
    viewModel: RequestServiceViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    android.util.Log.d(
        "REQUEST_SCREEN_DEBUG",
        "services count = ${uiState.services.size}, services = ${uiState.services}"
    )

    var customerName by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var vehicleNumber by rememberSaveable { mutableStateOf("") }
    var problemDescription by rememberSaveable { mutableStateOf("") }

    var selectedServiceId by rememberSaveable { mutableStateOf("") }
    var selectedServiceName by rememberSaveable { mutableStateOf("") }
    var serviceMenuExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(mechanicId) {
        viewModel.loadServices(mechanicId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Request Service")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->

            if (uiState.isSubmitted) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Request Submitted!",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your service request has been successfully submitted. The mechanic will get back to you soon.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Mechanic")
                    }
                }

            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = "Customer Details",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = {
                            Text("Customer Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = {
                            Text("Phone Number")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = vehicleNumber,
                        onValueChange = { vehicleNumber = it },
                        label = {
                            Text("Vehicle Number")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text(
                        text = "Service",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (uiState.isLoadingServices) {

                        CircularProgressIndicator()

                    } else {

                        ExposedDropdownMenuBox(
                            expanded = serviceMenuExpanded,
                            onExpandedChange = {
                                serviceMenuExpanded = !serviceMenuExpanded
                            }
                        ) {

                            OutlinedTextField(
                                value = selectedServiceName,
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text("Select Service")
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = serviceMenuExpanded
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = serviceMenuExpanded,
                                onDismissRequest = {
                                    serviceMenuExpanded = false
                                }
                            ) {

                                uiState.services.forEach { service ->

                                    DropdownMenuItem(
                                        text = {
                                            Text(service.name)
                                        },
                                        onClick = {
                                            selectedServiceId = service.id
                                            selectedServiceName = service.name
                                            serviceMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = problemDescription,
                        onValueChange = {
                            problemDescription = it
                        },
                        label = {
                            Text("Problem Description")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        minLines = 5
                    )

                    if (uiState.errorMessage != null) {

                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.submitRequest(
                                mechanicId = mechanicId,
                                serviceId = selectedServiceId,
                                customerName = customerName,
                                phoneNumber = phoneNumber,
                                vehicleNumber = vehicleNumber,
                                problemDescription = problemDescription
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isSubmitting
                    ) {

                        if (uiState.isSubmitting) {

                            CircularProgressIndicator(
                                modifier = Modifier.height(20.dp)
                            )

                        } else {

                            Text("Request Service")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}