package org.hse.smartcalendar.ui.screens

import InviteItem
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.view.model.InvitesViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.store.InvitesStore
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import java.util.UUID

@Composable
fun InvitesScreen(
    navigation: Navigation,
    openMenu: (()-> Unit)? = null,
    viewModel: InvitesViewModel,
    listViewModel: ListViewModel
) {
    LaunchedEffect(Unit) {
        InvitesStore.markInvitesSeen()
    }
    val invites = viewModel.invites
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopButton(
                text = "Invitations",
                navigation = navigation,
                openMenu = openMenu
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        if (invites.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No shared‑task invitations")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(invites, key = { it.id }) { invite ->
                    InviteItem(
                        invite = invite,
                        onAccept = {
                            val notNested = viewModel.tryAdd(invite);
                            if (notNested) {
                                listViewModel.loadDailyTasks()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Invitation accepted")
                                }
                            } else {
                                val conflict = viewModel.lastConflict!!
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Time conflict with existing task",
                                        actionLabel = "Go",
                                        duration = SnackbarDuration.Indefinite
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        listViewModel.changeDailyTaskSchedule(conflict.getTaskDate())
                                        navigation.navigateTo(Screens.CALENDAR.route)
                                    }
                                }
                            }
                            notNested
                        },
                        onDecline = {
                            viewModel.decline(invite)
                            scope.launch {
                                snackbarHostState.showSnackbar("Invitation declined")
                            }
                        }
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InvitesScreenPreview() {
    SmartCalendarTheme {
        val firstInvite = Invite(
            id = UUID.randomUUID(),
            inviterName = "Alexander Khrabrov",
            task = DailyTask.example(
                title = "Analysis exam",
                type = DailyTaskType.STUDIES,
                description = "The math exam will take place on June 25 at Kantemirovskaya",
                start = LocalTime(10, 0),
                end = LocalTime(11, 0)
            )
        )
        val secondInvite = Invite(
            id = UUID.randomUUID(),
            inviterName = "ITMO",
            task = DailyTask.example(
                title = "Transfer Test",
                type = DailyTaskType.STUDIES,
                description = "The certification test will be held on August 21 at 49 Kronverksky Prospekt in room 2137.",
                start = LocalTime(10, 0),
                end = LocalTime(11, 0)
            )
        )

        val invitesViewModel: InvitesViewModel = viewModel()
        invitesViewModel.addInvite(firstInvite)
        invitesViewModel.addInvite(secondInvite)
        InvitesScreen(
            navigation = rememberNavigation(),
            viewModel = invitesViewModel,
            openMenu = {},
            listViewModel = viewModel()
        )
    }
}