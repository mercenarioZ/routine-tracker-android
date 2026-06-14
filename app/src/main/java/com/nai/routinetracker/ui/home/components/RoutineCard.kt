package com.nai.routinetracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun RoutineCard(
    routine: RoutineItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(RoutineVisualDefaults.CardPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(RoutineVisualDefaults.PillShape)
                        .background(RoutineVisualDefaults.categoryAccent(routine.category.id))
                )
                CategoryIcon(category = routine.category)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = routine.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryBadge(category = routine.category)
                RoutineMeta(
                    scheduleLabel = routine.scheduleLabel,
                    streakDays = routine.streakDays
                )
            }
        }
    }
}

@Composable
private fun CategoryIcon(category: RoutineCategory) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(RoutineVisualDefaults.categoryAccent(category.id))
            .padding(9.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = category.icon(),
            contentDescription = null,
            tint = RoutineVisualDefaults.onCategoryAccent(category.id)
        )
    }
}

@Composable
private fun CategoryBadge(category: RoutineCategory) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(RoutineVisualDefaults.categoryAccent(category.id))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = category.label,
            style = MaterialTheme.typography.labelLarge,
            color = RoutineVisualDefaults.onCategoryAccent(category.id),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RoutineMeta(
    scheduleLabel: String,
    streakDays: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = scheduleLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocalFireDepartment,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = pluralStringResource(
                    R.plurals.routine_streak_days,
                    streakDays,
                    streakDays
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun RoutineCategory.icon(): ImageVector {
    return when (id) {
        "health" -> Icons.Outlined.FavoriteBorder
        "learning" -> Icons.Outlined.School
        "planning" -> Icons.AutoMirrored.Outlined.EventNote
        else -> Icons.Outlined.Timer
    }
}
