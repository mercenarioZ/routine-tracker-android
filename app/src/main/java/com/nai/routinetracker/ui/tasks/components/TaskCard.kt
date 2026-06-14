package com.nai.routinetracker.ui.tasks.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.TaskCategory
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.isDone
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun TaskCard(
    task: TaskItem,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = when (task.status) {
            TaskStatus.Pending -> MaterialTheme.colorScheme.surface
            TaskStatus.Done -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        },
        animationSpec = spring(),
        label = "taskCardContainer"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (task.isDone) 0.72f else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "taskCardContentAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoutineVisualDefaults.PillShape)
                    .background(RoutineVisualDefaults.categoryAccent(task.category.id))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.EventNote,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha)
                    )
                    Text(
                        text = "${task.dueLabel} • ${task.timeLabel}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isDone) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                CategoryBadge(category = task.category)
            }

            Spacer(modifier = Modifier.width(2.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggleTask(task.id) }
                )
                StatusBadge(status = task.status, contentAlpha = contentAlpha)
            }
        }
    }
}

@Composable
private fun CategoryBadge(category: TaskCategory) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(RoutineVisualDefaults.categoryAccent(category.id))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = RoutineVisualDefaults.onCategoryAccent(category.id),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusBadge(
    status: TaskStatus,
    contentAlpha: Float
) {
    Surface(
        shape = RoutineVisualDefaults.PillShape,
        color = if (status == TaskStatus.Done) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Text(
            text = status.label(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (status == TaskStatus.Done) {
                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = contentAlpha)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TaskStatus.label(): String {
    return stringResource(
        when (this) {
            TaskStatus.Pending -> R.string.task_status_pending
            TaskStatus.Done -> R.string.task_status_done
        }
    )
}
